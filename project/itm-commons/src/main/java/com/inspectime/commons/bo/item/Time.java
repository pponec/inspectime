/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */


package com.inspectime.commons.bo.item;

import java.io.Serializable;
import java.util.Calendar;
import java.util.StringTokenizer;
import org.ujorm.extensions.ValueTextable;
import org.ujorm.extensions.UjoCloneable;

/**
 * Time
 * @author Pavel Ponec
 */
public class Time implements Comparable, ValueTextable, UjoCloneable, Serializable {
    
    /** Separator */
    public static final String SEPARATOR = ":";
    
    
    public static int VALUE_MIN = 0;
    public static int VALUE_MAX = 24*60;
    
    /** minutes from 0 to 60*24-1 */
    private short minutes = 0;
    
    /**
     * Creates a new instance of Time
     * @param time Format 23:56
     */
    public Time(String time) {
        if (time.indexOf('.')>=0) { time = time.replace('.', ':'); }
        
        int timeLength = time.length();
        int separIndex = time.indexOf(SEPARATOR);
        if (separIndex<0 && timeLength>=2) {
            StringBuilder sb = new StringBuilder(5);
            sb.append('0');
            sb.append(time.substring(0, timeLength-2));
            sb.append(SEPARATOR);
            sb.append(time.substring(timeLength-2));
            time = sb.toString();
        }
        
        StringTokenizer st = new StringTokenizer(time, SEPARATOR);
        int hourse = Integer.parseInt(st.nextToken());
        int minute = Integer.parseInt(st.nextToken());
        setTime(hourse, minute);
    }
    
    /**
     * Creates a new instance of a System Time.
     */
    public Time(int minutes) {
        setTime(minutes);
    }
    
    
    /**
     * Creates a new instance of a System Time.
     */
    public Time(boolean systemTime) {
        if (systemTime) {
            setTimeFromSystem();
        }
    }
    
    /** Assign a system time. */
    public void setTimeFromSystem() {
        Calendar cal = Calendar.getInstance();
        setTime( cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
    }
    
    public void setTime(int hourse, int minute) {
        final int $mins  = hourse * 60 + minute;
        setTime($mins);
    }
    
    public void setTime(int minutes) {
        valid(minutes);
        this.minutes = (short) minutes;
    }
    
    /** Returns Time in minutes. */
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
    
    public void addMinute(int minutes) {
        int result = this.minutes += minutes;
        valid(result);
        this.minutes = (short) result;
    }
    
    public void addHour(float aHours) {
        int hours   = (int)   aHours ;
        int minutes = (int) ((aHours - hours) * 60f);
    }
    
    /** Result [minutes]: this - time. */
    public short substract(Time time) {
        final int result = this.minutes - time.minutes;
        return (short) result;
    }
    
    /** Return minutes */
    public int getMinutes() {
        return minutes;
    }
    
    
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(5);
        int hourse = minutes / 60 ;
        int minute = minutes % 60 ;
        
        if (hourse<10) { result.append('0'); }
        result.append(hourse);
        //
        result.append(SEPARATOR);
        if (minute<10) { result.append('0'); }
        result.append(minute);
        
        return result.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        final boolean result = this.minutes == ((Time)obj).minutes;
        return  result ;
    }
    
    @Override
    public int compareTo(Object obj) {
        final int result
        = this.minutes < ((Time)obj).minutes ? -1
        : this.minutes > ((Time)obj).minutes ? +1
        : 0
        ;
        return result;
    }
    
    /**
     * Add some minutes to a new Time instance.
     */
    public Time cloneAdd(int period) {
        return new Time(minutes + period);
    }
    
    /** Returns a clone */
    @Override
    public Object clone(int depth, final Object context) {
        return depth<=0 ? this : new Time(minutes);
    }
    
}
