package com.jubl.food.nextgendiscovery.model.domain.meta.theme;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a parsed theme module data.
 *
 * @author Davinder Singh
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Module implements Serializable, Comparable<Module> {

    /**
     * Serializable identifier.
     */
    private static final long serialVersionUID = 2686739608980118374L;

    /**
     * The place holder.
     */
    private String placeholder;

    /**
     * The module name.
     */
    private String name;

    /**
     * The module alias.
     */
    private String alias;

    /**
     * The view name.
     */
    private String viewName;

    /**
     * Data type.
     */
    private String dataType;

    /**
     * The actual module data.
     */
    private Object data;

    /**
     * query data.
     */
    private Object queryData;

    /**
     * The module position, necessary if a module is in a parent hook.
     */
    private Integer position;

    private String individualCache;

    private Object moduleProps;

    /**
     * Sets the value of placeholder.
     * @param placeholderIn
     * @return The value of placeholder to be set
     */
    public Module setPlaceholder(final String placeholderIn) {
        this.placeholder = placeholderIn;
        return this;
    }

    /**
     * Sets the value of name.
     * @param nameIn
     * @return The value of name to be set
     */
    public Module setName(final String nameIn) {
        this.name = nameIn;
        return this;
    }

    /**
     * Sets the value of alias.
     * @param aliasIn
     * @return The value of alias to be set
     */
    public Module setAlias(final String aliasIn) {
        this.alias = aliasIn;
        return this;
    }

    /**
     * Sets the value of viewName.
     * @param viewNameIn
     * @return The value of viewName to be set
     */
    public Module setViewName(final String viewNameIn) {
        this.viewName = viewNameIn;
        return this;
    }

    /**
     * Sets the value of moduleProps.
     * @param modulePropsIn
     * @return The value of moduleProps to be set
     */
    public Module setModuleProps(final Object modulePropsIn) {
        this.moduleProps = modulePropsIn;
        return this;
    }

    /**
     * Sets the value of dataType.
     * @param dataTypeIn
     * @return The value of dataType to be set
     */
    public Module setDataType(final String dataTypeIn) {
        this.dataType = dataTypeIn;
        return this;
    }

    /**
     * Sets the value of data.
     * @param dataIn
     * @return The value of data to be set
     */
    public Module setData(final Object dataIn) {
        this.data = dataIn;
        return this;
    }

    /**
     * Sets the value of queryData.
     * @param queryDataIn
     * @return The value of queryData to be set
     */
    public Module setQueryData(final Object queryDataIn) {
        this.queryData = queryDataIn;
        return this;
    }

    /**
     * Sets the value of position.
     * @param positionIn
     * @return The value of position to be set
     */
    public Module setPosition(final Integer positionIn) {
        this.position = positionIn;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(final Module o) {
        return this.position.compareTo(o.position);
    }
}
