package com.jubl.food.nextgendiscovery.model.domain;

import java.io.Serializable;

import com.jubl.food.nextgendiscovery.model.Html;
import com.jubl.food.nextgendiscovery.model.Meta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The content of a page.
 *
 * @author Davinder Singh
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageContent implements Serializable {

    /**
     * Serializable identiifer.
     */
    private static final long serialVersionUID = 5496207103467490800L;

    /**
     * The site html data.
     */
    private Html html;

    /**
     * The site meta data.
     * Meta data contains data for theme, category, platform, lang, modules, hooks etc
     */
    private Meta meta;
}
