package com.toktok;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ToktokApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToktokApplication.class, args);
        
    }
}
