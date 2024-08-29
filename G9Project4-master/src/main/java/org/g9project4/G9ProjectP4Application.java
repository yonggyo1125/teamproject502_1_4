package org.g9project4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class G9ProjectP4Application {

	public static void main(String[] args) {
		SpringApplication.run(G9ProjectP4Application.class, args);
	}

}
