package com.jubl.food.nextgendiscovery.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The root html entity.
 *
 * @author Davinder Singh
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Html implements Serializable {

    /**
     * Serializable identifier.
     */
    private static final long serialVersionUID = 8837583702766209493L;

    /**
     * The html head element.
     */
    private Head head;

    /**
     * The html body element.
     */
    private Body body;


    /**
     * Sets the value of head.
     * @param headIn
     * @return The value of head to be set
     */
    public Html setHead(final Head headIn) {
        this.head = headIn;
        return this;
    }

    /**
     * Sets the value of body.
     * @param bodyIn
     * @return The value of body to be set
     */
    public Html setBody(final Body bodyIn) {
        this.body = bodyIn;
        return this;
    }
}
