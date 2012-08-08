/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.commons.bo;

import org.ujorm.Key;
import org.ujorm.core.UjoIterator;
import org.ujorm.extensions.Property;
import org.ujorm.implementation.orm.RelationToMany;
import org.ujorm.orm.annot.Column;
import org.ujorm.orm.annot.Comment;

/**
 * User Group
 * @author Ponec
 */
@Comment("User Group")
final public class UserGroup extends AbstractBo {

    private static final String INDEX_NAME = "idx_userGroup";

    /** PrimaryKey */
    @Column(pk=true)
    public static final Key<UserGroup,Long> id = newKey($ID);

    /** Not deleted. The null value means a logical deleted state. */
    @Comment("Not deleted. The null value means a logical deleted state")
    @Column(uniqueIndex=INDEX_NAME)
    public static final Key<UserGroup,Boolean> active = newKey($ACTIVE);

    /** Group Name */
    @Column(length=100, mandatory=true, uniqueIndex=INDEX_NAME)
    public static final Key<UserGroup,String> name = newKey();

//    /** Company Group Name */
//    @Column(length=100, mandatory=true, uniqueIndex=INDEX_NAME)
//    public static final Key<UserGroup,Company> company = newKey(Company.class); // TODO

    /** Description */
    @Column(length = 250)
    public static final Key<UserGroup, String> description = newKey();

    /** Returns all users */
    public static final RelationToMany<UserGroup,User> users = newRelation(User.class);

    /** Property initialization */
    static { init(Property.class); }

    public UserGroup() {
        active.setValue(this, true);
    }

    // <editor-fold defaultstate="collapsed" desc="SET/GET">
    @Override
    public Long getId() {
        return id.getValue(this);
    }

    public String getName() {
        return name.getValue(this);
    }

    public void setName(String aGroupName) {
        name.setValue(this, aGroupName);
    }
    // </editor-fold>

    /** Returns all users */
    public UjoIterator<User> getUsers() {
        return users.getValue(this);
    }


}
