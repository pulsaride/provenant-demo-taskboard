package com.ibm.websphere.samples.daytrader;

import jakarta.transaction.NotSupportedException;
import jakarta.transaction.RollbackException;
import jakarta.transaction.Status;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.sql.DataSource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.jdbc.datasource.DelegatingDataSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Engine-authored (lot V2-5b, RFC-042 section 5; listed in
 * reserved_to_emitter.engine_files, not patchable; tier Isolated): the object
 * the name {@code java:comp/UserTransaction} answers and a bare
 * {@code @Resource UserTransaction} field is injected with, over the unit's
 * single transaction manager. Each method follows the interface's contract.
 * Run-time guard: {@code begin} refuses while the calling thread holds an open
 * connection of the engine datasource entry (a bean-managed transaction begun
 * after the connection is not carried).
 */
public final class EngineUserTransaction implements UserTransaction {

    /** The refusal of the run-time guard. */
    static final String OPEN_CONNECTION =
            "bean-managed transaction not carried: a connection opened before begin is still open";

    /** Open connections of the engine datasource entry, per opening thread. */
    private static final ThreadLocal<AtomicInteger> OPEN = ThreadLocal.withInitial(AtomicInteger::new);

    /** The transaction this object began on the thread. */
    private static final ThreadLocal<TransactionStatus> CURRENT = new ThreadLocal<>();

    /** Registers the object as a resolvable dependency, not as a bean definition. */
    @Component
    public static class Registrar implements BeanFactoryPostProcessor {

        private EngineUserTransaction instance;

        /** The object this registrar made (the naming environment binds it). */
        public EngineUserTransaction instance() {
            return instance;
        }

        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
            EngineUserTransaction transactions = new EngineUserTransaction(beanFactory.getBeanProvider(PlatformTransactionManager.class));
            beanFactory.registerResolvableDependency(UserTransaction.class, transactions);
            this.instance = transactions;
        }
    }

    private final ObjectProvider<PlatformTransactionManager> managers;

    EngineUserTransaction(ObjectProvider<PlatformTransactionManager> managers) {
        this.managers = managers;
    }

    /** The datasource entry seen through this object: each connection is counted on its opening thread until closed. */
    public static DataSource countingDataSource(DataSource target) {
        return new DelegatingDataSource(target) {
            @Override
            public Connection getConnection() throws SQLException {
                return counted(super.getConnection());
            }

            @Override
            public Connection getConnection(String username, String password) throws SQLException {
                return counted(super.getConnection(username, password));
            }
        };
    }

    private static Connection counted(Connection connection) {
        AtomicInteger open = OPEN.get();
        open.incrementAndGet();
        AtomicBoolean closed = new AtomicBoolean();
        return (Connection) Proxy.newProxyInstance(EngineUserTransaction.class.getClassLoader(), new Class<?>[] { Connection.class },
                (proxy, method, args) -> {
                    switch (method.getName()) {
                        case "equals":
                            return proxy == args[0];
                        case "hashCode":
                            return System.identityHashCode(proxy);
                        default:
                            break;
                    }
                    try {
                        return method.invoke(connection, args);
                    } catch (InvocationTargetException e) {
                        throw e.getTargetException();
                    } finally {
                        if ("close".equals(method.getName()) && closed.compareAndSet(false, true)) {
                            open.decrementAndGet();
                        }
                    }
                });
    }

    @Override
    public void begin() throws NotSupportedException, SystemException {
        if (CURRENT.get() != null || TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new NotSupportedException("a transaction is already active on this thread");
        }
        if (OPEN.get().get() > 0) {
            throw new provenant.engine.EngineStubException(OPEN_CONNECTION);
        }
        try {
            CURRENT.set(managers.getObject().getTransaction(new DefaultTransactionDefinition()));
        } catch (TransactionException e) {
            throw system(e);
        }
    }

    @Override
    public void commit() throws RollbackException, SystemException {
        TransactionStatus status = held();
        CURRENT.remove();
        PlatformTransactionManager manager = managers.getObject();
        if (status.isRollbackOnly()) {
            try {
                manager.rollback(status);
            } catch (TransactionException e) {
                throw system(e);
            }
            throw new RollbackException("the transaction was marked for rollback and has been rolled back");
        }
        try {
            manager.commit(status);
        } catch (UnexpectedRollbackException e) {
            throw rolledBack(e);
        } catch (TransactionException e) {
            throw system(e);
        } catch (RuntimeException e) {
            throw rolledBack(e);
        }
    }

    @Override
    public void rollback() throws SystemException {
        TransactionStatus status = held();
        CURRENT.remove();
        try {
            managers.getObject().rollback(status);
        } catch (TransactionException e) {
            throw system(e);
        }
    }

    @Override
    public void setRollbackOnly() {
        held().setRollbackOnly();
    }

    @Override
    public int getStatus() {
        TransactionStatus status = CURRENT.get();
        if (status == null) {
            return TransactionSynchronizationManager.isActualTransactionActive()
                    ? Status.STATUS_ACTIVE
                    : Status.STATUS_NO_TRANSACTION;
        }
        return status.isRollbackOnly() ? Status.STATUS_MARKED_ROLLBACK : Status.STATUS_ACTIVE;
    }

    @Override
    public void setTransactionTimeout(int seconds) {
        throw new provenant.engine.EngineStubException("UserTransaction.setTransactionTimeout not carried");
    }

    private static TransactionStatus held() {
        TransactionStatus status = CURRENT.get();
        if (status == null) {
            throw new IllegalStateException("no transaction is active on this thread");
        }
        return status;
    }

    private static SystemException system(Exception cause) {
        SystemException failure = new SystemException(cause.getMessage());
        failure.initCause(cause);
        return failure;
    }

    private static RollbackException rolledBack(Exception cause) {
        RollbackException failure = new RollbackException(cause.getMessage());
        failure.initCause(cause);
        return failure;
    }
}
