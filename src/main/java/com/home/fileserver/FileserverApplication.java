package com.home.fileserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EntityScan("com.home.fileserver.domain")
public class FileserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileserverApplication.class, args);
    }
}
