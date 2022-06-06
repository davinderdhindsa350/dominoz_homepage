package com.jubl.food.nextgendiscovery.model.domain.meta.theme;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents theme hook data.
 *
 * @author Davinder Singh
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Hook implements Serializable {

    /**
     * Serializable identifier.
     */
    private static final long serialVersionUID = 7054261954939990185L;

    /**
     * The hook name.
     */
    private String name;

    /**
     * The hook alias.
     */
    private String alias;

    /**
     * The hook description.
     */
    private String description;

    /**
     * The hook placeholder.
     */
    private String placeholder;

    /**
     * List of all modules.
     */
    private List<Module> modules;

    /**
     * Sets the value of name.
     * @param nameIn
     * @return The value of name to be set
     */
    public Hook setName(final String nameIn) {
        this.name = nameIn;
        return this;
    }

    /**
     * Sets the value of alias.
     * @param aliasIn
     * @return The value of alias to be set
     */
    public Hook setAlias(final String aliasIn) {
        this.alias = aliasIn;
        return this;
    }

    /**
     * Sets the value of description.
     * @param descriptionIn
     * @return The value of description to be set
     */
    public Hook setDescription(final String descriptionIn) {
        this.description = descriptionIn;
        return this;
    }

    /**
     * Sets the value of placeholder.
     * @param placeholderIn
     * @return The value of placeholder to be set
     */
    public Hook setPlaceholder(final String placeholderIn) {
        this.placeholder = placeholderIn;
        return this;
    }

    /**
     * Sets the value of modules.
     * @param modulesIn
     */
    public void setModules(final List<Module> modulesIn) {
        this.modules = modulesIn;
    }
}
