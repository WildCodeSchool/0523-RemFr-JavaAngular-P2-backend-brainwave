package com.templateproject.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import com.templateproject.api.service.EmailSenderService;

@SpringBootApplication
public class ApiApplication {
	@Autowired
	private EmailSenderService senderService;

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}


}
