/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.ao;

import java.io.Serializable;

/**
 * DateRange data type
 * @author Pavel Ponec
 */
public enum DateRangeEnum implements Serializable {

    SELECTED_DAY,
    SELECTED_WEEK,
    SELECTED_MONTH,
    SELECTED_YEAR,
    PREVIOUS_DAY,
    PREVIOUS_WEEK,
    PREVIOUS_MONTH,
    PREVIOUS_YEAR,;

    public String getLabel(DateRangeEnum range) {
        switch (range) {
            case SELECTED_DAY: return "Current Day";
            case SELECTED_WEEK: return "Current Week";
            case SELECTED_MONTH: return "Current Month";
            case SELECTED_YEAR: return "Current Year";
            case PREVIOUS_DAY: return "Previous Day";
            case PREVIOUS_WEEK: return "Previous Week";
            case PREVIOUS_MONTH: return "Previous Month";
            case PREVIOUS_YEAR: return "Previous Year";
            default: return "Undefined day " + range;
        }
    }
}
