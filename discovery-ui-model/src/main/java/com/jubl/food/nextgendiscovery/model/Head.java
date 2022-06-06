package com.jubl.food.nextgendiscovery.model;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The rendered html head part.
 *
 * @author Davinder Singh
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Head implements Serializable {

    /**
     * Serializable identifier.
     */
    private static final long serialVersionUID = 4168918595712709251L;

    private List<HtmlContent> headerContent;
    private String title;
    private HeaderMeta meta;
    private Links links;
}
