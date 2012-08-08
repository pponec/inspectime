/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.commons.bo;

import org.ujorm.Key;
import org.ujorm.core.annot.Transient;
import org.ujorm.orm.annot.Column;
import org.ujorm.orm.annot.Comment;

/**
 * Single User Parameter
 * @author Ponec
 */
@Comment("Single User Parameter")
final public class SingleUsrParam  extends AbstractBo {

    /** Unique index name */
    private static final String INDEX_NAME = "idx_usr_parameter";

    /** Primary Key */
    @Column(pk=true)
    public static final Key<SingleUsrParam,Long> id = newKey($ID);

    /** Property name */
    @Column(name="pkey", mandatory=true, length=64, uniqueIndex=INDEX_NAME)
    public static final Key<SingleUsrParam,String> key = newKey();

    /** User relation */
    @Column(mandatory=true, uniqueIndex=INDEX_NAME, name="id_user")
    public static final Key<SingleUsrParam,User> user = newKey();

    /** Property value */
    @Column(length=256, name="text_value")
    public static final Key<SingleUsrParam,String> value = newKey();

    /** Property value */
    @Transient
    public static final Key<SingleUsrParam,Integer> index = newKey();

    /** Property value */
    @Transient
    public static final Key<SingleUsrParam,String> type = newKey();

    /** Property value */
    @Transient
    public static final Key<SingleUsrParam,String> defaultValue = newKey();

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
