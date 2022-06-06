package com.jubl.food.nextgendiscovery.model.properties;

import javax.validation.constraints.NotEmpty;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Davinder Singh
 */
@Component
@ConfigurationProperties("discovery")
@Validated
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Discovery {

    /**
     * The app name.
     */
    @NotEmpty
    private String appName;

    /**
     * The app version.
     */
    @NotEmpty
    private String appVersion;

    /**
     * Thor service base url.
     */
    @NotEmpty
    private String serviceBaseUrl;

    /**
     * The domain site context configuration.
     * A valid json string of domain name to site context
     */
    @NotEmpty
    private String domainSiteContextConfig;

    /**
     * The user for actuator endpoints.
     */
    @NotEmpty
    private String actuatorEndpointsUser;

    /**
     * The password for actuator endpoints user.
     */
    @NotEmpty
    private String actuatorEndpointsUserPassword;

    /**
     * Generic error message for the server.
     */
    @NotEmpty
    private String genericErrorMessage;

    /**
     * default Tenant.
     */
    @NotEmpty
    private String defaultPlatform;

    /**
     * default Language.
     */
    @NotEmpty
    private String defaultLang;

    /**
     * default Language.
     */
    @NotEmpty
    private String defaultCategory;

    @NotEmpty
    private String resourcePath;

    @NotEmpty
    private String resourceDomain;

    @NotEmpty
    private String apiKey;

}
