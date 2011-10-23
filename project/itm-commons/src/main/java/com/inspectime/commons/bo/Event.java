/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.commons.bo;

import com.inspectime.commons.bo.item.Time;
import org.ujorm.UjoProperty;
import org.ujorm.core.annot.Transient;
import org.ujorm.extensions.Property;
import org.ujorm.orm.annot.Column;
import org.ujorm.orm.annot.Comment;
import com.inspectime.commons.bo.item.TimeZone;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * User Event
 * @author Ponec
 */
@Comment("User Event")
final public class Event extends AbstractBo {

    /** Index name */
    private static final String INDEX_NAME = "idx_event";

    /** UTC Date Index name */
    private static final String INDEX_UTC_DATE = "idx_utc_date_index";

    /** Primary Key */
    @Comment("Primary Key")
    @Column(pk = true)
    public static final Property<Event, Long> id = newProperty($ID, Long.class);

    /** Not deleted. The null value means a logical deleted state. */
    @Comment("Not deleted. The null value means a logical deleted state")
    @Column(index=INDEX_NAME)
    public static final Property<Event, Boolean> active = newProperty($ACTIVE, Boolean.class);

    /** Owner User of the Event */
    @Column(name = "id_user", mandatory = true, index=INDEX_NAME)
    public static final Property<Event, User> user = newProperty(User.class);

    /** Local Event day */
    @Column(name="day_value", mandatory=true, index=INDEX_NAME)
    public static final Property<Event, java.sql.Date> day = newProperty(java.sql.Date.class);

    /** Start time of the event */
    @Column(name="start_time", mandatory=true, index=INDEX_NAME)
    public static final Property<Event, Short> startTime = newProperty( (short) new Time("8:00").getMinutes() );

    /** Period [min] : only the last period is editable. */
    @Column(mandatory=true)
    public static final Property<Event, Short> period = newProperty((short)0);

    /** The Time Zone */
    @Column(name="time_zone", length=1, mandatory=true)
    public static final Property<Event, TimeZone> timeZone = newProperty(new TimeZone());

    /** UTC Event day and time */
    @Column(name="utc_day_time", mandatory=!true /*, index=INDEX_UTC_DATE*/) // TODO: create index (?), MANDATORY = true
    public static final Property<Event, java.util.Date> utcDayTime = newProperty(java.util.Date.class /*new java.util.Date(System.currentTimeMillis())*/);

    /** Project of tht task */
    @Transient
    public static final Property<Event, Project> project = newProperty(Project.class);

    /** Period [min] */
    @Column(mandatory=true)
    public static final Property<Event, Task> task = newProperty(Task.class);

    /** Event description */
    @Column(length = 256, mandatory = false)
    public static final Property<Event, String> description = newProperty(String.class);

    /** Project of the task */
    public static final UjoProperty<Event, Project> _project = task.add(Task.project);
    /** User's company */
    public static final UjoProperty<Event, Company> _compay = user.add(User.company);

    /** Property initialization */
    static {
        init(Event.class);
    }

    public Event() {
        active.setValue(this, true);
    }

    @Override
    public Object readValue(UjoProperty property) {
        if (property==project) {
            return _project.of(this);
        } else {
            return super.readValue(property);
        }
    }

    /** The end time */
    public int getStartTime() {
        return startTime.of(this);
    }


    /** The end time */
    public short getEndTime() {
        final int result = startTime.of(this) + period.of(this);
        return (short) result;
    }

    /** Is it the Private event? */
    public boolean isPrivate() {
        final Task _task = get(task);
        return _task!=null
            ? Task.privateState.of(_task)
            : false
            ;
    }

    /** Get User Id: */
    public Long getUserId() {
        return user.of(this).getId();
    }

    /** Set the Day and Time. */
    public void setDayTime(Date dayTime) {

        final Calendar c = GregorianCalendar.getInstance();
        c.setTime(dayTime);
        final int minutes = c.get(Calendar.MINUTE);
        final int hours = c.get(Calendar.HOUR_OF_DAY);
        final short result = (short)(minutes + hours * 60);

        startTime.setValue(this, result);
        day.setValue(this, new java.sql.Date(dayTime.getTime()));
    }

    // --- SETTERS / GETTERS --------------------
    // <editor-fold defaultstate="collapsed" desc="SET/GET">
    
    @Override
    public Long getId() {
        return id.getValue(this);
    }

    /** Returns period */
    public short getPeriod() {
        return period.getValue(this);
    }

    // </editor-fold>
}
