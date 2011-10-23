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

/**
 * CCompany
 * @author Ponec
 */
final public class CCompany extends AbstractCujo implements Serializable {

    private static final CujoPropertyList pl = list(CCompany.class);

    public static final CujoProperty<CCompany,Long> id = pl.newProperty("id", Long.class);

    /** Not deleted. The null value means a logical deleted state. */
    public static final CujoProperty<CCompany,Boolean> active = pl.newProperty("active", Boolean.class);

    /** CCompany Name */
    public static final CujoProperty<CCompany,String> name = pl.newProperty("name", String.class);

    /** Desccription */
    public static final CujoProperty<CCompany,String> description = pl.newProperty("description", String.class);


    @Override
    public CujoPropertyList readProperties() {
        return pl;
    }

    
    public CCompany() {
        active.setValue(this, true);
    }

    public CCompany init() {
        return this;
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
