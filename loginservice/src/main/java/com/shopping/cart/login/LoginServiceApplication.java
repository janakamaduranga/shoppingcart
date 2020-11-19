package com.shopping.cart.login;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

@SpringBootApplication
@EnableDiscoveryClient
@EnableEncryptableProperties
public class LoginServiceApplication {

	public static void main(String[] args) {
        SpringApplication.run(LoginServiceApplication.class, args);
    }
}
