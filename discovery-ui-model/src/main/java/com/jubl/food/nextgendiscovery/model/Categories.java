package com.jubl.food.nextgendiscovery.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a Categories.
 *
 * @author Davinder Singh
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Categories {
    private int id;
    private String controllerName;
    private boolean hasSomeSpecialModule;
    private boolean hasWap;
    private int insertBy;
    private boolean isNew;
    private boolean isRootCategory;
    private boolean isSiteDefault;
    private String linkNavigation;
    private String linkRewrite;
    private String linkRewritePattern;
    private String parentId;
    private int publishStatus;
    private int readCount;
    private boolean requiredLogin;
    private int siteId;
    private String specialModuleAlias;
    private String wapUrl;
    private String excerpt;
    private int langId;
    private String title;
    private String name;
}
