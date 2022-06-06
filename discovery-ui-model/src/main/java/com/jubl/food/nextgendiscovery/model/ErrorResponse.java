package com.jubl.food.nextgendiscovery.model;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The generic error response for all the api's.
 *
 * @author Davinder Singh
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    /**
     * The application name.
     */
    private String appName;

    /**
     * The application version.
     */
    private String appVersion;

    /**
     * The error code.
     */
    private String errorCode;

    /**
     * The error message based on locale.
     */
    private String errorMessage;

    /**
     * The detail error message.
     */
    private String detailErrorMessage;

    /**
     * Any additional information to be passed to clients.
     */
    private Map<String, Object> info;


}
