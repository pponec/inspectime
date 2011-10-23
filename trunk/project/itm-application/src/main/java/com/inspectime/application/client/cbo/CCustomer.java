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
import org.ujorm.gxt.client.tools.ColorGxt;

/**
 * CCustomer
 * @author Ponec
 */
final public class CCustomer extends AbstractCujo implements Serializable {

    private static final CujoPropertyList pl = list(CCustomer.class);

    public static final CujoProperty<CCustomer,Long> id = pl.newProperty("id", Long.class);

    /** Not deleted. The null value means a logical deleted state. */
    public static final CujoProperty<CCustomer,Boolean> active = pl.newProperty("active", Boolean.class);

    /** CCustomer Name */
    public static final CujoProperty<CCustomer,String> name = pl.newProperty("name", String.class);

    /** Desccription */
    public static final CujoProperty<CCustomer,String> description = pl.newProperty("description", String.class);

    /** Graph Color */
    public static final CujoProperty<CCustomer,ColorGxt> graphColor = pl.newProperty("graphColor", ColorGxt.class);

    @Override
    public CujoPropertyList readProperties() {
        return pl;
    }

    
    public CCustomer() {
        active.setValue(this, true);
    }

    // --- SETTERS / GETTERS --------------------

    // <editor-fold defaultstate="collapsed" desc="SET/GET">
    public Long getId() {
        return id.getValue(this);
    }
    public String getName() {
        return name.getValue(this);
    }
    public ColorGxt getGraphColor() {
        return graphColor.getValue(this);
    }
    // </editor-fold>

    @Override
    public String toString() {
        return name.getValue(this);
    }

}
