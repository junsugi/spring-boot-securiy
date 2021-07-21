package com.subject.genesislab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(scanBasePackages = {"com.subject.genesislab"})
@PropertySource({"classpath:/application.properties"})
@EnableAspectJAutoProxy
public class GenesislabApplication {

    public static void main(String[] args) {
        SpringApplication.run(GenesislabApplication.class, args);
    }

}
