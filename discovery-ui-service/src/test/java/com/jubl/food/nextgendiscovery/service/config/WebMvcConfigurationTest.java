package com.jubl.food.nextgendiscovery.service.config;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
@RunWith(MockitoJUnitRunner.class)
public class WebMvcConfigurationTest {
    
    @InjectMocks
    private WebMvcConfiguration configuration=new WebMvcConfiguration();
    
    @Mock
    private ViewResolverRegistry registry;

    @Mock
    private ResourceHandlerRegistry handlerRegistry;
    
    @BeforeEach
     void init() {
        MockitoAnnotations.openMocks(this);
        //registry = mock(ViewResolverRegistry.class);
        //handlerRegistry = mock(ResourceHandlerRegistry.class);
        ResourceHandlerRegistration handlerRegistration=mock(ResourceHandlerRegistration.class);
        when(handlerRegistry.addResourceHandler(anyString())).thenReturn(handlerRegistration );
        
    }

    @Test
    public void testConfigureViewResolvers() {
        assertDoesNotThrow(()-> configuration.configureViewResolvers(registry));
    }

    
//      @Test 
//      public void testAddResourceHandlers() { 
// 
//      assertDoesNotThrow(()-> configuration.addResourceHandlers(handlerRegistry));
//      }
     

}
