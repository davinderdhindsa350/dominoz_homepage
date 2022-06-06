package com.jubl.food.nextgendiscovery.service.resolver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * The resolve request.
 *
 * @author Davinder Singh
 * @version 1.0.0
 */
@Data
@Builder
@AllArgsConstructor
public class ResolveRequest {

    /**
     * The site name.
     */
    private final String siteName;

    /**
     * The optional request platform.
     */
    private final String platform;

    /**
     * The request lang (optional).
     */
    private final String lang;

    /**
     * The request category (optional).
     */
    private final String category;
}
