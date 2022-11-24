package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
class KafkaSenderExample {

	private final Logger LOG = LoggerFactory.getLogger(KafkaSenderExample.class);

	private KafkaTemplate<String, String> kafkaLockTemplate;

	@Autowired
	KafkaSenderExample(KafkaTemplate<String, String> kafkaLockTemplate) {
		this.kafkaLockTemplate = kafkaLockTemplate;
	}

	void sendMessage(String message, String topicName) {
		LOG.info("Sending : {}", message);
		LOG.info("--------------------------------");

		kafkaLockTemplate.send(topicName, message);
//		Random random = new Random();
//		String key = Integer.valueOf(random.nextInt()).toString();
//		LOG.info("key: " + key);
//		kafkaLockTemplate.send(topicName, key, message);
	}

	void sendMessageWithCallback(String message, String topicName) {
		LOG.info("Sending : {}", message);
		LOG.info("---------------------------------");

		ListenableFuture<SendResult<String, String>> future = kafkaLockTemplate.send(topicName, message);

		future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
			@Override
			public void onSuccess(SendResult<String, String> result) {
				LOG.info("Success Callback: [{}] delivered with offset -{}", message,
						result.getRecordMetadata().offset());
			}

			@Override
			public void onFailure(Throwable ex) {
				LOG.warn("Failure Callback: Unable to deliver message [{}]. {}", message, ex.getMessage());
			}
		});
	}

}
