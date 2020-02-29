package com.sherzad.evcc;

import com.google.common.annotations.VisibleForTesting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EvccApplication extends SpringApplication {

    private static SpringApplication springApplication = new EvccApplication();

    public static void main(final String... args) {
        springApplication.run(args);
    }

    @VisibleForTesting
    static void setSpringApplication(final SpringApplication springApplication) {
        EvccApplication.springApplication = springApplication;
    }

    EvccApplication() {
        super(EvccApplication.class);
    }
}
