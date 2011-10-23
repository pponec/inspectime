/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.ao;

import com.extjs.gxt.ui.client.data.PagingLoadResult;
import java.io.Serializable;
import java.util.Date;
import org.ujorm.gxt.client.Cujo;

/**
 * Event Day
 * @author pavel
 */
public class EventDay implements Serializable {

    private PagingLoadResult<Cujo> eventsResult;

    private Date currentDay;

    private boolean locked;

    private boolean dayOff;

    public Date getCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(Date currentDay) {
        this.currentDay = currentDay;
    }

    public boolean isDayOff() {
        return dayOff;
    }

    public void setDayOff(boolean dayOff) {
        this.dayOff = dayOff;
    }

    public PagingLoadResult<Cujo> getEventsResult() {
        return eventsResult;
    }

    public void setEventsResult(PagingLoadResult<Cujo> eventsResult) {
        this.eventsResult = eventsResult;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public void addProperties(EventDay temp) {
        this.currentDay = temp.currentDay;
        this.dayOff = temp.dayOff;
        this.locked = temp.locked;
    }



}
