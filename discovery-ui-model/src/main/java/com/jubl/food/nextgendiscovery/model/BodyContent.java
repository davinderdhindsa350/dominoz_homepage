package com.jubl.food.nextgendiscovery.model;

import java.io.Serializable;
import java.util.Map;

import com.jubl.food.nextgendiscovery.model.domain.meta.theme.Hook;
import com.jubl.food.nextgendiscovery.model.domain.meta.theme.Module;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The body content.
 *
 * @author Davinder Singh
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BodyContent implements Serializable {

    /**
     * Serializable identifier.
     */
    private static final long serialVersionUID = 5682594001867014066L;

    /**
     * The html skeleton.
     */
    private String skeleton;

    /**
     * The hooks inside body.
     */
    private Map<String, Hook> hooks;

    /**
     * The modules inside body.
     */
    private Map<String, Module> modules;

}
