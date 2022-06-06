package com.jubl.food.nextgendiscovery.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jubl.food.nextgendiscovery.model.constants.ErrorCode;
import com.jubl.food.nextgendiscovery.model.exception.BadRequestException;
import com.jubl.food.nextgendiscovery.service.util.ValidationUtil;

import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Root controller for all the api's.
 *
 * @author Davinder Singh
 * @version 1.0.0
 */
public abstract class AbstractApiController implements InitializingBean {

    /**
     * The jackson object mapper to write json.
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Callback after this bean properties are set.
     * First validates this bean properties and than delegate to {@link #afterPropertiesSetInternal()}
     *
     * @throws Exception if there is an error while validating
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        ValidationUtil.assertNotNull(objectMapper);
    }

    /**
     * Callback to be implemented by child classes to be notified of bean properties are set.
     *
     * @throws Exception if any exception occurs
     */
    protected abstract void afterPropertiesSetInternal();

    /**
     * Log the method entry.
     *
     * @param logger the logger to use for logging
     * @param name   the class and method name
     * @param args   any additional arguments
     * @throws JsonProcessingException if any json processing error occurs
     */
    protected void logEntry(final Logger logger, final String name, final Object... args) throws BadRequestException {
        try {
            String json = objectMapper.writeValueAsString(args);
            logger.debug("entering controller method - {}, args {}", name, json);
        } catch (JsonProcessingException jpe) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST, "malformed request body resulted in unexpected error", jpe);
        }
    }
}
