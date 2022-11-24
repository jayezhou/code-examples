package com.example.demo;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
class InitSend {
	
	private final Logger LOG = LoggerFactory.getLogger(getClass());
	
	//@Value("${kafka.topicLockTest}")
	private String topicLockTest = "topicLockTest";
	
	@Autowired
	private KafkaSenderExample kafkaSenderExample;
	
	@SuppressWarnings("unused")
	@Autowired
	private KafkaSenderWithMessageConverter messageConverterSender;
		
	@EventListener
	void initiateSendingMessage(ApplicationReadyEvent event) throws InterruptedException {
		for (int i= 0; i< 20; i++) {
			Thread.sleep(1000);
	    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    	String strTime =sdf.format(Calendar.getInstance().getTime());
			LOG.info("---------------------------------");
			kafkaSenderExample.sendMessage("Message time: " + strTime, topicLockTest);
		}
	}
}
