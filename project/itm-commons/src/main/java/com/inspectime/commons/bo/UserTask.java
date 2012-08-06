/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.commons.bo;

import org.ujorm.Key;
import org.ujorm.core.annot.Transient;
import org.ujorm.extensions.Property;
import org.ujorm.orm.annot.Column;
import org.ujorm.orm.annot.Comment;

/**
 * User Task
 * @author Ponec
 */
@Comment("User Task")
final public class UserTask extends AbstractBo {

    /** Index name */
    private static final String INDEX_NAME = "idx_usr_task";

    /** Primary Key */
    @Column(pk = true)
    public static final Property<UserTask, Long> id = newProperty($ID, Long.class);

    /** Owner User of the Event */
    @Column(name = "id_user", mandatory = true, uniqueIndex=INDEX_NAME)
    public static final Property<UserTask, User> user = newProperty(User.class);

    /** Task */
    @Column(mandatory=true, uniqueIndex=INDEX_NAME)
    public static final Property<UserTask, Task> task = newProperty(Task.class);

    /** Order */
    @Column(name = "task_order", mandatory=false)
    public static final Property<UserTask, Short> order = newProperty(Short.class);

    /** Name (Task code) [transient] */
    @Transient
    public static final Property<UserTask, String> name = newProperty(String.class);

    /** Descripton (title + project name) [transient] */
    @Transient
    public static final Property<UserTask, String> description = newProperty(String.class);

    /** Project name (title + project name) [transient] */
    @Transient
    public static final Property<UserTask, String> projectName = newProperty(String.class);

    /** Task Code */
    @Transient
    public static final Key<UserTask, String> _taskCode = task.add(Task.code);

    /** Task Code */
    @Transient
    public static final Key<UserTask, String> _taskTitle = task.add(Task.title);

    /** Project name */
    @Transient
    public static final Key<UserTask, String> _projectName = task.add(Task.project).add(Project.name);

    /** Property initialization */
    static {
        init(UserTask.class);
        //ArrayIndexOutOfBoundsException.class;
    }

    public UserTask() {
    }

    @Override
    public Object readValue(Key property) {
        if (property == name) {
            return _taskCode.of(this);
        } else if (property == projectName) {
            return _projectName.of(this);
        } else {
            return super.readValue(property);
        }
    }

    // --- SETTERS / GETTERS --------------------
    
    // <editor-fold defaultstate="collapsed" desc="SET/GET">
    
    @Override
    public Long getId() {
        return id.getValue(this);
    }

    public Task getTask() {
        return task.getValue(this);
    }
    
    public User getUser() {
        return user.getValue(this);
    }

    // </editor-fold>
}
