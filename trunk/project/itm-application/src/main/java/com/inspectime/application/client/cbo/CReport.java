/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.cbo;

import com.inspectime.application.client.ao.WTime;
import org.ujorm.gxt.client.AbstractCujo;
import org.ujorm.gxt.client.CujoProperty;
import org.ujorm.gxt.client.CujoPropertyList;
import java.io.Serializable;

/**
 * Common two - Level report
 * @author Ponec
 */
public class CReport extends AbstractCujo implements Serializable {

    private static final CujoPropertyList pl = list(CReport.class);

    /** Level APrimary Key */
    public static final CujoProperty<CReport,Long> levelAid = pl.newProperty("levelA_id", Long.class);
    /** Primary Key */
    public static final CujoProperty<CReport,AbstractCujo> levelA = pl.newProperty("levelA", AbstractCujo.class);
    /** Primary Key */
    public static final CujoProperty<CReport,AbstractCujo> levelB = pl.newProperty("levelB", AbstractCujo.class);
    /** Time */
    public static final CujoProperty<CReport,Integer> time = pl.newPropertyDef("time", 0);
    /** Time percentGroup by group */
    public static final CujoProperty<CReport,Float> percentGroup = pl.newPropertyDef("percentGroup", 0f);
    /** Time percentGroup total */
    public static final CujoProperty<CReport,Float> percentTotal = pl.newPropertyDef("percentTotal", 0f);

    // -------------------------

    public static final CujoProperty<CReport, CCustomer> _customer = CReport.levelA.add(CProject.customer);
    public static final CujoProperty<CReport, CProduct> _product = CReport.levelA.add(CProject.product);
    //public static final CujoProperty<CReport, CProduct> _account = CReport.levelA.add(CAccount.product);

    private int timeMin = 0;

    @Override
    public CujoPropertyList readProperties() {
        return pl;
    }

    @Override
    final public Object get(String property) {
        Object result = super.get(property);
        if (result==null && time.getName().equals(property)) {
            result = timeMin;
            return result;
        }
        return super.get(property);
    }

    // <editor-fold defaultstate="collapsed" desc="SET/GET">
    public Long getIdA() {
        return levelAid.getValue(this);
    }
    public void setIdA(Long id1) {
        levelAid.setValue(this, id1);
    }
    public AbstractCujo getLevelA() {
        return levelA.getValue(this);
    }
    public void setLevelA(AbstractCujo ujo) {
        levelA.setValue(this, ujo);
    }

    public Long getIdB() {
        return getLevelB().getId();
    }

    public AbstractCujo getLevelB() {
        return levelB.getValue(this);
    }

    public void setLevelB(AbstractCujo ujo) {
        levelB.setValue(this, ujo);
    }

    /** Returns total time in minutes */
    public int getTimeMin() {
        return timeMin;
    }
    
    public void addTime(int minutes) {
        timeMin += minutes;
    }

    /** Returns total time in minutes */
    public float getPercentGroup() {
        return percentGroup.getValue(this);
    }

    /** Returns total time in minutes */
    public void setPercentGroup(float _percent) {
        percentGroup.setValue(this, _percent);
    }

    /** Returns total time in minutes */
    public float getPercentTotal() {
        return percentTotal.getValue(this);
    }

    /** Returns total time in minutes */
    public void setPercentTotal(float _percent) {
        percentTotal.setValue(this, _percent);
    }



    /** Two CReport(s) are equals if levelA.id and levelB.id are the same. */
    @Override
    public boolean equals(final Object obj) {
        final CReport ext = (CReport) obj;
        boolean result = getIdA().equals(ext.getIdA());
        if (result) {
            result = getLevelB().getId().equals(ext.getLevelB().getId());
        }
        return result;
    }
    // </editor-fold>




}
