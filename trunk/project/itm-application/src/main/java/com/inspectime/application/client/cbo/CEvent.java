/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.cbo;

import com.inspectime.application.client.ao.WTime;
import org.ujorm.gxt.client.AbstractCujo;
import org.ujorm.gxt.client.CujoProperty;
import org.ujorm.gxt.client.CujoPropertyList;
import java.io.Serializable;
import org.ujorm.gxt.client.CPathProperty;

/**
 * CEvent
 * @author Ponec
 */
final public class CEvent extends AbstractCujo implements Serializable {

    private static final CujoPropertyList pl = list(CEvent.class);

    /** Primary Key */
    public static final CujoProperty<CEvent,Long> id = pl.newProperty("id", Long.class);

    /** Not deleted. The null value means a logical deleted state. */
    public static final CujoProperty<CEvent, Boolean> active = pl.newProperty("active",  Boolean.class);

    /** Event day */
    public static final CujoProperty<CEvent, java.sql.Date> day = pl.newProperty("day", java.sql.Date.class);

    /** Event startTime of the day (the transient property) */
    public static final CujoProperty<CEvent, WTime> startTime_ = pl.newPropertyDef("startTime ", new WTime("8:00"));

    /** Period [min] : only the last period is editable (the transient property) . */
    public static final CujoProperty<CEvent, WTime> period_ = pl.newPropertyDef("period ", new WTime("0:00"));

    /** Project */
    public static final CujoProperty<CEvent, CProject> project = pl.newProperty("project", CProject.class);

    /** Task */
    public static final CujoProperty<CEvent, CTask> task = pl.newProperty("task", CTask.class);

    /** Event description */
    public static final CujoProperty<CEvent, String> description = pl.newProperty("description", String.class);

    /** Owner User of the Event */
    public static final CujoProperty<CEvent, CUser> user = pl.newProperty("user", CUser.class);

    // ----------------

    /** Event startTime of the day */
    public static final CujoProperty<CEvent, Short> startTime = pl.newPropertyDef("startTime", (short)0);

    /** Period [min] : only the last period is editable. */
    public static final CujoProperty<CEvent, Short> period = pl.newPropertyDef("period", (short)0 );

    // ---------------- COMPOSED PROPERTIES ------------------

    public static final CujoProperty<CEvent, CCustomer> _customer = task.add(CTask.project).add(CProject.customer);
    public static final CujoProperty<CEvent, CProject> _project = task.add(CTask.project);
    public static final CujoProperty<CEvent, CProduct> _product = task.add(CTask.project).add(CProject.product);
    public static final CujoProperty<CEvent, CAccount> _account = task.add(CTask.account);
    public static final CujoProperty<CEvent, CTask> _task = CPathProperty.newInstance(task);
    public static final CujoProperty<CEvent, CUser> _user = CPathProperty.newInstance(user);

    // -----------------------

    public static final CujoProperty<CEvent, String> _task_code = task.add(CTask.DISPLAY_PROPERTY);
    public static final CujoProperty<CEvent, String> _task_title = task.add(CTask.title);
    public static final CujoProperty<CEvent, String> _task_name = task.add(CTask.name);


    // =======================================================

    @Override
    public CujoPropertyList readProperties() {
        return pl;
    }

    private WTime __time__ = null;

    public CEvent() {
    }
    
    public CEvent(CEvent aParent) {
        for (CujoProperty p : readProperties()) {
            p.copy(aParent, this);
        }
    }

    /** Init values */
    public CEvent init() {
        set(active, true);
        set(day, new java.sql.Date(new java.util.Date().getTime()));
        set(startTime_, new WTime().setCurrentTime());
        set(period, period.getDefault());
        
        return this;
    }

    @Override
    public Object get(String property) {
        Object result = super.get(property);

        if (result==null && project.getName().equals(property)) {
            CTask _task = get(task);
            if (_task!=null) {
                result = _task.get(CTask.project);
            }
        }
        else if (result==null && period_.getName().equals(property)) {
            WTime time = new WTime(get(period).intValue() );
            set(property, time);
            result = time;
        }
        else if (result==null && startTime_.getName().equals(property)) {
            WTime time = new WTime((short) get(startTime) );
            set(property, time);
            result = time;
        }

        return result;
    }

    public WTime getStartTime() {
        return get(startTime_);
    }

    public WTime getPeriod() {
        return get(period_);
    }


    /** Synchronize a time values before saving */
    public void timeSync() {
        WTime time = get(startTime_);
        if (time!=null) set(startTime.getName(), (short) time.getMinutes());
        //
        set(period.getName(), null); // reset the time period

    }

    // --- SETTERS / GETTERS --------------------

    // <editor-fold defaultstate="collapsed" desc="SET/GET">
    public Long getId() {
        return id.getValue(this);
    }

    public CTask getTask() {
        return task.getValue(this);
    }

    public CUser getUser() {
        return user.getValue(this);
    }

    /** Is it a private event ? */
    public boolean isPrivate() {
        CTask _task = get(task);
        return _task!=null
                ? _task.isPrivate()
                : false
                ;
    }
    // </editor-fold>

    @Override
    public String toString() {
        return description.getValue(this);
    }

}
