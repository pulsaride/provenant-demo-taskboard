package com.ibm.websphere.samples.daytrader;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.ibm.websphere.samples.daytrader", "provenant.engine"})
@EntityScan(basePackages = {"com.ibm.websphere.samples.daytrader", "provenant.engine"})
public class Daytrader7Application {

    public static void main(String[] args) {
        SpringApplication.run(Daytrader7Application.class, args);
    }
}
