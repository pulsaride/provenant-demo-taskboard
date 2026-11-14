package com.ibm.websphere.samples.daytrader;

import jakarta.enterprise.concurrent.ManagedExecutorService;
import jakarta.enterprise.concurrent.ManagedScheduledExecutorService;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.stereotype.Component;

/**
 * Engine-authored (lot V2-12a, RFC-042 section 12a): the default managed
 * scheduled executor a bare {@code @Resource ManagedScheduledExecutorService}
 * field is injected with. A private {@link ScheduledThreadPoolExecutor} runs
 * the tasks on daemon threads; it is shut down when the context closes. Only
 * the {@code execute}, {@code submit}, {@code schedule} (with a delay),
 * {@code scheduleAtFixedRate} and {@code scheduleWithFixedDelay} methods run;
 * every other method throws {@link UnsupportedManagedExecutorOperation}. The
 * two types of the interface's own signatures that the kept row does not list
 * ({@code ContextService}, {@code Trigger}) are written qualified, never
 * imported.
 */
public final class EngineManagedScheduledExecutor implements ManagedScheduledExecutorService, DisposableBean {

    /** The pool bound: an engine constant where the legacy server's is its own configuration. */
    static final int POOL_SIZE = 4;

    /** The bean name under which the pool's shutdown is registered (no bean definition exists). */
    static final String DISPOSABLE_NAME = "engineManagedScheduledExecutor";

    /** Thrown by every method outside the admitted table. */
    public static final class UnsupportedManagedExecutorOperation extends UnsupportedOperationException {

        private static final long serialVersionUID = 1L;

        UnsupportedManagedExecutorOperation(String method) {
            super("the engine managed executor does not carry " + method);
        }
    }

    /** Registers the executor as a resolvable dependency, not as a bean definition. */
    @Component
    public static class Registrar implements BeanFactoryPostProcessor {

        private EngineManagedScheduledExecutor instance;

        /** The executor this registrar made (the naming environment binds it under the default names). */
        public EngineManagedScheduledExecutor instance() {
            return instance;
        }

        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
            if (!(beanFactory instanceof DefaultSingletonBeanRegistry registry)) {
                throw new IllegalStateException(
                        "the engine managed executor needs a bean factory that keeps disposable beans");
            }
            EngineManagedScheduledExecutor executor = new EngineManagedScheduledExecutor();
            beanFactory.registerResolvableDependency(ManagedScheduledExecutorService.class, executor);
            beanFactory.registerResolvableDependency(ManagedExecutorService.class, executor);
            this.instance = executor;
            registry.registerDisposableBean(DISPOSABLE_NAME, executor);
        }
    }

    private final ScheduledThreadPoolExecutor pool;

    EngineManagedScheduledExecutor() {
        AtomicInteger counter = new AtomicInteger();
        ThreadFactory threads = task -> {
            Thread thread = new Thread(task, "engine-managed-executor-" + counter.incrementAndGet());
            thread.setDaemon(true);
            return thread;
        };
        this.pool = new ScheduledThreadPoolExecutor(POOL_SIZE, threads);
        this.pool.setRemoveOnCancelPolicy(true);
        this.pool.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
        this.pool.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
    }

    @Override
    public void destroy() {
        pool.shutdownNow();
    }

    // --- the admitted table -------------------------------------------------

    @Override
    public void execute(Runnable command) {
        pool.execute(command);
    }

    @Override
    public Future<?> submit(Runnable task) {
        return pool.submit(task);
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return pool.submit(task, result);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return pool.submit(task);
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        return pool.schedule(command, delay, unit);
    }

    @Override
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
        return pool.schedule(callable, delay, unit);
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        return pool.scheduleAtFixedRate(command, initialDelay, period, unit);
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        return pool.scheduleWithFixedDelay(command, initialDelay, delay, unit);
    }

    // --- everything else throws ---------------------------------------------

    @Override
    public ScheduledFuture<?> schedule(Runnable command, jakarta.enterprise.concurrent.Trigger trigger) {
        throw new UnsupportedManagedExecutorOperation("schedule(Runnable, Trigger)");
    }

    @Override
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, jakarta.enterprise.concurrent.Trigger trigger) {
        throw new UnsupportedManagedExecutorOperation("schedule(Callable, Trigger)");
    }

    @Override
    public <U> CompletableFuture<U> completedFuture(U value) {
        throw new UnsupportedManagedExecutorOperation("completedFuture");
    }

    @Override
    public <U> CompletionStage<U> completedStage(U value) {
        throw new UnsupportedManagedExecutorOperation("completedStage");
    }

    @Override
    public <T> CompletableFuture<T> copy(CompletableFuture<T> stage) {
        throw new UnsupportedManagedExecutorOperation("copy(CompletableFuture)");
    }

    @Override
    public <T> CompletionStage<T> copy(CompletionStage<T> stage) {
        throw new UnsupportedManagedExecutorOperation("copy(CompletionStage)");
    }

    @Override
    public <U> CompletableFuture<U> failedFuture(Throwable ex) {
        throw new UnsupportedManagedExecutorOperation("failedFuture");
    }

    @Override
    public <U> CompletionStage<U> failedStage(Throwable ex) {
        throw new UnsupportedManagedExecutorOperation("failedStage");
    }

    @Override
    public jakarta.enterprise.concurrent.ContextService getContextService() {
        throw new UnsupportedManagedExecutorOperation("getContextService");
    }

    @Override
    public <U> CompletableFuture<U> newIncompleteFuture() {
        throw new UnsupportedManagedExecutorOperation("newIncompleteFuture");
    }

    @Override
    public CompletableFuture<Void> runAsync(Runnable runnable) {
        throw new UnsupportedManagedExecutorOperation("runAsync");
    }

    @Override
    public <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier) {
        throw new UnsupportedManagedExecutorOperation("supplyAsync");
    }

    @Override
    public void shutdown() {
        throw new UnsupportedManagedExecutorOperation("shutdown");
    }

    @Override
    public List<Runnable> shutdownNow() {
        throw new UnsupportedManagedExecutorOperation("shutdownNow");
    }

    @Override
    public boolean isShutdown() {
        throw new UnsupportedManagedExecutorOperation("isShutdown");
    }

    @Override
    public boolean isTerminated() {
        throw new UnsupportedManagedExecutorOperation("isTerminated");
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) {
        throw new UnsupportedManagedExecutorOperation("awaitTermination");
    }

    @Override
    public void close() {
        throw new UnsupportedManagedExecutorOperation("close");
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) {
        throw new UnsupportedManagedExecutorOperation("invokeAll");
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) {
        throw new UnsupportedManagedExecutorOperation("invokeAll");
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) {
        throw new UnsupportedManagedExecutorOperation("invokeAny");
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) {
        throw new UnsupportedManagedExecutorOperation("invokeAny");
    }
}
