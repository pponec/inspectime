/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */


package com.inspectime.commons.bo.item;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import org.ujorm.extensions.ValueTextable;
import org.ujorm.extensions.UjoCloneable;

/**
 * Year, month and day
 * @author Pavel Ponec
 */
public class YearMonthDay implements Comparable, ValueTextable, UjoCloneable {
    
    public static final int TYPE_DAY   = 0;
    public static final int TYPE_MONTH = 1;
    public static final int TYPE_YEAR  = 2;
    
    public static final SimpleDateFormat exportDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    
    private int yearMonthDay;
    
    /**
     * Creates a new instance of Time
     * @param yearMonthDay 2006-10-21
     */
    protected YearMonthDay(int yearMonthDay) {
        this.yearMonthDay = yearMonthDay;
    }
    
    /**
     * Creates a new instance of Time
     * @param yearMonthDay 2006-10-21
     */
    public YearMonthDay(String yearMonthDay) {
        try {
            Date date = exportDateFormat.parse(yearMonthDay);
            setYearMonthDay(date);
        } catch (Throwable ex) {
            new IllegalArgumentException("Bad parameter: " + yearMonthDay, ex);
        }
    }
    
    /**
     * Creates a new instance of Time
     */
    public YearMonthDay(Date date) {
        setYearMonthDay(date);
    }
    
    
    /**
     * Creates a new TODAY instance of Time
     */
    public YearMonthDay() {
        setToday();
    }
    
    
    /** Set value by Date */
    public void setYearMonthDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        setYearMonthDay(calendar);
    }
    
    /** Set value by Calendar */
    public void setYearMonthDay(Calendar calendar) {
        int date = 0;
        date += calendar.get(Calendar.YEAR ) * 100 * 100;
        date += calendar.get(Calendar.MONTH) * 100;
        date += calendar.get(Calendar.DAY_OF_MONTH);
        
        yearMonthDay = date;
    }
    
    /** See constants TYPE* for more information. */
    public int get(int itemType) {
        switch(itemType) {
            case TYPE_YEAR : return  yearMonthDay / 100  / 100 ;
            case TYPE_MONTH: return (yearMonthDay / 100) % 100 ;
            case TYPE_DAY  : return  yearMonthDay % 100        ;
            default: throw new IllegalArgumentException(String.valueOf(itemType));
        }
    }
    
    public Calendar getCalendar() {
        final int year  = get(TYPE_YEAR ); //  yearMonthDay / 100  / 100 ;
        final int month = get(TYPE_MONTH); // (yearMonthDay / 100) % 100 ;
        final int day   = get(TYPE_DAY  ); //  yearMonthDay % 100;
        
        Calendar cal = Calendar.getInstance();
        ApplTools.resetTime(cal);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        
        return cal;
    }
    
    /** Get Time */
    public Date getTime() {
        return getCalendar().getTime();
    }
    
    
    public void addDay(int day) {
        Calendar cal = getCalendar();
        cal.add(Calendar.DAY_OF_MONTH, day);
        setYearMonthDay(cal);
    }
    
    public void setToday() {
        Calendar cal = Calendar.getInstance();
        ApplTools.resetTime(cal);
        setYearMonthDay(cal);
    }
    
    /** Sample: 2007-07 or 2006-12 */
    @Override
    public String toString() {
        Calendar cal = getCalendar();
        String result = exportDateFormat.format(cal.getTime());
        return result;
    }
    
    /** Get Localized String to GUI. */
    public String toString(String sFormat, Locale language) {
        SimpleDateFormat format = new SimpleDateFormat(sFormat, language);
        String result = format.format(getTime());
        return result;
    }
    
    /** Equals to another YearMonthDay ? */
    @Override
    public boolean equals(Object obj) {
        final boolean result = obj!=null && ((YearMonthDay) obj).yearMonthDay==this.yearMonthDay;
        return result ;
    }
    
//    /** HashCode */
//    @Override
//    final public int hashCode() {
//        return yearMonthDay;
//    }
    
    public YearMonthDay cloneDay() {
        return new YearMonthDay(yearMonthDay);
    }
    
    public int compareTo(Object obj) {
        final int result
        = this.yearMonthDay < ((YearMonthDay) obj).yearMonthDay ? -1
        : this.yearMonthDay > ((YearMonthDay) obj).yearMonthDay ? +1
        : 0
        ;
        return result;
    }

    public Object clone(int depth, final Object context) {
        return depth<=0 ? this : new YearMonthDay(yearMonthDay);
    }
    
}
