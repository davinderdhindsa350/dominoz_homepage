package com.jubl.food.nextgendiscovery.service.resolver.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jubl.food.nextgendiscovery.service.resolver.DomainSiteContextResolver;
import com.jubl.food.nextgendiscovery.service.util.ValidationUtil;

/**
 * Concrete implementation of {@link DomainSiteContextResolver} that resolves
 * domain --> site context mapping from the configured. environment variables
 *
 * @author Davinder Singh
 * @version 1.0.0
 */
public class EnvironmentVariableDomainSiteContextResolver implements DomainSiteContextResolver, InitializingBean {

    /**
     * Private static class level logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(EnvironmentVariableDomainSiteContextResolver.class);

    /**
     * Domain name to site context mapping.
     */
    private final Map<String, String> domainSiteContextMapping;

    /**
     * The object mapper bean resolved by spring.
     */
    private final ObjectMapper objectMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        ValidationUtil.assertNotNull(objectMapper, domainSiteContextMapping);
    }

    /**
     * Create an instance of environment variable domain site context resolver with
     * specified application properties.
     *
     * @param objectMapperInput the jackson mapper used to parse json config
     * @throws JsonProcessingException
     * @throws JsonMappingException
     * @throws Exception               if any error occurs while parsing
     *                                 configuration
     * @param domainSiteContextConfig
     * @throws JsonProcessingException
     */
    @Autowired
    public EnvironmentVariableDomainSiteContextResolver(final String domainSiteContextConfig,
            final ObjectMapper objectMapperInput) throws JsonProcessingException {
        this.objectMapper = objectMapperInput;
        this.domainSiteContextMapping = objectMapper.readValue(domainSiteContextConfig,
                new TypeReference<HashMap<String, String>>() {
                });
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(String.format("domain site configuration specified [%s]", domainSiteContextConfig));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String resolve(final HttpServletRequest request) throws IllegalStateException {
        try {
            URI uri = new URIBuilder(request.getRequestURL().toString()).build();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(String.format("parsed domain name [%s] from the request url [%s]", uri.getHost(),
                        request.getRequestURL()));
            }
            String siteContext = this.domainSiteContextMapping.get(uri.getHost());

            if (StringUtils.isBlank(siteContext)) {
                siteContext = "jflcms";
            }

            // everything is valid return the parsed value
            return siteContext;
        } catch (URISyntaxException ex) {
            throw new IllegalStateException("cannot parse request url", ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String resolveHost(final String siteContext) throws IllegalStateException {
        String host = this.domainSiteContextMapping.entrySet().stream().filter(e -> e.getValue().equals(siteContext))
                .map(Entry::getKey).findAny().orElse(null);
        if (StringUtils.isBlank(host)) {
            throw new IllegalStateException(
                    String.format("cannot find host corresponds to site context %s", siteContext));
        }
        return host;
    }
}
