package com.example.demo;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

/**
 * KafkaProducerConfig
 */
@Configuration
class KafkaProducerConfig {

	private final Logger LOG = LoggerFactory.getLogger(getClass());

    // @Value("${kafka.bootstrap-servers}")
	private String bootstrapServers = "1.1.1.1:6001";
	
	// @Value("${kafka.topicLock}")
	private String topicLock = "topicLockTest";

	@Bean
	Map<String, Object> producerLockConfigs() {
		Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		
		props.put("sasl.mechanism","PLAIN");
		props.put("security.protocol","SASL_PLAINTEXT");
		props.put("sasl.jaas.config","org.apache.kafka.common.security.plain.PlainLoginModule required username=\"username\" password=\"password\";");
		
		props.put(bootstrapServers, props);
		return props;
	}

	@Bean
	ProducerFactory<String, String> producerLockFactory() {
		return new DefaultKafkaProducerFactory<>(producerLockConfigs());
	}

	@Bean
	KafkaTemplate<String, String> kafkaLockTemplate() {
		KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(producerLockFactory());
		kafkaTemplate.setMessageConverter(new StringJsonMessageConverter());
		kafkaTemplate.setDefaultTopic(topicLock);
		kafkaTemplate.setProducerListener(new ProducerListener<String, String>() {
			@Override
			public void onSuccess(ProducerRecord<String, String> producerRecord, RecordMetadata recordMetadata) {
				LOG.info("ACK from ProducerListener message: {} offset:  {}", producerRecord.value(),
						recordMetadata.offset());
			}
		});
		return kafkaTemplate;
	}

}