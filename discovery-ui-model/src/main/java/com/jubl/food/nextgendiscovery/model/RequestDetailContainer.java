package com.jubl.food.nextgendiscovery.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDetailContainer {

    private String domain;

    private String requestUrl;

    private String contextPath;

    private String siteContext;

    private String ipAddress;

    private String deviceId;

    private String device;

    private String userAgent;

    private String sourceCountry;

}
