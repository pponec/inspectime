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

/**
 * CUserRole
 * @author Ponec
 */
public class CUserRole extends AbstractCujo implements Serializable {

    private static final CujoPropertyList pl = list(CUserRole.class);

    /** Primary Key */
    public static final CujoProperty<CUserRole,Long> id = pl.newProperty("id", Long.class);
    /** Role */
    public static final CujoProperty<CUserRole,Integer> role = pl.newProperty("role", Integer.class);
    /** User */
    public static final CujoProperty<CUserRole,CUser> user = pl.newProperty("user", CUser.class);

    @Override
    public CujoPropertyList readProperties() {
        return pl;
    }
    
}
