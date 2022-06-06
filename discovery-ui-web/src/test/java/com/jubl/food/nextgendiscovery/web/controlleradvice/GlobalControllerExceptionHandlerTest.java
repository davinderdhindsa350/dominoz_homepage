package com.jubl.food.nextgendiscovery.web.controlleradvice;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import com.jubl.food.nextgendiscovery.model.exception.ApiException;
import com.jubl.food.nextgendiscovery.model.exception.BadRequestException;
import com.jubl.food.nextgendiscovery.model.exception.NotFoundException;
import com.jubl.food.nextgendiscovery.model.exception.UnauthorizedException;
import com.jubl.food.nextgendiscovery.model.properties.Discovery;
import com.jubl.food.nextgendiscovery.service.exception.ServiceException;
@RunWith(MockitoJUnitRunner.class)
public class GlobalControllerExceptionHandlerTest {

    @Mock
    private  Discovery discovery;

    @InjectMocks
    private GlobalControllerExceptionHandler controllerExceptionHandler=new GlobalControllerExceptionHandler();
    
    @BeforeEach
     void init() {
        MockitoAnnotations.initMocks(this);
       // discovery = mock(Discovery.class);
        when(discovery.getAppName()).thenReturn("TestDiscovery");
        when(discovery.getAppVersion()).thenReturn("1.0.0");
    }
    
    @Test
    public void testHandleThrowable() {
        ApiException ex =new ApiException("500", "test");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, controllerExceptionHandler.handleThrowable(ex).getStatusCode());
    }

    @Test
    public void testHandleBadRequestException() {
        BadRequestException ex =new BadRequestException("500", "test");
        assertEquals(HttpStatus.BAD_REQUEST, controllerExceptionHandler.handleBadRequestException(ex).getStatusCode());
    }

    @Test
    public void testHandleThorException() {
        ApiException ex =new ApiException("500", "test");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, controllerExceptionHandler.handleThorException(ex).getStatusCode());
        ApiException ex1 =new BadRequestException("500", "test");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, controllerExceptionHandler.handleThorException(ex1).getStatusCode());
        ApiException ex2 =new NotFoundException("500", "test");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, controllerExceptionHandler.handleThorException(ex2).getStatusCode());
        ApiException ex3 =new UnauthorizedException("500", "test");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, controllerExceptionHandler.handleThorException(ex3).getStatusCode());
   
    }

    @Test
    public void testHandleNotFoundException() {
        NotFoundException ex =new NotFoundException("500", "test");
        assertEquals(HttpStatus.NOT_FOUND, controllerExceptionHandler.handleNotFoundException(ex).getStatusCode());
    }

    @Test
    public void testHandleServiceException() {
        ServiceException ex =new ServiceException("500", "test");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, controllerExceptionHandler.handleServiceException(ex).getStatusCode());
    }

    @Test
    public void testHandleUnauthorizedException() {
        UnauthorizedException ex =new UnauthorizedException("500", "test");
        assertEquals(HttpStatus.UNAUTHORIZED, controllerExceptionHandler.handleUnauthorizedException(ex).getStatusCode());
    }

}
