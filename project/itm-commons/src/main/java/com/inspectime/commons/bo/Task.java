/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.commons.bo;

import com.inspectime.commons.bo.enums.TaskStateEnum;
import com.inspectime.commons.bo.enums.TypeEnum;
import java.util.Date;
import org.ujorm.Key;
import org.ujorm.core.annot.Transient;
import org.ujorm.orm.annot.Column;
import org.ujorm.orm.annot.Comment;

/**
 * Project Task is unique for properties $ACTIVE, CODE, COMPANY
 * @author Ponec
 */
@Comment("Project Task is unique for properties $ACTIVE, CODE, COMPANY")
final public class Task extends AbstractBo {

    private static final String INDEX_NAME = "idx_task";

    /** Primary Key */
    @Column(pk=true)
    public static final Key<Task,Long> id = newKey($ID);

    /* Not deleted */
    @Comment("Not deleted. The null value means a logical deleted state")
    @Column(uniqueIndex=INDEX_NAME)
    public static final Key<Task, Boolean> active = newKey($ACTIVE);

    /* Is the task finished? */
    @Comment("Is the task finished?")
    @Column(mandatory=true)
    public static final Key<Task, Boolean> finished = newKey(false);

    /** Task Code */
    @Comment("Task Code")
    @Column(mandatory=true, uniqueIndex=INDEX_NAME)
    public static final Key<Task, String> code = newKey();

    /** Task Title */
    @Comment("Task Title")
    @Column(mandatory=true)
    public static final Key<Task, String> title = newKey();

    /** Created for Company <br>
     * This is duplicate information intended only to ensure data consistency.
     * The company is taken from a created user property value.
     */
    @Column(mandatory=true, uniqueIndex=INDEX_NAME)
    public static final Key<Task, Company> company = newKey($COMPANY);

    /** Timestamp of creation */
    @Comment("Timestamp of creation")
    @Column(mandatory=true)
    public static final Key<Task, Date> created = newKey();

    /** Created by user */
    @Column(mandatory=true)
    public static final Key<Task, User> createdBy = newKey();

    /** Timestamp of the last modification */
    @Column(mandatory=true)
    public static final Key<Task, Date> modified = newKey();

    /** Modified by user */
    @Column(mandatory=true)
    public static final Key<Task, User> modifiedBy = newKey();

    /** Relation to Release */
    @Column(name="id_release", mandatory=!true)
    public static final Key<Task,Release> release = newKey();

    /** TODO: the future is the rleation via Release only ! */
    @Column(name="id_project", mandatory=true)
    public static final Key<Task,Project> project = newKey();

    /** Account */
    @Column(name="id_account", mandatory=true)
    public static final Key<Task,Account> account = newKey();

    /** Description */
    @Column(length=250)
    public static final Key<Task,String> description = newKey();

    /** Task State Enum */
    @Column("taskState")
    public static final Key<Task,TaskStateEnum> taskState = newKey(TaskStateEnum.NEW);

    /** Task State Enum */
    @Column("type_enum")
    public static final Key<Task,TypeEnum> type = newKey(TypeEnum.FEATURE);

    /** Task State Enum */
    @Column("id_parent")
    public static final Key<Task,Task> parent = newKey();

    /** The non-commercial task */
    @Transient
    public static final Key<Task,Boolean> privateState = newKey("private", false);

    /** The non-commercial task */
    @Transient
    public static final Key<Task,Boolean> _privateState = account.add(Account.privateState);


    /** Property initialization */
    static { init(Task.class); }
    
    public Task() {
        active.setValue(this, true);
    }

    @Override
    public Object readValue(Key property) {
        if (property==privateState) {
            return _privateState.of(this);
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
    // </editor-fold>

}
