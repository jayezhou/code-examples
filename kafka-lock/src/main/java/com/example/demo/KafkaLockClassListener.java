package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
//@KafkaListener(id = "${kafka.groupLock}", topics = "${kafka.topicLock}", containerFactory = "kafkaListenerLockContainerFactory")
@KafkaListener(id = "groupLockTest", topics = "topicLockTest", containerFactory = "kafkaListenerLockContainerFactory")
class KafkaLockClassListener {

	private final Logger LOG = LoggerFactory.getLogger(getClass());
	
	@KafkaHandler
	void listen(String message) {
		LOG.info("ClassLevel KafkaHandler[String] {}", message);
	}

//	@KafkaHandler(isDefault = true)
//	void listenDefault(Object object) {
//		LOG.info("ClassLevel KafkaHandler[Default] {}", object);
//	}
	
}
