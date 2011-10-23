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
 * CRelease
 * @author Ponec
 */
final public class CRelease extends AbstractCujo implements Serializable {

    private static final CujoPropertyList pl = list(CRelease.class);

    /** Primary Key */
    public static final CujoProperty<CRelease,Long> id = pl.newProperty("id", Long.class);

    /** Not deleted. The null value means a logical deleted state. */
    public static final CujoProperty<CRelease,Boolean> active = pl.newProperty("active", Boolean.class);

    /** Business Name */
    public static final CujoProperty<CRelease,String> name = pl.newProperty("name", String.class);

    /** Release date */
    public static final CujoProperty<CRelease,java.util.Date> releaseDate = pl.newProperty("releaseDate", java.util.Date.class);

    /** Description */
    public static final CujoProperty<CRelease,String> description = pl.newProperty("description", String.class);

    /** Project */
    public static final CujoProperty<CRelease,CProject> project = pl.newProperty("project", CProject.class);


    @Override
    public CujoPropertyList readProperties() {
        return pl;
    }

    
    public CRelease() {
        active.setValue(this, true);
    }

    // --- SETTERS / GETTERS --------------------

    // <editor-fold defaultstate="collapsed" desc="SET/GET">
    public Long getId() {
        return id.getValue(this);
    }

    public CProject getProject() {
        return project.getValue(this);
    }

    // </editor-fold>

    @Override
    public String toString() {
        return name.getValue(this);
    }

}
