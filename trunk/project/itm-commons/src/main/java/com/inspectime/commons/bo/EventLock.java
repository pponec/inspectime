/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.commons.bo;

import java.util.Date;
import org.ujorm.extensions.Property;
import org.ujorm.orm.annot.Column;
import org.ujorm.orm.annot.Comment;

/**
 * Lock for a user Events
 * @author Ponec
 */
@Comment("Lock for a user period")
final public class EventLock extends AbstractBo {

    private static final String INDEX_NAME = "idx_period_lock";

    /** Primary Key */
    @Comment("Primary Key")
    @Column(pk=true)
    public static final Property<EventLock,Long> id = newProperty($ID, Long.class);

    /** Not deleted. The null value means a logical deleted state. */
    @Comment("Not deleted. The null value means a logical deleted state")
    public static final Property<EventLock,Boolean> active = newProperty($ACTIVE, Boolean.class);

    /** Owner User of the Event */
    @Column(name = "id_user", mandatory = true, index=INDEX_NAME)
    public static final Property<EventLock, User> user = newProperty(User.class);

    /** Date of lock, the last value is relevant. Locked are all days to the required one, include. */
    @Column(mandatory=true, index = INDEX_NAME)
    public static final Property<EventLock,java.sql.Date> lockDate = newProperty(java.sql.Date.class);

    /** Timestamp of the last modification */
    @Column(mandatory=true)
    public static final Property<EventLock, Date> modified = newProperty(Date.class);

    /** Modified by user */
    @Column(mandatory=!true)
    public static final Property<EventLock, User> modifiedBy = newProperty(User.class);

    /** Property initialization */
    static { init(EventLock.class); }
    
    public EventLock() {
        active.setValue(this, true);
    }

    /** Init the object. */
    public EventLock init() {
        active.setValue(this, true);
        return this;
    }

    // --- SETTERS / GETTERS --------------------

    // <editor-fold defaultstate="collapsed" desc="SET/GET">
    @Override
    public Long getId() {
        return id.getValue(this);
    }
    // </editor-fold>

}
