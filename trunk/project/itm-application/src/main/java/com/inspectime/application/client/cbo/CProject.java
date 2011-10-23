/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.cbo;

import org.ujorm.gxt.client.AbstractCujo;
import org.ujorm.gxt.client.CujoProperty;
import org.ujorm.gxt.client.CujoPropertyList;
import java.io.Serializable;
import java.util.Date;
import org.ujorm.gxt.client.tools.ColorGxt;
import org.ujorm.orm.annot.Column;

/**
 * CProject
 * @author Ponec
 */
final public class CProject extends AbstractCujo implements Serializable {

    private static final CujoPropertyList pl = list(CProject.class);

    /** Primary Key */
    public static final CujoProperty<CProject,Long> id = pl.newProperty("id", Long.class);

    /** Not deleted. The null value means a logical deleted state. */
    public static final CujoProperty<CProject,Boolean> active = pl.newProperty("active", Boolean.class);

    /** Is the project finished? */
    public static final CujoProperty<CProject,Boolean> finished = pl.newProperty("finished", Boolean.class);

    /** Project name */
    public static final CujoProperty<CProject,String> name = pl.newProperty("name", String.class);

    /** Project description */
    public static final CujoProperty<CProject,String> description = pl.newProperty("description", String.class);

    /** Product */
    public static final CujoProperty<CProject,CProduct> product = pl.newProperty("product", CProduct.class);

    /** Product */
    public static final CujoProperty<CProject,CCustomer> customer = pl.newProperty("customer", CCustomer.class);

    /** Completon day */
    public static final CujoProperty<CProject,java.sql.Date> completionDate = pl.newProperty("completionDate", java.sql.Date.class);

    /** Created timestamp */
    public static final CujoProperty<CProject,Date> created = pl.newProperty("created", Date.class);

    /** Created by */
    public static final CujoProperty<CProject,CUser> createdBy = pl.newProperty("createdBy", CUser.class);

    /** Modified timestamp */
    public static final CujoProperty<CProject,Date> modified = pl.newProperty("modified", Date.class);

    /** Created by */
    public static final CujoProperty<CProject,CUser> modifiedBy = pl.newProperty("modifiedBy", CUser.class);

    /** Graph Color */
    public static final CujoProperty<CProject,ColorGxt> graphColor = pl.newProperty("graphColor", ColorGxt.class);


    @Override
    public CujoPropertyList readProperties() {
        return pl;
    }
    
    public CProject() {
        Date date = new Date();
        created.setValue(this, date);
        modified.setValue(this, date);
        active.setValue(this, true);
        finished.setValue(this, false);
    }

    /** Create new instance */
    @Override
    public CProject createInstance() {
        return new CProject();
    }

    // <editor-fold defaultstate="collapsed" desc="SET/GET">
    public Long getId() {
        return id.getValue(this);
    }

    public CProduct getProduct() {
        return get(product);
    }

    // </editor-fold>

    @Override
    public String toString() {
        return name.getValue(this);
    }


}
