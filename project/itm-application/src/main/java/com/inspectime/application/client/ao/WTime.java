/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */


package com.inspectime.application.client.ao;

import com.extjs.gxt.ui.client.util.DateWrapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import java.io.Serializable;


/**
 * Work Time Item
 * @author Pavel Ponec
 */
final public class WTime implements Comparable, Serializable {

    public static final DateTimeFormat DEFAULT_TIME_FORMAT = GWT.isClient()
        ? DateTimeFormat.getFormat("HH:mm")
        : null
        ;
    
    /** Separator */
    public static final String SEPARATOR = ":";
    
    
    public static int VALUE_MIN = 0;
    public static int VALUE_MAX = 24*60;
    public static int ONE_DAY = VALUE_MAX;

    /** minutes from 0 to 60*24-1 */
    private short minutes = 0;
    /** Second */
    transient private short sec = -1;


    /** Default constructor for GWT */
    public WTime() {
    }

    /**
     * Creates a new instance of WTime
     * @param time Format 23:56
     */
    public WTime(String time) {
        setStringTime(time);
    }
    
    /**
     * Creates a new instance of a System WTime.
     * No validation.
     */
    public WTime(int minutes) {
        if (minutes > Short.MAX_VALUE) {
            this.minutes = Short.MAX_VALUE;
        } else if (minutes < 0) {
            this.minutes = 0;
        } else {
            this.minutes = (short) minutes;
        }
    }
    
    /**
     * Creates a new instance of a System WTime.
     */
    public WTime(boolean systemTime) {
        if (systemTime) {
            setCurrentTime();
        }
    }
    
    /** Assign a system time. */
    final public WTime setCurrentTime() {

//        if (DEFAULT_TIME_FORMAT!=null) {
//            String time = DEFAULT_TIME_FORMAT.format(new java.util.Date());
//            setStringTime(time);
//        } else {
//            setTime(0);
//        }

        if (DEFAULT_TIME_FORMAT!=null) {
            DateWrapper dateWrapper = new DateWrapper(new java.util.Date());
            int minutes = dateWrapper.getHours() * 60 + dateWrapper.getMinutes();
            setTime(minutes);
        }
        
        return this;
    }
    
    public void setTime(int hourse, int minute) {
        final int _mins  = hourse * 60 + minute;
        setTime(_mins);
    }
    
    public void setTime(int minutes) {
        valid(minutes);
        this.minutes = (short) minutes;
    }

    /**
     * Set time from String
     * @param time HH:MM or HHMM
     */
    final public void setStringTime(String time) {
        if (time.indexOf('.')>=0) { time = time.replace('.', ':'); }

        int timeLength = time.length();
        int separIndex = time.indexOf(SEPARATOR);
        
        if (separIndex < 0 && timeLength <= 2) {
            int hourse = Integer.parseInt(time);
            if (hourse<=24) {
                setTime(hourse, 0); // hourses
            } else {
                setTime(0, hourse); // minutes
            }
            return;
        }
        else if(separIndex < 0 && timeLength >= 3) {
            StringBuilder sb = new StringBuilder(5);
            sb.append('0');
            sb.append(time.substring(0, timeLength-2));
            sb.append(SEPARATOR);
            sb.append(time.substring(timeLength-2));
            time = sb.toString();
        }

        String[] st = time.split(SEPARATOR);
        int hourse = Integer.parseInt(st[0]);
        int minute = Integer.parseInt(st[1]);
        setTime(hourse, minute);
    }

    
    /** Returns WTime in minutes. */
    public short getTimeMinutes() {
        return minutes;
    }
    
    protected void valid(int minutes) {
        if (minutes<VALUE_MIN
        ||  minutes>VALUE_MAX
        ){
            throw new RuntimeException("Time is out of range: " + (minutes/60) + SEPARATOR + (minutes % 60)  );
        }
    }
    
    public void addTime(WTime time) {
        int result = this.minutes += time.minutes;
        valid(result);
        this.minutes = (short) result;
    }

    public void addMinute(int minutes) {
        int result = this.minutes += minutes;
        valid(result);
        this.minutes = (short) result;
    }
    
    public void addHour(float aHours) {
        int hours   = (int)   aHours ;
        int minutes = (int) ((aHours - hours) * 60f);
        valid(minutes);
        this.minutes = (short) minutes;
    }
    
    /** Result [minutes]: this - time. */
    public short substract(WTime time) {
        final int result = this.minutes - time.minutes;
        return (short) result;
    }
    
    /** Return minutes */
    public int getMinutes() {
        return minutes;
    }

    /** Add one second - no validation */
    public void addOneSec() {
        if (isSecondsSupported()) {
            if (sec >= 59) {
                sec = 0;
                addMinute(1);
            } else {
                ++sec;
            }
        }
    }

    
    @Override
    public String toString() {
        return toString(false);
    }

    public String toString(boolean leftTrim) {
        if (minutes==Short.MAX_VALUE) {
            return "month(s)";
        }
        if (minutes>(ONE_DAY*2)) {
            int result = (minutes + (12*60)) / ONE_DAY;
            return String.valueOf(result) + " days";
        }

        StringBuilder result = new StringBuilder(5);
        int hourse = minutes / 60 ;
        int minute = minutes % 60 ;

        if (!leftTrim && hourse<10) { result.append('0'); }
        result.append(hourse);
        //
        result.append(SEPARATOR);
        if (minute<10) { result.append('0'); }
        result.append(minute);

        if (isSecondsSupported()) {
            if (sec >= 10) {
                result.append(":");
                result.append(sec);
            } else {
                result.append(":0");
                result.append(sec);
            }
        }

        return result.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        final boolean result = this.minutes == ((WTime)obj).minutes;
        return  result ;
    }
    
    @Override
    public int compareTo(Object obj) {
        final int result
        = this.minutes < ((WTime)obj).minutes ? -1
        : this.minutes > ((WTime)obj).minutes ? +1
        : 0
        ;
        return result;
    }
    
    /**
     * Add some minutes to a new WTime instance.
     */
    public WTime cloneAdd(int period) {
        return new WTime(minutes + period);
    }
    
    /** Returns a clone */
    public Object clone(int depth, final Object context) {
        return depth<=0 ? this : new WTime(minutes);
    }

    /** Add one minute - no-validation */
    public void addMinute() {
        ++minutes;
    }
    
    /** Is instance of the WTimeSec ? */
    final public boolean isSecondsSupported() {
        return sec>=0;
    }

    /** Enable seconds */
    public WTime enableSec() {
        sec = 0;
        return this;
    }

    /** Is the sec zero. */
    public boolean isZeroSec() {
        return sec==0;
    }

}
