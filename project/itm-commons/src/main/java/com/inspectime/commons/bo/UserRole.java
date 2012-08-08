/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.commons.bo;

import com.inspectime.commons.bo.enums.RoleEnum;
import org.ujorm.Key;
import org.ujorm.orm.annot.Column;
import org.ujorm.orm.annot.Comment;

/**
 * Object for maping user roles
 * user has many userroles, userrole has one role(from enum)
 * @author Hampl
 */
@Comment("Table for maping user roles")
final public class UserRole extends AbstractBo {

    private static final String INDEX_NAME = "idx_userRole";

    /** PrimaryKey */
    @Column(pk = true)
    public static final Key<UserRole, Long> id = newKey($ID);
    /** User role */
    @Column(name = "id_role", mandatory = true, uniqueIndex=INDEX_NAME)
    public static final Key<UserRole, RoleEnum> role = newKey(RoleEnum.USER);
    /** The user */
    @Column(name = "id_user", mandatory = true, uniqueIndex=INDEX_NAME)
    public static final Key<UserRole, User> user = newKey();

    static {
        init(UserRole.class);
    }

    public UserRole() {
    }

    public UserRole(User _user, RoleEnum _role) {
        set(role, _role);
        set(user, _user);
    }

    public String getRoleName() {
        return getRole().getRoleName();
    }


    // <editor-fold defaultstate="collapsed" desc="SET/GET">

    @Override
    public Long getId() {
        return id.getValue(this);
    }

    public RoleEnum getRole() {
        return role.getValue(this);
    }

    public void setRole(RoleEnum _role) {
        role.setValue(this, _role);
    }

    public void serUser(User u) {
        user.setValue(this, u);
    }
    // </editor-fold>
}
