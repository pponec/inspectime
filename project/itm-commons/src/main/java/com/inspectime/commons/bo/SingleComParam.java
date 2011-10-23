/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.commons.bo;

import org.ujorm.core.annot.Transient;
import org.ujorm.extensions.Property;
import org.ujorm.orm.annot.Column;
import org.ujorm.orm.annot.Comment;

/**
 * Single Company Parameter
 * @author Ponec
 */
@Comment("Single Company Parameter")
final public class SingleComParam  extends AbstractBo {

    /** Unique index name */
    private static final String INDEX_NAME = "idx_parameter";

    /** Primary Key */
    @Column(pk=true)
    public static final Property<SingleComParam,Long> id = newProperty($ID, Long.class);

    /** Property name */
    @Column(name="pkey", mandatory=true, length=64, uniqueIndex=INDEX_NAME)
    public static final Property<SingleComParam,String> key = newProperty(String.class);

    /** Company relation */
    @Column(mandatory=true, uniqueIndex=INDEX_NAME)
    public static final Property<SingleComParam,Company> company = newProperty($COMPANY, Company.class);

    /** Property value */
    @Column(length=256, name="text_value")
    public static final Property<SingleComParam,String> value = newProperty(String.class);

    /** Property value */
    @Transient
    public static final Property<SingleComParam,Integer> index = newProperty(Integer.class);

    /** Property value */
    @Transient
    public static final Property<SingleComParam,String> type = newProperty(String.class);

    /** Property value */
    @Transient
    public static final Property<SingleComParam,String> defaultValue = newProperty(String.class);

    // <editor-fold defaultstate="collapsed" desc="SET/GET">

    @Override
    public Long getId() {
        return null;
    }

    public String getKey() {
        return key.getValue(this);
    }

    public void setKey(String aKey) {
        key.setValue(this, aKey);
    }

    public String getValue() {
        return value.getValue(this);
    }

    public void setValue(String aValue) {
        value.setValue(this, aValue);
    }
// </editor-fold>

    /** A key for I18N of the parameter description */
    public String getLokatorKey() {
        return "param." + getKey();
    }

}
