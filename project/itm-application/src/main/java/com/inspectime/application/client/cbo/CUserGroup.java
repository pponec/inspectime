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
 * CUserGroup
 * @author Ponec
 */
final public class CUserGroup extends AbstractCujo implements Serializable {

    private static final CujoPropertyList pl = list(CUserGroup.class);

    /** Primary Key */
    public static final CujoProperty<CUserGroup,Long> id = pl.newProperty("id", Long.class);

    /** Not deleted. The null value means a logical deleted state. */
    public static final CujoProperty<CUserGroup,Boolean> active = pl.newProperty("active", Boolean.class);

    /** Name */
    public static final CujoProperty<CUserGroup,String> name = pl.newProperty("name", String.class);

    /** Description */
    public static final CujoProperty<CUserGroup,String> description = pl.newProperty("description", String.class);

    @Override
    public CujoPropertyList readProperties() {
        return pl;
    }
    
    public CUserGroup() {
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
