package com.ibm.websphere.samples.daytrader;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;
import javax.naming.NamingException;
import javax.naming.spi.NamingManager;
import javax.sql.DataSource;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

/**
 * The engine naming environment (engine-authored, listed in reserved_to_emitter.engine_files;
 * not patchable; RFC-042 section 11, tier Isolated): the names the engine proved and the
 * installation of the context that answers them. Installed through the JDK's
 * initial-context-factory builder by a static bean factory post-processor, so before any
 * application bean is created and before the servlet context initialises; no jndi.properties
 * file and no system property, so Spring's environment holds no JNDI property source. Each
 * entry is resolved from the bean factory at its first lookup and kept.
 */
@Configuration(proxyBeanMethods = false)
public class EngineNamingEnvironment {

    private static volatile Map<String, Supplier<Object>> entries = Collections.emptyMap();
    private static boolean installed;

    @Bean
    static BeanFactoryPostProcessor engineNamingEnvironmentInstaller() {
        return EngineNamingEnvironment::install;
    }

    static synchronized void install(ConfigurableListableBeanFactory factory) {
        entries = table(factory);
        if (installed) {
            return;
        }
        if (NamingManager.hasInitialContextFactoryBuilder()) {
            throw new IllegalStateException("another initial context factory builder is installed");
        }
        try {
            NamingManager.setInitialContextFactoryBuilder(
                    environment -> context -> new EngineNamingContext(entries, context));
        } catch (NamingException e) {
            throw new IllegalStateException(e);
        }
        installed = true;
    }

    private static Map<String, Supplier<Object>> table(ConfigurableListableBeanFactory factory) {
        Map<String, Supplier<Object>> table = new TreeMap<>();
        table.put("java:comp/DefaultManagedThreadFactory", once(() -> factory.getBean(EngineManagedThreadFactory.Registrar.class).instance()));
        table.put("java:comp/UserTransaction", once(() -> factory.getBean(EngineUserTransaction.Registrar.class).instance()));
        table.put("java:comp/env/ejb/TradeSLSBBean", once(() -> factory.getBean(com.ibm.websphere.samples.daytrader.ejb3.TradeSLSBBean.class)));
        table.put("java:comp/env/ejb/TradeSLSBBeanRemote", once(() -> java.lang.reflect.Proxy.newProxyInstance(com.ibm.websphere.samples.daytrader.ejb3.TradeSLSBRemote.class.getClassLoader(), new Class<?>[] { com.ibm.websphere.samples.daytrader.ejb3.TradeSLSBRemote.class }, (proxy, method, args) -> { if (method.getDeclaringClass() == Object.class) { switch (method.getName()) { case "hashCode": return System.identityHashCode(proxy); case "equals": return proxy == args[0]; default: return "remote business view not carried: com.ibm.websphere.samples.daytrader.ejb3.TradeSLSBRemote"; } } throw new provenant.engine.EngineStubException("remote business view not carried: com.ibm.websphere.samples.daytrader.ejb3.TradeSLSBRemote#" + method.getName()); })));
        table.put("java:comp/env/jdbc/TradeDataSource", once(() -> EngineUserTransaction.countingDataSource(new TransactionAwareDataSourceProxy(factory.getBean(DataSource.class)))));
        table.put("java:comp/env/jms/QueueConnectionFactory", once(() -> factory.getBean("jms/QueueConnectionFactory")));
        table.put("java:comp/env/jms/TopicConnectionFactory", once(() -> factory.getBean("jms/TopicConnectionFactory")));
        table.put("java:comp/env/jms/TradeBrokerQueue", once(() -> factory.getBean("jms/TradeBrokerQueue")));
        table.put("java:comp/env/jms/TradeStreamerTopic", once(() -> factory.getBean("jms/TradeStreamerTopic")));
        return Collections.unmodifiableMap(table);
    }

    private static Supplier<Object> once(Supplier<Object> resolve) {
        return new Supplier<>() {
            private Object value;

            @Override
            public synchronized Object get() {
                if (value == null) {
                    value = resolve.get();
                }
                return value;
            }
        };
    }
}
