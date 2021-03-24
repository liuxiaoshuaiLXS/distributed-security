package com.ys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class DisuributedSecurityDiscoveryApplication {

    public static void main(String[] args) {
        SpringApplication.run(DisuributedSecurityDiscoveryApplication.class, args);
    }

}
