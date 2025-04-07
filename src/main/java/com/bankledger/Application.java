package com.bankledger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.bankledger")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}