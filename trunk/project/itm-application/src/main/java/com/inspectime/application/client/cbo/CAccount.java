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
 * CAccount
 * @author Ponec
 */
final public class CAccount extends AbstractCujo implements Serializable {

    private static final CujoPropertyList pl = list(CAccount.class);

    /** Primary Key */
    public static final CujoProperty<CAccount,Long> id = pl.newProperty("id", Long.class);

    /** Not deleted. The null value means a logical deleted state. */
    public static final CujoProperty<CAccount,Boolean> active = pl.newProperty("active", Boolean.class);

    /** Business Name */
    public static final CujoProperty<CAccount,String> name = pl.newProperty("name", String.class);

    /** Description */
    public static final CujoProperty<CAccount,String> description = pl.newProperty("description", String.class);

    /** The account for a non-commercial use */
    public static final CujoProperty<CAccount,Boolean> privateState = pl.newProperty("privateState", Boolean.class);

    /** Graph Color */
    public static final CujoProperty<CAccount,ColorGxt> graphColor = pl.newProperty("graphColor", ColorGxt.class);

    @Override
    public CujoPropertyList readProperties() {
        return pl;
    }

    
    public CAccount() {
        active.setValue(this, true);
    }

    // --- SETTERS / GETTERS --------------------

    // <editor-fold defaultstate="collapsed" desc="SET/GET">
    public Long getId() {
        return id.getValue(this);
    }

    /** Graph Color */
    public ColorGxt getGraphColor() {
        return graphColor.getValue(this);
    }
    // </editor-fold>

    @Override
    public String toString() {
        return name.getValue(this);
    }

}
