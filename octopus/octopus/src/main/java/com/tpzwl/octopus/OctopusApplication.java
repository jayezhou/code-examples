package com.tpzwl.octopus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@SpringBootApplication
public class OctopusApplication {
	
	// 这里使用包名而不是import包名，因为以后可能还要启动另外一个产品的tcp server
    private final com.tpzwl.octopus.netty.chedian.TCPServer chedianTcpServer; 

	public static void main(String[] args) {
		SpringApplication.run(OctopusApplication.class, args);
	}

    @Bean
    public ApplicationListener<ApplicationReadyEvent> readyEventApplicationListener() {
        return new ApplicationListener<ApplicationReadyEvent>() {
            @Override
            public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
                chedianTcpServer.start();
            }
        };
    }
    
}
