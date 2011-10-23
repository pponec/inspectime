/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.cbo;

import com.inspectime.application.client.service.CParam4Company;
import org.ujorm.gxt.client.AbstractCujo;
import org.ujorm.gxt.client.CujoProperty;
import org.ujorm.gxt.client.CujoPropertyList;
import java.io.Serializable;

/**
 * Parameter table row of the Company
 * @author meatfly, Ponec
 **/
public class CSingleComParam extends AbstractCujo implements Serializable {

    private static final CujoPropertyList pl = list(CSingleComParam.class);
    /** index */
    public static final CujoProperty<CSingleComParam, Integer> index = pl.newProperty("index", Integer.class);
    /** key */
    public static final CujoProperty<CSingleComParam, String> key = pl.newProperty("key", String.class);
    /** type */
    public static final CujoProperty<CSingleComParam, String> type = pl.newProperty("type", String.class);
    /** defaultValue */
    public static final CujoProperty<CSingleComParam, String> defaultValue = pl.newProperty("defaultValue", String.class);
    /** value */
    public static final CujoProperty<CSingleComParam, String> value = pl.newProperty("value", String.class);
    /** Parameter description */
    public static final CujoProperty<CSingleComParam, String> description = pl.newProperty("description", String.class);
        
    @Override
    public CujoPropertyList readProperties() {
        return pl;
    }

    public CSingleComParam() {
    }

    @Override
    public <X> X get(String aKey) {
        if (description.getName().equals(aKey)) {
            String paramKey = super.get(key.getName());
            return (X) CParam4Company.getInstance().getDescription(paramKey);
        } else {
            return (X) super.get(aKey);
        }
    }


    /** Return value in a text format */
    public String getTextValue() {
        return value.getValue(this);
    }

}
