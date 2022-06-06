package com.jubl.food.nextgendiscovery.model.exception;

/**
 * Exception thrown when user don't have permission to access the specified resource.
 *
 * @author Davinder Singh
 * @version 1.0.0
 */
public class UnauthorizedException extends ApiException {

    private static final long serialVersionUID = 5767383464751144196L;

    /**
     * Create an instance of new unauthorized exception with specified code and message.
     *
     * @param code    the specified code
     * @param message the specified message
     */
    public UnauthorizedException(final String code, final String message) {
        this(code, message, null);
    }

    /**
     * Create an instance of new unauthorized exception with specified code, message and cause.
     *
     * @param code    the specified code
     * @param message the specified message
     * @param cause   the specified cause
     */
    public UnauthorizedException(final String code, final String message, final Throwable cause) {
        super(code, message, cause);
    }
}
