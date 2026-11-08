package com.github.hantsy.ee8sample;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.github.hantsy.ee8sample", "provenant.engine"})
@EntityScan(basePackages = {"com.github.hantsy.ee8sample", "provenant.engine"})
public class TaskboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskboardApplication.class, args);
    }
}
