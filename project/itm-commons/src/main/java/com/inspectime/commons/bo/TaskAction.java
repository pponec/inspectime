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
import org.ujorm.Key;
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
    public static final Key<TaskAction,Long> id = newKey($ID);

    /** Not deleted. The null value means a logical deleted state. */
    @Comment("Not deleted. The null value means a logical deleted state")
    public static final Key<TaskAction,Boolean> active = newKey($ACTIVE);

    /** Action descripton */
    @Column(length=1000)
    public static final Key<TaskAction,String> description = newKey();

    /** Created time stamp */
    @Column()
    public static final Key<TaskAction,Date> created = newKey();

    /** Created time stamp */
    @Column()
    public static final Key<TaskAction,Date> modified = newKey();

    /** Task State Enum */
    @Column()
    public static final Key<Task,TaskStateEnum> taskState = newKey(TaskStateEnum.NEW);

    /** Action scope enum */
    @Column()
    public static final Key<Task,ActionScopeEnum> actionScope = newKey(ActionScopeEnum.PUBLIC);

    /** User */
    @Column(name="id_user", mandatory=true)
    public static final Key<TaskAction,User> user = newKey();

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
