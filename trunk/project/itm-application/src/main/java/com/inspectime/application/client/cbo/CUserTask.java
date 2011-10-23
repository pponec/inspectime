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
 * User Task
 * @author Ponec
 */
final public class CUserTask extends AbstractCujo implements Serializable {

    private static final CujoPropertyList pl = list(CUserTask.class);

    /** Primary Key */
    public static final CujoProperty<CUserTask,Long> id = pl.newProperty("id", Long.class);

    /** Short */
    public static final CujoProperty<CUserTask, Short> order = pl.newPropertyDef("order",  (short) 10 );

    /** Name (Task Code)  [transient] */
    public static final CujoProperty<CUserTask, String> name = pl.newProperty("name", String.class);

    /** Description (Task Code)  [transient] */
    public static final CujoProperty<CUserTask, String> description = pl.newProperty("description", String.class);

    /** Description (Task Code)  [transient] */
    public static final CujoProperty<CUserTask, String> projectName = pl.newProperty("projectName", String.class);

    /** Task */
    public static final CujoProperty<CUserTask, CTask> task = pl.newProperty("task", CTask.class);

    /** Owner User of the Event */
    public static final CujoProperty<CUserTask, CUser> user = pl.newProperty("user", CUser.class);

    /** Owner User of the Event */
    public static final CujoProperty<CUserTask, Boolean> privateState = pl.newProperty("private", Boolean.class);

    /** Private task */
    public static final CujoProperty<CUserTask, Boolean> _taskPrivate = task.add(CTask.privateState);

    /** Descripton of the task */
    public static final CujoProperty<CUserTask, String> _taskDescription = task.add(CTask.description);

    /** Is the task finished  */
    public static final CujoProperty<CUserTask, Boolean> _isFinished = task.add(CTask.finished);

    // ----------------

    @Override
    public CujoPropertyList readProperties() {
        return pl;
    }

    public CUserTask() {
    }
    
    /** Init values */
    public CUserTask init() {
        set(order, order.getDefault());
        return this;
    }

    @Override
    public Object get(String property) {
        if (property==description.getName()) {
            return _taskDescription.getValue(this);
        } 
        else if (property == privateState.getName()) {
            return _taskPrivate.getValue(this);
        }
        else {
            return super.get(property);
        }
    }

    // --- SETTERS / GETTERS --------------------

    // <editor-fold defaultstate="collapsed" desc="SET/GET">
    @Override
    public Long getId() {
        return id.getValue(this);
    }

    public CTask getTask() {
        return task.getValue(this);
    }

    public CUser getUser() {
        return user.getValue(this);
    }

    public String getName() {
        return name.getValue(this);
    }
    
    public String getDescription() {
        return description.getValue(this);
    }

    public String getProjectName() {
        return projectName.getValue(this);
    }
    // </editor-fold>

    @Override
    public String toString() {
        return name.getValue(this);
    }

}
