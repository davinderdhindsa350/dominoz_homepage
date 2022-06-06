package com.jubl.food.nextgendiscovery.service.content.impl;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.jubl.food.nextgendiscovery.model.SiteConfig;
import com.jubl.food.nextgendiscovery.model.constants.Constants;
import com.jubl.food.nextgendiscovery.model.constants.ErrorCode;
import com.jubl.food.nextgendiscovery.model.properties.Discovery;
import com.jubl.food.nextgendiscovery.service.exception.ServiceException;

/**
 * Constant Data Loader.
 *
 * @author Davinder Singh
 * @version 1.0.0
 */
@Component
public class ConstantDataLoader {

    /**
     * Static class level logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ConstantDataLoader.class);

    @Autowired
    private RestTemplate restTemplate;

    /**
     */
    @Autowired
    private Discovery discovery;

    /**constantDataLoader.
     * @throws Exception
     */
    @EventListener(ApplicationReadyEvent.class)
    public void constantDataLoader() throws ServiceException {
        try {
            SiteConfig siteConfig = getSiteConfigs();
            Constants.PLATFORMS.clear();
            siteConfig.getPlatformData().stream().forEach(x -> Constants.PLATFORMS.add(x.getLinkRewrite()));
            siteConfig.getCategories().stream().forEach(x -> {
                if (x.getId() == siteConfig.getDefaultData().getCategoryId()) {
                    discovery.setDefaultCategory(x.getLinkRewrite());
                }
            });
            siteConfig.getLang().stream().forEach(x -> {
                if (x.getId() == siteConfig.getDefaultData().getLangId()) {
                    discovery.setDefaultLang(x.getLanguageCode());
                }
            });
            siteConfig.getPlatformData().stream().forEach(x -> {
                if (x.getId() == siteConfig.getDefaultData().getPlatformId()) {
                    discovery.setDefaultPlatform(x.getLinkRewrite());
                }
            });

        } catch (ServiceException e) {
            if (Constants.PLATFORMS.isEmpty()) {
                Constants.PLATFORMS.add("pwa");
                Constants.PLATFORMS.add("desktop");
                Constants.PLATFORMS.add("ios");
                Constants.PLATFORMS.add("android");
            }
            throw e;
        }
    }

    private SiteConfig getSiteConfigs() throws ServiceException {

        try {
            final URI baseUri = new URIBuilder(discovery.getServiceBaseUrl()).build();

            final URIBuilder applicationUriBuilder = new URIBuilder(baseUri);

            if (StringUtils.isNotBlank(baseUri.getPath())) {
                applicationUriBuilder.setPath(String.format("%s%s", baseUri.getPath(), Constants.CONSTANT_DATA_PATH));
            } else {
                applicationUriBuilder.setPath(Constants.CONSTANT_DATA_PATH);
            }

            applicationUriBuilder.addParameter("site", "jflcms");

            URI applicationURI = applicationUriBuilder.build();
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("external service uri {}", applicationURI);
            }

            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>("body", headers);
            ResponseEntity<SiteConfig> response = restTemplate.exchange(applicationURI, HttpMethod.GET, entity,
                    SiteConfig.class);
            LOGGER.info("external service response status code {}", response.getStatusCode());
            if (response.getStatusCodeValue() == HttpStatus.SC_OK) {
                return response.getBody();
            }

            // if any other status throw exception
            throw new ServiceException(ErrorCode.NON_OK_STATUS_CODE, response.getStatusCode().toString());

        } catch (URISyntaxException ex) {
            throw new ServiceException(ErrorCode.EXTERNAL_SERVICE_ERROR, "failed to call external service", ex);
        }
    }
}
