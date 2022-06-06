package com.jubl.food.nextgendiscovery.model.exception;

/**
 * Exception thrown in case the requested resource is not found on server.
 *
 * @author Davinder Singh
 * @version 1.0.0
 */
public class NotFoundException extends ApiException {

    private static final long serialVersionUID = -5678710843269253472L;

    /**
     * Create an instance of Not found exception with specified code and message.
     *
     * @param code    the specified code
     * @param message the specified message
     */
    public NotFoundException(final String code, final String message) {
        super(code, message);
    }
}
