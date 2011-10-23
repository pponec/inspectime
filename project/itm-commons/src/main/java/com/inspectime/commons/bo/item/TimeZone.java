/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.commons.bo.item;

import java.io.Serializable;
import org.ujorm.extensions.UjoCloneable;
import org.ujorm.extensions.ValueExportable;

/**
 * Immutable TimeZone designed for the JWS.
 * @author Pavel Ponec
 */
final public class TimeZone implements Comparable, ValueExportable, UjoCloneable, Serializable {

    /** The zero zone */
    static final private char ZERO_ZONE = 'A';
    /** HOURSThe zero zone */
    static final private int HOURS_PER_DAY = 24;
    /** Time Zone where same values means:
     * <ul>
     *    <li>A : 0 hourses (zero zone)</li>
     *    <li>B : +1 hourses</li>
     *    <li>C : +2 hourses</li>
     *    <li>Y : -1 hourses</li>
     * </ul>
     * TODO: zimní / letní čas 
     */
    final private char zone; // Zero zone

    /**
     * Creates a new instance of TimeZone
     * @param timeZone Format 'A'
     */
    public TimeZone() {
        this(ZERO_ZONE);
    }

    /**
     * Creates a new instance of TimeZone
     * @param timeZone Range from -11 to 12
     */
    public TimeZone(int timeZone) {
        this.zone = (char) (ZERO_ZONE + (timeZone % HOURS_PER_DAY));
    }

    /**
     * Creates a new instance of TimeZone
     * @param timeZone Format 'A'
     */
    public TimeZone(String timeZone) {
        if (timeZone != null && timeZone.length() > 0) {
            final char c = timeZone.charAt(0);
            final int zone = (c - ZERO_ZONE) % HOURS_PER_DAY;
            this.zone = (char) (ZERO_ZONE + zone);
        } else {
            this.zone = ZERO_ZONE;
        }
    }

    /**
     * Creates a new instance of TimeZone
     * @param timeZone Format 'A'
     */
    private TimeZone(char zone) {
        this.zone = zone;
    }

    /** Returns a clone */
    @Override
    public Object clone(int depth, final Object context) {
        return new TimeZone(zone);
    }

    @Override
    public int compareTo(Object o) {
        final TimeZone p = (TimeZone) o;

        if (zone == p.zone) {
            return 0;
        } else {
            return zone < p.zone ? -1 : 1;
        }
    }

    /** Returns zone in the [hourse] unit. */
    public int getHourseZone() {
        final int result = zone - ZERO_ZONE;
        return result > 12 ? (result - HOURS_PER_DAY) : result;
    }

    /** Returns an instance of object java.util.TimeZone */
    public java.util.TimeZone getUtilTimeZone() {
        int hourseZone = getHourseZone();

        if (hourseZone < 0) {
            return java.util.TimeZone.getTimeZone("GMT" + hourseZone + ":00");
        } else {
            return java.util.TimeZone.getTimeZone("GMT+" + hourseZone + ":00");
        }
    }

    /** Print Time Zone */
    @Override
    public String toString() {
        return getUtilTimeZone().getID();
    }

    /** Value to database store */
    @Override
    public String exportToString() {
        return String.valueOf(zone);
    }
}
