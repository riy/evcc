package com.sherzad.evcc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.boot.SpringApplication;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EvccApplicationUnitTest {

    @Mock
    private SpringApplication springApplication;

    @BeforeEach
    void beforeEach() {
        EvccApplication.setSpringApplication(springApplication);
    }

    @Test
    void shouldRunApplication() {
        // when
        EvccApplication.main("arg1", "arg2");

        // then
        verify(springApplication).run("arg1", "arg2");
    }
}
