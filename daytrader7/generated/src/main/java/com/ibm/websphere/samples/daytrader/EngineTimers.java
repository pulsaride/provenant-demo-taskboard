package com.ibm.websphere.samples.daytrader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.SmartLifecycle;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Engine-authored (lot V2-9, RFC-042 section 9): the non-persistent EJB calendar timers of the legacy
 * application. Each row below is one {@code @Schedule}; its callback is invoked on the bean instance
 * (not through the proxy), under the singleton lock and in a new transaction, as the EJB container does.
 * Declared limits: an expiration due while the callback still runs is skipped (L1); a callback whose
 * transaction rolled back is not retried, the next expiration proceeds (L2); the bean is created at
 * context start (L3); daylight-saving transitions are not compared (L4).
 */
@Component
public class EngineTimers implements SmartLifecycle {

    private static final Logger LOG = LoggerFactory.getLogger(EngineTimers.class);
    private static final String THREAD_PREFIX = "engine-timer-";

    enum Lock { READ, WRITE, NONE }

    enum Tx { NEW, NONE }

    /** Thrown inside the transaction for a checked failure of a callback, so the transaction rolls back. */
    static final class CallbackException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        CallbackException(Throwable cause) {
            super(cause);
        }
    }

    /** One carried timer: bean class, callback name, Spring cron text, lock, transaction. */
    static final class Row {
        private final Class<?> bean;
        private final String method;
        private final String cron;
        private final Lock lock;
        private final Tx transaction;

        private Row(Class<?> bean, String method, String cron, Lock lock, Tx transaction) {
            this.bean = bean;
            this.method = method;
            this.cron = cron;
            this.lock = lock;
            this.transaction = transaction;
        }
    }

    /** A row resolved at start: the callback, the target instance and the transaction template. */
    private static final class Resolved {
        private final Row row;
        private final Method method;
        private final Object target;
        private final TransactionTemplate template;

        private Resolved(Row row, Method method, Object target, TransactionTemplate template) {
            this.row = row;
            this.method = method;
            this.target = target;
            this.template = template;
        }
    }

    static Row row(Class<?> bean, String method, String cron, Lock lock, Tx transaction) {
        return new Row(bean, method, cron, lock, transaction);
    }

    /** Sorted by bean class, method name and cron text. */
    static List<Row> rows() {
        return List.of(
                row(com.ibm.websphere.samples.daytrader.ejb3.MarketSummarySingleton.class, "updateMarketSummary", "*/20 * * * * *", Lock.WRITE, Tx.NEW));
    }

    private final ApplicationContext context;
    private final ObjectProvider<PlatformTransactionManager> transactions;
    private List<Resolved> resolved = List.of();
    private ThreadPoolTaskScheduler scheduler;
    private ScheduledExecutorService executor;
    private boolean running;

    public EngineTimers(ApplicationContext context, ObjectProvider<PlatformTransactionManager> transactions) {
        this.context = context;
        this.transactions = transactions;
    }

    @Override
    public synchronized void start() {
        if (running) {
            return;
        }
        List<Resolved> table = new ArrayList<>();
        for (Row row : rows()) {
            table.add(resolve(row));
        }
        ThreadPoolTaskScheduler pool = new ThreadPoolTaskScheduler();
        pool.setPoolSize(table.size());
        pool.setDaemon(true);
        pool.setThreadNamePrefix(THREAD_PREFIX);
        pool.initialize();
        for (Resolved entry : table) {
            pool.schedule(() -> run(entry), new CronTrigger(entry.row.cron, ZoneId.systemDefault()));
        }
        this.resolved = List.copyOf(table);
        this.scheduler = pool;
        this.executor = pool.getScheduledExecutor();
        this.running = true;
    }

    @Override
    public synchronized void stop() {
        if (scheduler != null) {
            scheduler.shutdown();
        }
        running = false;
    }

    @Override
    public synchronized boolean isRunning() {
        return running;
    }

    /** Whether the engine scheduler was started and has been shut down. */
    public synchronized boolean isShutdown() {
        return executor != null && executor.isShutdown();
    }

    /** Runs the runner of the row of {@code bean}#{@code method} once, on the caller's thread. */
    public void fire(Class<?> bean, String method) {
        List<Resolved> table;
        synchronized (this) {
            table = resolved;
        }
        for (Resolved entry : table) {
            if (entry.row.bean.equals(bean) && entry.row.method.equals(method)) {
                run(entry);
                return;
            }
        }
        throw new IllegalArgumentException("no timer row: " + bean.getName() + "#" + method);
    }

    private Resolved resolve(Row row) {
        Method method;
        try {
            method = row.bean.getDeclaredMethod(row.method);
        } catch (NoSuchMethodException missing) {
            throw new IllegalStateException("timer row names no method: " + row.bean.getName() + "#" + row.method, missing);
        }
        method.setAccessible(true);
        Object bean = context.getBean(row.bean);
        Object target = AopUtils.isAopProxy(bean) ? AopProxyUtils.getSingletonTarget(bean) : bean;
        if (target == null) {
            throw new IllegalStateException("timer bean has no singleton target: " + row.bean.getName());
        }
        TransactionTemplate template = null;
        if (row.transaction == Tx.NEW) {
            PlatformTransactionManager manager = transactions.getIfUnique();
            if (manager == null) {
                throw new IllegalStateException("timer needs one transaction manager: " + row.bean.getName() + "#" + row.method);
            }
            template = new TransactionTemplate(manager);
            template.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        }
        return new Resolved(row, method, target, template);
    }

    private void run(Resolved entry) {
        try {
            locked(entry.row, () -> transactional(entry));
        } catch (Throwable failure) {
            LOG.error("timer callback failed, not retried: {}#{}", entry.row.bean.getName(), entry.row.method, failure);
        }
    }

    private Object locked(Row row, Callable<Object> body) throws Exception {
        if (row.lock == Lock.NONE) {
            return body.call();
        }
        return context.getBean(EngineSingletonLocks.class).callLocked(row.bean, row.lock == Lock.WRITE, body);
    }

    private Object transactional(Resolved entry) {
        if (entry.template == null) {
            return invoke(entry);
        }
        return entry.template.execute(status -> invoke(entry));
    }

    private Object invoke(Resolved entry) {
        try {
            return entry.method.invoke(entry.target);
        } catch (InvocationTargetException thrown) {
            Throwable cause = thrown.getCause();
            if (cause instanceof RuntimeException runtime) {
                throw runtime;
            }
            if (cause instanceof Error error) {
                throw error;
            }
            throw new CallbackException(cause);
        } catch (IllegalAccessException denied) {
            throw new CallbackException(denied);
        }
    }
}
