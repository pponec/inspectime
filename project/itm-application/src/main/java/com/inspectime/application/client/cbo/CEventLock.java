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

/**
 * EventLock
 * @author Ponec
 */
final public class CEventLock extends AbstractCujo implements Serializable {

    private static final CujoPropertyList pl = list(CEventLock.class);

    /** Primary Key */
    public static final CujoProperty<CEventLock,Long> id = pl.newProperty("id", Long.class);

    /** Not deleted. The null value means a logical deleted state. */
    public static final CujoProperty<CEventLock,Boolean> active = pl.newProperty("active", Boolean.class);

    /** User name */
    public static final CujoProperty<CEventLock,CUser> user = pl.newProperty("user", CUser.class);

    /** Date of lock, the last value is relevant. Locked are all days to the required one, include. */
    public static final CujoProperty<CEventLock,java.sql.Date> lockDate = pl.newProperty("lockDate", java.sql.Date.class);

    /** Last modified */
    public static final CujoProperty<CEventLock,Date> modified = pl.newProperty("modified", Date.class);

    /** Last modified */
    public static final CujoProperty<CEventLock,CUser> modifiedBy = pl.newProperty("modifiedBy", CUser.class);

    @Override
    public CujoPropertyList readProperties() {
        return pl;
    }

    
    public CEventLock() {
        active.setValue(this, true);
    }

    // --- SETTERS / GETTERS --------------------

    // <editor-fold defaultstate="collapsed" desc="SET/GET">
    public Long getId() {
        return id.getValue(this);
    }

    /** Graph Color */
    public java.sql.Date getLockDate() {
        return lockDate.getValue(this);
    }
    // </editor-fold>


}
