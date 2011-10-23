/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.commons.bo.item;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Static methods.
 * @author Pavel Ponec
 */
public final class ApplTools {
    
    private static final Logger LOGGER = Logger.getLogger("ApplTools");
    
    /**
     * Get StackTrace from an Exception.
     */
    public static StringBuffer getStackTraceBuf(Throwable anException) {
        StringWriter stringWriter = new StringWriter();
        
        if (anException==null) {
            stringWriter.write("Undefined exception (null).");
        } else {
            PrintWriter tempWriter = new PrintWriter(stringWriter);
            tempWriter.println(""+anException);
            anException.printStackTrace(tempWriter);
            tempWriter.flush();
        }
        return stringWriter.getBuffer();
    }
    
    public static DecimalFormat createDecimalFormat(String format) {
        return createDecimalFormat(format, Locale.US);
    }
    
    public static DecimalFormat createDecimalFormat(String format, Locale locale) {
        final DecimalFormat result = (DecimalFormat) DecimalFormat.getNumberInstance(locale);
        result.applyPattern(format);
        return result;
    }
    
    /** Copy a file to a target file. */
    public static void copy(File source, File target) throws IOException {
        InputStream  is = null;
        try {
            is = new BufferedInputStream( new FileInputStream(source));
            copy(is, target);
            target.setLastModified(source.lastModified()); // Set the time
        } finally {
            if (is!=null) { is.close(); }
        }
    }
    
    /** Copy a file to a target file. */
    public static void copy(InputStream is, File target) throws IOException {
        OutputStream os = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(target));
            
            int c;
            while ((c=is.read()) != -1) {
                os.write(c);
            }
        } finally {
            if (os!=null) {
                os.close();
            }
        }
    }
    
    /** Reset Time to 12:00:00.000 . */
    public static void resetTime(Calendar calednar) {
        calednar.set(Calendar.HOUR_OF_DAY, 12);
        calednar.set(Calendar.MINUTE, 0);
        calednar.set(Calendar.SECOND, 0);
        calednar.set(Calendar.MILLISECOND, 0);
    }
    
    /** Returns true, if text is not null and not empty. */
    public static final boolean isValid(CharSequence text) {
        final boolean result = text!=null && text.length()>0;
        return result;
    }
    
    /** Returns true, if environment is a Windows OS. */
    public static boolean isWindowsOS() {
        final boolean result = System.getProperty("os.name").startsWith("Windows");
        return result;
    }
    
    /** Returns true, if environment is a Linux OS. */
    public static boolean isLinuxOS() {
        final boolean result = System.getProperty("os.name").startsWith("Linux");
        return result;
    }
    
    
    /** Sleep */
    public static void sleep(int milis) {
        try {
            Thread.sleep(milis);
        } catch (InterruptedException e) {
            LOGGER.log(Level.WARNING, "err", e);
        }
    }
    
    
    /** Converts bytes to String on Java 5.0. */
    public static String newString(byte[] bytes, Charset charset) {
        String result;
        try {
            result = new String(bytes, charset.name());
        } catch (UnsupportedEncodingException e) {
            result = new String(bytes);
        }
        return result;
    }
    
    /** Converts String to bytes on Java 5.0. */
    public static byte[] getBytes(String text, Charset charset) {
        byte[] result;
        try {
            result = text.getBytes(charset.name());
        } catch (UnsupportedEncodingException e) {
            result = text.getBytes();
        }
        return result;
    }
        
}
