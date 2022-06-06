package com.jubl.food.nextgendiscovery.model.response;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.jubl.food.nextgendiscovery.model.ContentResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The parse seo url response dto.
 *
 * @author Davinder Singh
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParseSeoUrlResponse implements Serializable {

    /**
     * Serializable identifier.
     */
    private static final long serialVersionUID = 3674483401937501202L;

    /**
     * The page number.
     */
    private Integer page;

    /**
     * The query parameter.
     */
    private String query;

    /**
     * The locations.
     */
    private List<String> locations;

    /**
     * Seo category.
     */
    private String seoCategory;

    /**
     * Seo category.
     */
    private String queryString;

    private String requestPath;

    private String requestPathWithoutPage;

    private ContentResponse contentResponse;

    private String xcode;

    private String siteDomain;

    private String siteBaseUrl;

    private String categoryFinalUrl;

    private Map<String, String[]> queryParameters;

    private String searchId;

    private String redirectionString;
}
