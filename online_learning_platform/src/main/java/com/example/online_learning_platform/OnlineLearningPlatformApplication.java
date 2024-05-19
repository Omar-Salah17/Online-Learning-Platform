package com.example.online_learning_platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class OnlineLearningPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineLearningPlatformApplication.class, args);
	}

}
