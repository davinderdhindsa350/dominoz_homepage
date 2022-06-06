package com.jubl.food.nextgendiscovery.service.content.impl;

import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.Cookie;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.jubl.food.nextgendiscovery.model.ContentRequest;
import com.jubl.food.nextgendiscovery.model.constants.Constants;
import com.jubl.food.nextgendiscovery.model.constants.ErrorCode;
import com.jubl.food.nextgendiscovery.model.domain.PageContent;
import com.jubl.food.nextgendiscovery.model.exception.ApiException;
import com.jubl.food.nextgendiscovery.model.properties.Discovery;
import com.jubl.food.nextgendiscovery.service.exception.ServiceException;
import com.jubl.food.nextgendiscovery.service.util.ValidationUtil;

/**
 * Default implementation of {@link ContentLoader}.
 *
 * @author Davinder Singh
 * @version 1.0.0
 */
@Component
public class DefaultContentLoader implements InitializingBean {

    /**
     * Static class level logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultContentLoader.class);

    @Autowired
    private RestTemplate restTemplate;

    /**
     */
    @Autowired
    private Discovery discovery;

    /**
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        ValidationUtil.assertNotNull(restTemplate, discovery);
    }

    /**
     * Load the page content based on the content request.
     * @param request the content request
     * @param cache   whether to use cache or not
     * @return the loaded page content from external service
     * @throws ApiException if any exception occurs while loading
     * @param cookies
     * @return
     */
    public PageContent load(final ContentRequest request, final Boolean cache, final Cookie[] cookies)
            throws ApiException {
        try {
            final URI baseUri = new URIBuilder(discovery.getServiceBaseUrl()).build();

            final URIBuilder applicationUriBuilder = new URIBuilder(baseUri);

            if (StringUtils.isNotBlank(baseUri.getPath())) {
                applicationUriBuilder.setPath(String.format("%s%s", baseUri.getPath(), Constants.LOAD_DATA_PATH));
            } else {
                applicationUriBuilder.setPath(Constants.LOAD_DATA_PATH);
            }

            applicationUriBuilder.addParameter("site", request.getSite())
                    .addParameter("platform", request.getPlatform()).addParameter("category", request.getCategory())
                    .addParameter("lang", request.getLang());
            if (request.getStoreId() != null) {
                applicationUriBuilder.addParameter("storeId", request.getStoreId().toString());
            }
            URI applicationURI = applicationUriBuilder.build();
            LOGGER.info("external service uri {}", applicationURI);
            LOGGER.info("api_key {}", discovery.getApiKey());

            HttpGet httpGet = new HttpGet(applicationURI);

            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (Constants.USER_POLL_COOKIE_NAME.equals(cookie.getName())) {
                        httpGet.addHeader(new BasicHeader(Constants.USER_POLL_HEADER_NAME, cookie.getValue()));
                    }
                }
            }
            HttpHeaders headers = new HttpHeaders();
            headers.add("api_key", discovery.getApiKey());
            headers.add("If-None-Match", request.getETag());
            HttpEntity<String> entity = new HttpEntity<>("some body", headers);
            ResponseEntity<PageContent> response = restTemplate.exchange(applicationURI, HttpMethod.GET, entity, PageContent.class);
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
