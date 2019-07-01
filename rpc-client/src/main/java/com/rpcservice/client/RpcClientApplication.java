package com.rpcservice.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RpcClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(RpcClientApplication.class, args);
	}

}
