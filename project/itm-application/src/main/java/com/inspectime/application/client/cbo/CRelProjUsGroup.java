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
 * Relation: Project-UserGroup
 * @author Ponec
 */
final public class CRelProjUsGroup extends AbstractCujo implements Serializable {

    private static final CujoPropertyList pl = list(CRelProjUsGroup.class);

    /** Primary Key */
    public static final CujoProperty<CRelProjUsGroup,Long> id = pl.newProperty("id", Long.class);

    /** Project */
    public static final CujoProperty<CRelProjUsGroup,CProject> project = pl.newProperty("project", CProject.class);

    /** User Group */
    public static final CujoProperty<CRelProjUsGroup,CUserGroup> userGroup = pl.newProperty("userGroup", CUserGroup.class);

    @Override
    public CujoPropertyList readProperties() {
        return pl;
    }
    
    // --- SETTERS / GETTERS --------------------

    // <editor-fold defaultstate="collapsed" desc="SET/GET">
    public Long getId() {
        return id.getValue(this);
    }
    // </editor-fold>

    @Override
    public String toString() {
        return String.valueOf(getId());
    }

}
