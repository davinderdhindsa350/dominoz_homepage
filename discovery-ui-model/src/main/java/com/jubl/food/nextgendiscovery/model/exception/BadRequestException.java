package com.jubl.food.nextgendiscovery.model.exception;

import java.util.Map;

import com.jubl.food.nextgendiscovery.model.constants.ErrorCode;

/**
 * Exception thrown if client request is invalid.
 *
 * @author Davinder Singh
 * @version 1.0.0
 */
public class BadRequestException extends ApiException {

    private static final long serialVersionUID = -2539682424715279176L;

    /**
     * Create an instance of new Bad Request Exception with specified message.
     *
     * @param message the specified message
     */
    public BadRequestException(final String message) {
        super(ErrorCode.BAD_REQUEST, message);
    }

    /**
     * Create an instance of new Bad Request Exception with specified message and code.
     *
     * @param code    the error code
     * @param message the specified message
     */
    public BadRequestException(final String code, final String message) {
        this(code, message, null);
    }

    /**
     * Create an instance of new Bad Request Exception with specified code, message and cause.
     *
     * @param code    the specified code
     * @param message the specified message
     * @param cause   the specified cause
     */
    public BadRequestException(final String code, final String message, final Throwable cause) {
        super(code, message, cause);
    }

    /**
     * Create an instance of new Bad Request Exception with specified code, message and cause.
     *
     * @param code    the specified code
     * @param message the specified message
     * @param cause   the specified cause
     * @param info    the specified info
     */
    public BadRequestException(final String code, final String message, final Throwable cause, final Map<String, Object> info) {
        super(code, message, cause, info);
    }
}
