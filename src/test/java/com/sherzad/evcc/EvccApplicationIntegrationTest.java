package com.sherzad.evcc;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class EvccApplicationIntegrationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${spring.application.name}")
    private String applicationName;

    @Test
    void contextLoads() {
        assertThat(applicationContext.getEnvironment().getProperty("spring.application.name")).isEqualTo("Coding Challenge");
        assertThat(applicationName).isEqualTo("Coding Challenge");
    }
}
