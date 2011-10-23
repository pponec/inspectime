/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.ao;

import java.util.ArrayList;
import java.util.List;
import org.ujorm.gxt.client.CEnum;

/**
 * Immutable TimeZone designed for the JWS.
 * @author Pavel Ponec
 */
final public class CTimeZone extends CEnum {

    /** The zero zone */
    static final private char ZERO_ZONE = 'A';
    /** HOURSThe zero zone */
    static final private int HOURS_PER_DAY = 24;
    /** Time Zone where same values means:
     * <ul>
     *    <li>A : 0 hourses (zero zone)</li>
     *    <li>B : +1 hourses</li>
     *    <li>C : +2 hourses</li>
     *    <li>X : -2 hourses</li>
     *    <li>Y : -1 hourses</li>
     * </ul>
     * TODO: zimní / letní čas 
     */
    private char zone; // Zero zone

    /**
     * Creates a new instance of TimeZone
     */
    public CTimeZone() {
        this(ZERO_ZONE);
        initProperties();
    }

    /**
     * Creates a new instance of TimeZone
     * @param timeZone Range from -11 to 12
     */
    public CTimeZone(int timeZone) {
        this.zone = (char) (ZERO_ZONE + (timeZone % HOURS_PER_DAY));
        initProperties();
    }

    /**
     * Creates a new instance of TimeZone
     * @param timeZone Format 'A'
     */
    public CTimeZone(String timeZone) {
        if (timeZone != null && timeZone.length() > 0) {
            final char c = timeZone.charAt(0);
            final int zone = (c - ZERO_ZONE) % HOURS_PER_DAY;
            this.zone = (char) (ZERO_ZONE + zone);
        } else {
            this.zone = ZERO_ZONE;
        }
        initProperties();
    }

    private void initProperties() {
        id.setValue(this, (int) this.zone);
        name.setValue(this, toString());
    }

    /**
     * Creates a new instance of TimeZone
     * @param timeZone Format 'A'
     */
    private CTimeZone(char zone) {
        this.zone = zone;
    }

    /** Returns zone in the [hourse] unit. */
    public int getHourseZone() {
        final int result = zone - ZERO_ZONE;
        return result > 12 ? (result - HOURS_PER_DAY) : result;
    }

    /** Print Time Zone */
    @Override
    public String toString() {
        int hourseZone = getHourseZone();

        if (hourseZone < -9) {
            return "GMT" + hourseZone + ":00";
        } else if (hourseZone < 0) {
            return "GMT-0" + (-hourseZone) + ":00";
        } else if (hourseZone < 9) {
            return "GMT+0" + hourseZone + ":00";
        } else {
            return "GMT+" + hourseZone + ":00";
        }

    }

    /** Returns all time zones */
    public static List<CEnum> getAllZones() {
        List<CEnum> result = new ArrayList<CEnum>(24 + 1);

        for (int i = (24+12); i >= (12+1); i--) {
            CTimeZone tz = new CTimeZone(i);
            result.add(tz);
        }

        return result;
    }

    /** Returns the Simple Class Name */
    public static String getSimpleClassName() {
        return "CTimeZone";
    }

    /** Get RAW zone */
    public char getRawZone() {
        return zone;
    }

    /** Get RAW zone in a String format */
    public String getStringRawZone() {
        return String.valueOf(zone);
    }

    @Override
    public boolean equals(Object obj) {
        return this.zone==((CTimeZone)obj).zone;
    }

    @Override
    public int hashCode() {
        return 77 + this.zone;
    }

}
