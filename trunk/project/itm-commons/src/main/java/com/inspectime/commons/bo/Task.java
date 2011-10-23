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
import org.ujorm.UjoProperty;
import org.ujorm.core.annot.Transient;
import org.ujorm.extensions.Property;
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
    public static final Property<Task,Long> id = newProperty($ID, Long.class);

    /* Not deleted */
    @Comment("Not deleted. The null value means a logical deleted state")
    @Column(uniqueIndex=INDEX_NAME)
    public static final Property<Task, Boolean> active = newProperty($ACTIVE, Boolean.class);

    /* Is the task finished? */
    @Comment("Is the task finished?")
    @Column(mandatory=true)
    public static final Property<Task, Boolean> finished = newProperty(false);

    /** Task Code */
    @Comment("Task Code")
    @Column(mandatory=true, uniqueIndex=INDEX_NAME)
    public static final Property<Task, String> code = newProperty(String.class);

    /** Task Title */
    @Comment("Task Title")
    @Column(mandatory=true)
    public static final Property<Task, String> title = newProperty(String.class);

    /** Created for Company <br>
     * This is duplicate information intended only to ensure data consistency.
     * The company is taken from a created user property value.
     */
    @Column(mandatory=true, uniqueIndex=INDEX_NAME)
    public static final Property<Task, Company> company = newProperty($COMPANY, Company.class);

    /** Timestamp of creation */
    @Comment("Timestamp of creation")
    @Column(mandatory=true)
    public static final Property<Task, Date> created = newProperty(Date.class);

    /** Created by user */
    @Column(mandatory=true)
    public static final Property<Task, User> createdBy = newProperty(User.class);

    /** Timestamp of the last modification */
    @Column(mandatory=true)
    public static final Property<Task, Date> modified = newProperty(Date.class);

    /** Modified by user */
    @Column(mandatory=true)
    public static final Property<Task, User> modifiedBy = newProperty(User.class);

    /** Relation to Release */
    @Column(name="id_release", mandatory=!true)
    public static final Property<Task,Release> release = newProperty(Release.class);

    /** TODO: the future is the rleation via Release only ! */
    @Column(name="id_project", mandatory=true)
    public static final Property<Task,Project> project = newProperty(Project.class);

    /** Account */
    @Column(name="id_account", mandatory=true)
    public static final Property<Task,Account> account = newProperty(Account.class);

    /** Description */
    @Column(length=250)
    public static final Property<Task,String> description = newProperty(String.class);

    /** Task State Enum */
    @Column("taskState")
    public static final Property<Task,TaskStateEnum> taskState = newProperty(TaskStateEnum.NEW);

    /** Task State Enum */
    @Column("type_enum")
    public static final Property<Task,TypeEnum> type = newProperty(TypeEnum.FEATURE);

    /** Task State Enum */
    @Column("id_parent")
    public static final Property<Task,Task> parent = newProperty(Task.class);

    /** The non-commercial task */
    @Transient
    public static final Property<Task,Boolean> privateState = newProperty("private", false);

    /** The non-commercial task */
    @Transient
    public static final UjoProperty<Task,Boolean> _privateState = account.add(Account.privateState);


    /** Property initialization */
    static { init(Task.class); }
    
    public Task() {
        active.setValue(this, true);
    }

    @Override
    public Object readValue(UjoProperty property) {
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
