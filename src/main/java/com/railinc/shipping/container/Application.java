package com.railinc.shipping.container;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.core.JmsTemplate;

import javax.annotation.PostConstruct;
import javax.jms.Queue;

@SpringBootApplication
public class Application   {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}


}
