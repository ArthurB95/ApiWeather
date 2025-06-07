package com.example.api_wheater;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@EnableConfigurationProperties
public class ApiWheaterApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiWheaterApplication.class, args);
	}

}
