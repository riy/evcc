package com.sherzad.evcc.testdata;

import com.sherzad.evcc.domain.Client;
import com.sherzad.evcc.service.ClientService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ClientTestDataCreatorUnitTest {

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientTestDataCreator clientTestDataCreator;

    @Test
    void shouldSaveContainerType() {
        // when
        clientTestDataCreator.init();

        // then
        verify(clientService, times(250)).save(any(Client.class));
    }
}
