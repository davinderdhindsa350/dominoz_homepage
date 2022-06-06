package com.jubl.food.nextgendiscovery.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the ordered css element.
 *
 * @author Davinder Singh
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderedCss implements Serializable, Comparable<OrderedCss> {

    /**
     * Serializable identifier.
     */
    private static final long serialVersionUID = 1449976766543614207L;

    /**
     * The rel.
     */
    private String rel;

    /**
     * The order of this css element.
     * Least order means highest privilege
     */
    private Integer order;

    /**
     * The href.
     */
    private String href;

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(final OrderedCss o) {
        return this.order.compareTo(o.order);
    }
}
