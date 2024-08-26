package com.mywl.app.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableConfigurationProperties
@EnableTransactionManagement
@SpringBootApplication
public class FUllLifeCyclePlatform {

	public static void main(String[] args) {
		SpringApplication.run(FUllLifeCyclePlatform.class, args);
	}

}
