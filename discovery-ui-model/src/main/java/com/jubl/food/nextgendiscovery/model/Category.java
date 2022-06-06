package com.jubl.food.nextgendiscovery.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The category data.
 *
 * @author Davinder Singh
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category implements Serializable {

    /**
     * serializable identifier.
     */
    private static final long serialVersionUID = 5753389645481813281L;

    /**
     * The category id.
     */
    private Integer id;

    /**
     * The category's name.
     */
    private String name;

    /**
     * The category link rewrite.
     */
    private String linkRewrite;

    /**
     * The category link navigation.
     */
    private String linkNavigation;

    /**
     * The publish status.
     */
    private Integer publishStatus;

    /**
     * The header content.
     */
    private String headerContent;

    /**
     * The footer content.
     */
    private String footerContent;

    /**
     * The created at timestamp.
     */
    private Long createdAt;

    /**
     * The updated at timestamp.
     */
    private Long updatedAt;

    private String controllerName;

    private Integer hasSomeSpecialModule;

    private Integer hasWap;

    private Integer insertBy;

    private Integer isNew;

    private Integer isRootCategory;

    private Integer isSiteDefault;

    private String linkRewritePattern;

    private String parentId;

    private Integer readCount;

    private Integer requiredLogin;

    private Integer siteId;

    private String specialModuleAlias;

    private String wapUrl;

    private Integer platformId;

    private String cacheCategory;

    private Integer excludeInListing;

    private String icon;

    private String iconCss;

    private Integer themeId;
}
