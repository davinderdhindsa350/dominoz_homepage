package com.jubl.food.nextgendiscovery.service.util;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.jubl.food.nextgendiscovery.model.properties.HttpClientVariables;

@RunWith(MockitoJUnitRunner.class)
public class HttpClientConfigurationTest {
    @InjectMocks
    private HttpClientConfiguration client=new HttpClientConfiguration();
    
    @Mock
    private HttpClientVariables httpClientVariables;
    
    @Test
    public void testHttpClient() {
        MockitoAnnotations.openMocks(this);
       // httpClientVariables = mock(HttpClientVariables.class);
        when(httpClientVariables.getConnectTimeout()).thenReturn(15000);
        when(httpClientVariables.getRequestTimeOut()).thenReturn(15000);
        when(httpClientVariables.getSocketTimeOut()).thenReturn(20000);
        when(httpClientVariables.getMaxConnTotal()).thenReturn(2000);
        when(httpClientVariables.getMaxConnPerRoute()).thenReturn(1000);
        assertNotNull( client.httpClient());
    }

}
