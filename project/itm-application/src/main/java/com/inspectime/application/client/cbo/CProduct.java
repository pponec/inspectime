/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */


package com.inspectime.application.client.cbo;

import org.ujorm.gxt.client.AbstractCujo;
import org.ujorm.gxt.client.CujoProperty;
import org.ujorm.gxt.client.CujoPropertyList;
import java.io.Serializable;
import java.util.Date;
import org.ujorm.gxt.client.tools.ColorGxt;

/**
 * CProduct
 * @author Ponec
 */
final public class CProduct extends AbstractCujo implements Serializable {

    private static final CujoPropertyList pl = list(CProduct.class);

    /** Primary Key */
    public static final CujoProperty<CProduct,Long> id = pl.newProperty("id", Long.class);

    /** Company */
    public static final CujoProperty<CProduct,CCompany> company = pl.newProperty("company", CCompany.class);

    /** Not deleted. The null value means a logical deleted state. */
    public static final CujoProperty<CProduct,Boolean> active = pl.newProperty("active", Boolean.class);

    /** Product name */
    public static final CujoProperty<CProduct,String> name = pl.newProperty("name", String.class);

    /** Product description */
    public static final CujoProperty<CProduct,String> description = pl.newProperty("description", String.class);

    /** Created timestamp */
    public static final CujoProperty<CProduct,Date> created = pl.newProperty("created", Date.class);

    /** Created by */
    public static final CujoProperty<CProduct,CUser> createdBy = pl.newProperty("createdBy", CUser.class);

    /** Modified timestamp */
    public static final CujoProperty<CProduct,Date> modified = pl.newProperty("modified", Date.class);

    /** Created by */
    public static final CujoProperty<CProduct,CUser> modifiedBy = pl.newProperty("modifiedBy", CUser.class);

    /** Graph Color */
    public static final CujoProperty<CProduct,ColorGxt> graphColor = pl.newProperty("graphColor", ColorGxt.class);

    @Override
    public CujoPropertyList readProperties() {
        return pl;
    }
    
    public CProduct() {
        Date date = new Date();
        created.setValue(this, date);
        modified.setValue(this, date);
        active.setValue(this, true);
    }

    // --- SETTERS / GETTERS --------------------

    // <editor-fold defaultstate="collapsed" desc="SET/GET">
    public Long getId() {
        return id.getValue(this);
    }
    // </editor-fold>

    @Override
    public String toString() {
        return name.getValue(this);
    }

}
