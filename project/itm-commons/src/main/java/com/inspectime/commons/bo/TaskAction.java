/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.commons.bo;

import com.inspectime.commons.bo.enums.ActionScopeEnum;
import com.inspectime.commons.bo.enums.TaskStateEnum;
import java.util.Date;
import org.ujorm.extensions.Property;
import org.ujorm.orm.annot.Column;
import org.ujorm.orm.annot.Comment;

/**
 * The Action of the task
 * @author Ponec
 */
@Comment("The Action of the task")
final public class TaskAction extends AbstractBo {

    /** Primary Key */
    @Column(pk=true)
    public static final Property<TaskAction,Long> id = newProperty($ID, Long.class);

    /** Not deleted. The null value means a logical deleted state. */
    @Comment("Not deleted. The null value means a logical deleted state")
    public static final Property<TaskAction,Boolean> active = newProperty($ACTIVE, Boolean.class);

    /** Action descripton */
    @Column(length=1000)
    public static final Property<TaskAction,String> description = newProperty(String.class);

    /** Created time stamp */
    @Column()
    public static final Property<TaskAction,Date> created = newProperty(Date.class);

    /** Created time stamp */
    @Column()
    public static final Property<TaskAction,Date> modified = newProperty(Date.class);

    /** Task State Enum */
    @Column()
    public static final Property<Task,TaskStateEnum> taskState = newProperty(TaskStateEnum.NEW);

    /** Action scope enum */
    @Column()
    public static final Property<Task,ActionScopeEnum> actionScope = newProperty(ActionScopeEnum.PUBLIC);

    /** User */
    @Column(name="id_user", mandatory=true)
    public static final Property<TaskAction,User> user = newProperty(User.class);

    static { init(TaskAction.class); }
    
    public TaskAction() {
        active.setValue(this, true);
        created.setValue(this, new Date());
    }

    // --- SETTERS / GETTERS --------------------

    // <editor-fold defaultstate="collapsed" desc="SET/GET">
    @Override
    public Long getId() {
        return id.getValue(this);
    }
    // </editor-fold>

}
