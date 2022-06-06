package com.jubl.food.nextgendiscovery.service.resolver;

import javax.servlet.http.HttpServletRequest;

/**
 * Domain site resolver contract.
 *
 * @author Davinder Singh
 * @version 1.0.0
 */
public interface DomainSiteContextResolver {

    /**
     * Resolve the site context from the http servlet request.
     *
     * @param request the http servlet request
     * @return the resolved site context (must never be blank/null)
     * @throws IllegalStateException if site context cannot be resolved from http request
     */
    String resolve(HttpServletRequest request) throws IllegalStateException;

    /**
     * Resolve the host from site context.
     *
     * @param siteContext the siteContext
     * @return the resolved host (must never be blank/null)
     * @throws IllegalStateException if host cannot be resolved from siteContext
     */
    String resolveHost(String siteContext) throws IllegalStateException;
}
