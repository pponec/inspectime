/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.cbo;

import org.ujorm.gxt.client.AbstractCujo;
import org.ujorm.gxt.client.Cujo;
import org.ujorm.gxt.client.CujoProperty;
import org.ujorm.gxt.client.CujoPropertyList;
import java.io.Serializable;

/**
 * CUser
 * @author Ponec
 */
public class CUser extends AbstractCujo implements Serializable {

    private static final CujoPropertyList pl = list(CUser.class);

    /** Primary Key */
    public static final CujoProperty<CUser,Long> id = pl.newProperty("id", Long.class);
    /** Not deleted. The null value means a logical deleted state. */
    public static final CujoProperty<CUser,Boolean> active = pl.newProperty("active", Boolean.class);
    /** Login */
    public static final CujoProperty<CUser,String> login = pl.newProperty("login", String.class);
    /** Email */
    public static final CujoProperty<CUser,String> email = pl.newProperty("email", String.class);
    /** Password Native */
    public static final CujoProperty<CUser,String> passwordNative = pl.newProperty("password", String.class);
    /** User Name */
    public static final CujoProperty<CUser,String> name = pl.newProperty("name", String.class);
    /** User Name */
    public static final CujoProperty<CUser,String> pid = pl.newProperty("pid", String.class);
    /** Work  fund staff per week. */
    public static final CujoProperty<CUser,Short> workFundStafPerWeek = pl.newPropertyDef("workFundStafPerWeek", (short)(8*5));
    /** Description */
    public static final CujoProperty<CUser,String> description = pl.newProperty("description", String.class);
    /** User group */
    public static final CujoProperty<CUser, CUserGroup> userGroup = pl.newProperty("userGroup", CUserGroup.class);
    /** Enabled */
    public static final CujoProperty<CUser,Boolean> enabled = pl.newProperty("enabled", Boolean.class);
    /** Account non expired */
    public static final CujoProperty<CUser,Boolean> accountNonExpired = pl.newProperty("accountNonExpired", Boolean.class);
    /** Credentials non expired */
    public static final CujoProperty<CUser,Boolean> credentialsNonExpired = pl.newProperty("credentialsNonExpired", Boolean.class);
    /** Account non locked */
    public static final CujoProperty<CUser,Boolean> accountNonLocked = pl.newProperty("accountNonLocked", Boolean.class);
    /** UserRights by sample: 1,3,5 */
    public static final CujoProperty<CUser,String> roles = pl.newPropertyDef("roles", "");
    /** UserRights by sample: 1,3,5 */
    public static final CujoProperty<CUser,String> timeZone = pl.newPropertyDef("timeZone", "B");
    /** User Company (the transient property) */
    public static final CujoProperty<CUser,CCompany> company = pl.newProperty("Company ", CCompany.class);
    //

    @Override
    public CujoPropertyList readProperties() {
        return pl;
    }

    /** Create new user and initializa it. */
    public static CUser create() {
        return new CUser().init();
    }

    /** Initialize new User */
    final public CUser init() {
        set(active, true);
        set(enabled, true);
        set(accountNonExpired, true);
        set(credentialsNonExpired, true);
        set(accountNonLocked, true);
        set(roles, "0");
        set(workFundStafPerWeek, workFundStafPerWeek.getDefault());
        return this;
    }

    @Override
    public <T extends Cujo> T createInstance() {
        return (T) new CUser();
    }



    // <editor-fold defaultstate="collapsed" desc="SET/GET">
    public Long getId() {
        return id.getValue(this);
    }

    public String getName() {
        return name.getValue(this);
    }

    public String getLogin() {
        return login.getValue(this);
    }

    public String getEmail() {
        return email.getValue(this);
    }

    public CCompany getCompany() {
        return company.getValue(this);
    }
    
    public String getTimeZone() {
        return timeZone.getValue(this);
    }



    // </editor-fold>


    @Override
    public String toString() {
        return get(login);
    }

}
