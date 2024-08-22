package org.hidog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class DogProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(DogProjectApplication.class, args);
	}

}
