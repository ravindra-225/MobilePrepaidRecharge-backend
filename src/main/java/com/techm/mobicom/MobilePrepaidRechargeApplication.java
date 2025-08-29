package com.techm.mobicom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MobilePrepaidRechargeApplication {

	public static void main(String[] args) {
		SpringApplication.run(MobilePrepaidRechargeApplication.class, args);
	}

}	
