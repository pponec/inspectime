/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */


package com.inspectime.application.client.gui.event;

import com.inspectime.application.client.cbo.CEvent;
import org.ujorm.gxt.client.tools.ColorGxt;

/**
 *
 * @author pavel
 */
public class ChartItem {

    private Long id;
    private String name;
    private String color;
    private long value = 0L;
    private boolean calcByTime = true;

    public ChartItem(Long id, String name, ColorGxt color, boolean calcByTime) {
        this.id = id;
        this.name = name;
        this.color = "#" + color.toString();
        this.calcByTime = calcByTime;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    /** Get Color */
    public String getColor() {
        return color;
    }

    /** GetPeriod */
    public long getPeriod() {
        return value;
    }

    /** Add nwe period */
    public void addValue(CEvent event) {
        short value = calcByTime ? event.getPeriod().getTimeMinutes() : 1;
        this.value += value;
    }

}
