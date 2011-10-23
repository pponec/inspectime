/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.gui.report;

import java.io.Serializable;

/**
 * ReportChartItem
 * @author Pavel Ponec
 */
public class ReportChartItem implements Serializable {

    private Long id;
    private int value = 0;

    public ReportChartItem() {
    }

    public ReportChartItem(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    /** GetPeriod */
    public int getPeriod() {
        return value;
    }

    /** Add nwe period */
    public void addValue(int time) {
        this.value += time;
    }
}
