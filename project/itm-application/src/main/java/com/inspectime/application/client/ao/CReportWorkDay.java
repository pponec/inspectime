/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.ao;

import java.io.Serializable;
import org.ujorm.gxt.client.AbstractCujo;
import org.ujorm.gxt.client.CujoProperty;
import org.ujorm.gxt.client.CujoPropertyList;

/**
 *
 * @author Ponec
 */
public class CReportWorkDay extends AbstractCujo implements Serializable {


    private static final CujoPropertyList pl = list(CReportWorkDay.class);

    /** User Name */
    public static final CujoProperty<CReportWorkDay, String> user = pl.newProperty("user", String.class);

    /** Event day */
    public static final CujoProperty<CReportWorkDay, java.sql.Date> day = pl.newProperty("day", java.sql.Date.class);

    /** Arrival */
    public static final CujoProperty<CReportWorkDay, WTime> arrival = pl.newProperty("arrival", WTime.class);

    /** Departure */
    public static final CujoProperty<CReportWorkDay, WTime> departure = pl.newProperty("departure", WTime.class);

    /** Time To */
    public static final CujoProperty<CReportWorkDay, WTime> workTime = pl.newProperty("workTime", WTime.class);

    /** Private time */
    public static final CujoProperty<CReportWorkDay, WTime> privateTime = pl.newProperty("privateTime", WTime.class);

    /** Task Names */
    public static final CujoProperty<CReportWorkDay, String> taskNames = pl.newProperty("taskNames", String.class);

    /** Test the last event of the day for the private Event */
    public static final CujoProperty<CReportWorkDay, Boolean> closed = pl.newProperty("closed", Boolean.class);

    // -------------------------

    @Override
    public CujoPropertyList readProperties() {
        return pl;
    }

    /** Set the day workTime and the Private time */
    public void setDayWorkTime(int _workTime) {
        workTime.setValue(this, new WTime(_workTime));
        int result = departure.getValue(this).getMinutes()
                - arrival.getValue(this).getMinutes()
                - _workTime
                ;
        privateTime.setValue(this, new WTime(result));
    }


    /** Is the working time empty ? */
    public boolean isWorkTimeEmpty() {
        return workTime.getValue(this).isZeroSec();
    }
    
}
