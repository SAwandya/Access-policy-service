package com.policy.authority;

import ch.qos.logback.core.net.SyslogOutputStream;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PolicyAuthoringServiceApplication {

	public static void main(String[] args) {

		SpringApplication.run(PolicyAuthoringServiceApplication.class, args);

		System.out.println("policy authority service started");

	}

}
