package com.jubl.food.nextgendiscovery.model.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@ConfigurationProperties("jubl.http-client")
@Validated
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HttpClientVariables {

    private Integer connectTimeout;

    private Integer requestTimeOut;

    private Integer socketTimeOut;

    private Integer maxConnTotal;

    private Integer maxConnPerRoute;

}
