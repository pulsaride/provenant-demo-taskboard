package com.ibm.websphere.samples.daytrader;

import jakarta.enterprise.concurrent.ManagedThreadFactory;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * Engine-authored (lot V2-12, RFC-042 section 12b): the default managed thread
 * factory a bare {@code @Resource ManagedThreadFactory} field is injected with
 * and the name {@code java:comp/DefaultManagedThreadFactory} answers.
 * {@code newThread(Runnable)} returns a new daemon thread over the task; the
 * thread is not signalled when the context closes. The fork-join overload is
 * not reachable from a carried body and throws
 * {@link UnsupportedManagedThreadFactoryOperation}.
 */
public final class EngineManagedThreadFactory implements ManagedThreadFactory {

    /** Thrown by every method outside the admitted one. */
    public static final class UnsupportedManagedThreadFactoryOperation extends UnsupportedOperationException {

        private static final long serialVersionUID = 1L;

        UnsupportedManagedThreadFactoryOperation(String method) {
            super("the engine managed thread factory does not carry " + method);
        }
    }

    /** Registers the factory as a resolvable dependency, not as a bean definition. */
    @Component
    public static class Registrar implements BeanFactoryPostProcessor {

        private EngineManagedThreadFactory instance;

        /** The factory this registrar made (the naming environment binds it under the default name). */
        public EngineManagedThreadFactory instance() {
            return instance;
        }

        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
            EngineManagedThreadFactory factory = new EngineManagedThreadFactory();
            beanFactory.registerResolvableDependency(ManagedThreadFactory.class, factory);
            this.instance = factory;
        }
    }

    private final AtomicInteger counter = new AtomicInteger();

    EngineManagedThreadFactory() {
    }

    @Override
    public Thread newThread(Runnable task) {
        Thread thread = new Thread(task, "engine-managed-thread-" + counter.incrementAndGet());
        thread.setDaemon(true);
        return thread;
    }

    @Override
    public ForkJoinWorkerThread newThread(ForkJoinPool pool) {
        throw new UnsupportedManagedThreadFactoryOperation("newThread(ForkJoinPool)");
    }
}
