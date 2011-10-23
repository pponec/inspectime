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
import org.ujorm.gxt.client.CEnum;

/**
 * CRelease
 * @author Ponec
 */
final public class CTaskAction extends AbstractCujo implements Serializable {

    private static final CujoPropertyList pl = list(CTaskAction.class);

    /** Primary Key */
    public static final CujoProperty<CTaskAction,Long> id = pl.newProperty("id", Long.class);

    /** Not deleted. The null value means a logical deleted state. */
    public static final CujoProperty<CTaskAction,Boolean> active = pl.newProperty("active", Boolean.class);

    /** Description */
    public static final CujoProperty<CTaskAction,String> description = pl.newProperty("description", String.class);

    /** Created time stamp */
    public static final CujoProperty<CTaskAction,Date> created = pl.newProperty("created", Date.class);

    /** Created time stamp */
    public static final CujoProperty<CTaskAction,Date> modified = pl.newProperty("modified", Date.class);

    /** User */
    public static final CujoProperty<CTaskAction,CUser> user = pl.newProperty("user", CUser.class);

    /** Task state Enum */
    public static final CujoProperty<CTaskAction,CEnum> taskState = pl.newPropertyEnum("taskState", "TaskStateEnum");

    /** Task state Enum */
    public static final CujoProperty<CTaskAction,CEnum> actionScope = pl.newPropertyEnum("actionScope", "ActionScopeEnum");


    @Override
    public CujoPropertyList readProperties() {
        return pl;
    }

    
    public CTaskAction() {
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
        return description.getValue(this);
    }

}
