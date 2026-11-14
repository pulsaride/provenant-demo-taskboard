package com.ibm.websphere.samples.daytrader;

import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.Queue;
import jakarta.jms.Topic;
import org.apache.activemq.artemis.api.jms.ActiveMQJMSClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The JMS destinations the legacy declared (web.xml jms-destination, engine-authored, RFC-039
 * section 2.3): one bean per destination, named by its JNDI name, so a @Resource(name = N)
 * field resolves what @Resource(lookup = N) named. Listed in reserved_to_emitter.engine_files;
 * not patchable.
 */
@Configuration
public class JmsDestinations {

    @Bean(name = "jms/TradeBrokerQueue")
    public Queue jndi_jms_TradeBrokerQueue() {
        return ActiveMQJMSClient.createQueue("TradeBrokerQueue");
    }

    @Bean(name = "jms/TradeStreamerTopic")
    public Topic jndi_jms_TradeStreamerTopic() {
        return ActiveMQJMSClient.createTopic("TradeTopicSpace");
    }

    @Bean(name = "jms/QueueConnectionFactory", autowireCandidate = false, defaultCandidate = false, destroyMethod = "")
    public EngineJmsConnectionFactory.Both jndi_factory_jms_QueueConnectionFactory(
            ConnectionFactory connectionFactory, JMSContext jmsContext) {
        return EngineJmsConnectionFactory.of(connectionFactory, jmsContext);
    }

    @Bean(name = "jms/TopicConnectionFactory", autowireCandidate = false, defaultCandidate = false, destroyMethod = "")
    public EngineJmsConnectionFactory.Both jndi_factory_jms_TopicConnectionFactory(
            ConnectionFactory connectionFactory, JMSContext jmsContext) {
        return EngineJmsConnectionFactory.of(connectionFactory, jmsContext);
    }
}
