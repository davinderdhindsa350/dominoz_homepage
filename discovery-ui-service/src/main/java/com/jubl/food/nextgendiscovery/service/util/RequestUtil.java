package com.jubl.food.nextgendiscovery.service.util;

import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;

import com.google.common.net.InternetDomainName;
import com.jubl.food.nextgendiscovery.model.constants.Constants;

/**
 * The request util.
 *
 * @author Davinder Singh
 * @version 1.0.0
 */
public final class RequestUtil {


    /**
     * Get cookie domain helper method.
     * For now the sub domain is not considered in cookie domain
     *
     * @param request the http servlet request
     * @return the cookie domain
     * @throws URISyntaxException
     */
    public static String getCookieDomain(final HttpServletRequest request) throws URISyntaxException {
        String host = new URIBuilder(request.getRequestURL().toString()).getHost();
        if (StringUtils.isNotBlank(host)) {
            InternetDomainName name = InternetDomainName.from(host);
            if (!name.hasPublicSuffix()) {
                // this is not under public suffix so return as it is
                return name.toString();
            }

            return name.topPrivateDomain().toString();
        }
        return InternetDomainName.from(Constants.DEFAULT_COOKIE_DOMAIN).topPrivateDomain().toString();
    }

    /**
     * Get the media path.
     *
     * @param request the http servlet path
     * @return the media path
     * @throws URISyntaxException
     * @throws Exception if any error occurs
     */
    public static String getMediaPath(final HttpServletRequest request) throws URISyntaxException {
        URIBuilder builder = new URIBuilder(request.getRequestURL().toString());
        String host = new URIBuilder(request.getRequestURL().toString()).getHost();
        InternetDomainName name = InternetDomainName.from(host);
        if (!name.hasPublicSuffix()) {
            // this is not under public suffix so return as it is
            String mediaDomain = String.format("%s.%s", Constants.MEDIA_SUB_DOMAIN, name.toString());
            builder.setHost(mediaDomain);
        } else {
            String mediaDomain = String.format("%s.%s", Constants.MEDIA_SUB_DOMAIN, name.topPrivateDomain().toString());
            builder.setHost(mediaDomain);
        }

        return builder.getHost();
    }

    /**
     *
     * @param request the request
     * @return the uri
     * @throws URISyntaxException
     */
    public static String getDiscoveryURI(final HttpServletRequest request) throws URISyntaxException {
       URI uri = new URIBuilder(request.getRequestURL().toString()).setPath("/service").build();

        return uri.toString();
    }

    private RequestUtil() {
        throw new IllegalStateException("RequestUtil class");
      }
}
