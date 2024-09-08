package com.example.calendar_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CalendarServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CalendarServiceApplication.class, args);
	}

}
