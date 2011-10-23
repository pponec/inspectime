/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.inspectime.commons.bo.enums;

/**
 * User roles
 * @author Pavel Ponec
 */
public enum RoleEnum /* implements org.ujorm.extensions.ValueExportable */ {

    /** Common user */
    USER(RoleInterfaceEnum.USER),
    /** Manager */
    MANAGER(RoleInterfaceEnum.MANAGER),
    /** Inspectime administrator */
    ADMIN(RoleInterfaceEnum.ADMIN),
    /** Top administrator */
    ROOT(RoleInterfaceEnum.ROOT),
    ;

    final private String roleName;

    /**
     * RoleEnum constuctor
     * @param roleName A role of the RoleInterfaceEnum.
     */
    private RoleEnum(String roleName) {
        this.roleName = roleName;
    }

    /** RoleEnum name */
    public final String getRoleName() {
        return roleName;
    }
    
    // @Override
    public String exportToString() {
        return name().substring(0, 1);
    }

 
}
