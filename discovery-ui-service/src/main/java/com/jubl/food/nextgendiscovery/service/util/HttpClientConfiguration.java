package com.jubl.food.nextgendiscovery.service.util;

import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import com.google.common.collect.Lists;
import com.jubl.food.nextgendiscovery.model.properties.HttpClientVariables;

@Configuration
public class HttpClientConfiguration {

    @Autowired
    private HttpClientVariables httpClientVariables;

    /**
     * afterPropertiesSet.
     */
    public void afterPropertiesSet() {
        ValidationUtil.assertNotNull(httpClientVariables);
    }

    /**
     * httpClient.
     * @return CloseableHttpClient
     */
    @Bean
    public CloseableHttpClient httpClient() {
        final RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(httpClientVariables.getConnectTimeout())
                .setConnectionRequestTimeout(httpClientVariables.getRequestTimeOut())
                .setSocketTimeout(httpClientVariables.getSocketTimeOut()).build();

        Header acceptHeader = new BasicHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        List<Header> defaultHeaders = Lists.newArrayList(acceptHeader);

        return HttpClients.custom()
                .setMaxConnTotal(httpClientVariables.getMaxConnTotal())
                .setMaxConnPerRoute(httpClientVariables.getMaxConnPerRoute())
                .setDefaultRequestConfig(requestConfig)
                .setDefaultHeaders(defaultHeaders)
                .build();
    }
}
