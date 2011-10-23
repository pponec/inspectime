/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.inspectime.commons.bo.item;


import junit.framework.TestCase;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author pavel
 */
public class TimeZoneTest extends TestCase {

    public TimeZoneTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    @Override
    public void setUp() {
    }

    @After
    @Override
    public void tearDown() {
    }

    /**
     * Test of clone method, of class TimeZone.
     */
    @Test
    public void test1() {
        System.out.println("test1");

        TimeZone tz = new TimeZone();
        assertEquals(0, tz.getHourseZone());
        assertEquals("GMT+00:00", tz.toString());

        tz = new TimeZone(-2);
        assertEquals(-2, tz.getHourseZone());
        assertEquals("GMT-02:00", tz.toString());
    }


}