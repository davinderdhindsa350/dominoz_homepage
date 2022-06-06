package com.jubl.food.nextgendiscovery.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The html content.
 *
 * @author Davinder Singh
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HtmlContent implements Serializable, Comparable<HtmlContent> {

    /**
     * Serializable identifier.
     */
    private static final long serialVersionUID = 891580324504204808L;

    /**
     * The order of this html content.
     * lower value means highest order
     */
    private Integer order;

    /**
     * The actual html.
     */
    private String html;

    /**
     * Compare this object with the specified object.
     *
     * @param content the specified object
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(final HtmlContent content) {
        return this.order.compareTo(content.order);
    }
}
