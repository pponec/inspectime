/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.ao;

import com.google.gwt.core.client.GWT;
import java.util.EnumSet;


/**
 * User Roles
 * @author Pavel Ponec
 * @see com.inspectime.commons.bo.enums.RoleEnum.RoleEnum
 */
public enum CRoleEnum {

    /** Common user */
    USER("user"),
    /** Manager */
    MANAGER("manager"),
    /** Inspectime administrator */
    ADMIN("admin"),
    /** Top administrator */
    ROOT("root"),
    ;

    final private String roleName;

    /**
     * RoleEnum constuctor
     * @param roleName A role of the RoleInterfaceEnum.
     */
    private CRoleEnum(String roleName) {
        this.roleName = roleName;
    }

    /** RoleEnum name */
    public final String getRoleName() {
        return roleName;
    }

   /** Return roles */
    public static String getAllRoles() {
        return getRoles(CRoleEnum.class.getEnumConstants());
    }

    /** Return roles as String by template: "0,1,2,3" */
    public static String getRoles(CRoleEnum ... roles) {
        StringBuilder result = new StringBuilder();
        for (CRoleEnum roleEnum : roles) {
            if (result.length()>0) {
                result.append(',');
            }
            result.append(roleEnum.ordinal());
        }
        return result.toString();
    }


    /** Returns required roles from string */
    public static EnumSet<CRoleEnum> parseString(String roles) {
        EnumSet<CRoleEnum> result = EnumSet.noneOf(CRoleEnum.class);
        if (roles==null) {
            return result;
        }

        String token = "";
        for (String atoken : roles.split(",")) {
            token = atoken;
            try {
                Integer i = Integer.parseInt(token);
                result.add(CRoleEnum.values()[i]);
            } catch (Throwable e) {
                GWT.log("Cant get user role: " + token + " for: " + token);
            }
        }
        return result;
    }

}
