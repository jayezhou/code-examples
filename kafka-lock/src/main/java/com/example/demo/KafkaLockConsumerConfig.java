package com.example.demo;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

@Configuration
class KafkaLockConsumerConfig {

	@Autowired
	private KafkaTemplate<String, String> kafkaLockTemplate;
	
    // @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers = "1.1.1.1:6001";
    
	// @Value("${kafka.groupLock}")
	private String groupLock = "groupLockTest";

    Map<String, Object> consumerLockConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupLock);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "100");
        
		props.put("sasl.mechanism","PLAIN");
		props.put("security.protocol","SASL_PLAINTEXT");
		props.put("sasl.jaas.config","org.apache.kafka.common.security.plain.PlainLoginModule required username=\"username\" password=\"password\";");
        return props;
    }

    private ConsumerFactory<String, String> consumerLockFactory() {
        return new DefaultKafkaConsumerFactory<String, String>(consumerLockConfigs());
    }

    //consume one by one
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerLockContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<String, String>();
        factory.setConsumerFactory(consumerLockFactory());
        factory.setReplyTemplate(kafkaLockTemplate);
        // Comment the RecordFilterStrategy if Filtering is not required        
        factory.setRecordFilterStrategy(record -> record.value().contains("ignored"));
        return factory;
    }
    
    //consume batch
    //@Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerLockBatchContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<String, String>();
        factory.setConsumerFactory(consumerLockFactory());
        factory.setBatchListener(true);
        //factory.getContainerProperties().setBatchErrorHandler(new BatchLoggingErrorHandler());
        return factory;
    }
        
    @Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> kafkaJsonListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, String> factory =
				new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerLockFactory());
		factory.setMessageConverter(new StringJsonMessageConverter());
		return factory;
	}
    
}