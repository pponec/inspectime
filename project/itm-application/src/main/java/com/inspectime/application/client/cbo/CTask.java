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
 * Clinet Projects Component
 * @author Ponec
 */
final public class CTask extends AbstractCujo implements Serializable {

    private static final CujoPropertyList pl = list(CTask.class);

    /** Primary Key */
    public static final CujoProperty<CTask,Long> id = pl.newProperty("id", Long.class);

    /** Not deleted */
    public static final CujoProperty<CTask, Boolean> active = pl.newProperty("active", Boolean.class);

    /** Task Code */
    public static final CujoProperty<CTask, String> code = pl.newProperty("code", String.class);

    /** Task title */
    public static final CujoProperty<CTask, String> title = pl.newProperty("title", String.class);

    /** Company */
    public static final CujoProperty<CTask,CCompany> company = pl.newProperty("company", CCompany.class);

    /** Created timestamp */
    public static final CujoProperty<CTask,Date> created = pl.newProperty("created", Date.class);

    /** Created by */
    public static final CujoProperty<CTask,CUser> createdBy = pl.newProperty("createdBy", CUser.class);

    /** Modified timestamp */
    public static final CujoProperty<CTask,Date> modified = pl.newProperty("modified", Date.class);

    /** Created by */
    public static final CujoProperty<CTask,CUser> modifiedBy = pl.newProperty("modifiedBy", CUser.class);

    /** Relation to Release */
    public static final CujoProperty<CTask,CRelease> release = pl.newProperty("release", CRelease.class);

    /** TODO: the future is the rleation via Release only ! */
    public static final CujoProperty<CTask,CProject> project = pl.newProperty("project", CProject.class);

    /** Account */
    public static final CujoProperty<CTask,CAccount> account = pl.newProperty("account", CAccount.class);

    /** Relation to Environment */
    public static final CujoProperty<CTask,String> description = pl.newPropertyDef("description", "");

    /** Task state Enum */
    public static final CujoProperty<CTask,CEnum> taskState = pl.newPropertyEnum("taskState", "TaskStateEnum");

    /** Task state Enum */
    public static final CujoProperty<CTask,CEnum> type = pl.newPropertyEnum("type", "TypeEnum");

    /** Relation to Release */
    public static final CujoProperty<CTask,CTask> parent = pl.newProperty("parent", CTask.class);

    /** Not deleted */
    public static final CujoProperty<CTask, Boolean> finished = pl.newProperty("finished", Boolean.class);

    /** The non-commercial task */
    public static final CujoProperty<CTask, Boolean> privateState = pl.newProperty("private", Boolean.class);

    /** Transient Name (code + title) */
    public static final CujoProperty<CTask, String> name = pl.newProperty("name", String.class);

    /** Property to DISPLAY (in a combo-box for example) */
    public static final CujoProperty<CTask, String> DISPLAY_PROPERTY = title;

    @Override
    public CujoPropertyList readProperties() {
        return pl;
    }
    
    public CTask() {
        Date date = new Date();
        created.setValue(this, date);
        modified.setValue(this, date);
        taskState.setValue(this, new CEnum(0, "NEW"));
        type.setValue(this, new CEnum(0, "FEATURE"));
        set(active, true);
        set(finished, false);
    }

    @Override
    public Object get(String property) {
        if (property.equals(name.toString())) {
            return getName();
        }
        return super.get(property);
    }


    // --- SETTERS / GETTERS --------------------

    // <editor-fold defaultstate="collapsed" desc="SET/GET">
    @Override
    public Long getId() {
        return id.getValue(this);
    }

    public CRelease getRelease() {
        return release.getValue(this);
    }

    public String getCode() {
        return code.getValue(this);
    }

    public String getTitle() {
        return title.getValue(this);
    }

    public String getDescription() {
        return description.getValue(this);
    }

    /** Has the Task a Private state ? */
    public boolean isPrivate() {
        return privateState.getValue(this);
    }
    // </editor-fold>

    @Override
    public String toString() {
        return get(code);
    }

    /** CODE + TITLE */
    public String getName() {
        return "[" + code.getValue(this) + "] " + title.getValue(this);
    }

}
