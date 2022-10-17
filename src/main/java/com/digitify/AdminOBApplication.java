package com.digitify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableEurekaClient
@ComponentScan("com")
@EnableJpaRepositories({"com.digitify.unit.*"})
@EntityScan({"com.validator.entity","com.digitify.unit.*"})
public class AdminOBApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminOBApplication.class, args);
    }

}
