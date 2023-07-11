package com.bezkoder.spring.jpa.h2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.dao.InvalidDataAccessApiUsageException;

@SpringBootApplication
public class SpringBootJpaH2Application {

	public static void main(String[] args) {
//		InvalidDataAccessApiUsageException
		
		SpringApplication.run(SpringBootJpaH2Application.class, args);
	}

}
