package com.jubl.food.nextgendiscovery.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Ordered js entity.
 *
 * @author Davinder Singh
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderedJs implements Serializable, Comparable<OrderedJs> {

    /**
     * Serializable identifier.
     */
    private static final long serialVersionUID = 7566409711003911139L;

    /**
     * The order of this js element.
     * Least order means highest privilege
     */
    private Integer order;

    /**
     * The source of script.
     */
    private String src;

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(final OrderedJs o) {
        return this.order.compareTo(o.order);
    }
}
