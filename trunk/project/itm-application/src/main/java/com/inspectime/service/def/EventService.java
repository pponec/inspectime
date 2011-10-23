/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.service.def;

import com.inspectime.application.client.ao.EventDay;
import com.inspectime.commons.bo.Event;
import java.util.Date;
import java.util.List;

/**
 * Event Service
 * @author Ponec
 */
public interface EventService extends AbstractService<Event> {

    /** Get Properties of the Event Day */
    public EventDay getEventDayProperties(Date selectedDate);

    /** Load Projects and Accounts */
    public void loadLazy(List<Event> events);

    /** Is the day empty of is closed for a logged user ?
     * the NULL value means NO-EVENT, TRUE means closed, FALSE means opened working day
     */
    public Boolean isClosedDay(java.util.Date day);

}
