/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.commons.bo;

import java.awt.Color;
import java.util.Date;
import org.ujorm.extensions.Property;
import org.ujorm.orm.annot.Column;
import org.ujorm.orm.annot.Comment;

/**
 * Account to calculate costs of taks.
 * @author Ponec
 */
@Comment("Account to calculate costs of taks")
final public class Account extends AbstractBo {

    private static final String INDEX_NAME = "idx_account";

    /** Primary Key */
    @Comment("Primary Key")
    @Column(pk=true)
    public static final Property<Account,Long> id = newProperty($ID, Long.class);

    /** Not deleted. The null value means a logical deleted state. */
    @Comment("Not deleted. The null value means a logical deleted state")
    @Column(uniqueIndex = INDEX_NAME)
    public static final Property<Account,Boolean> active = newProperty($ACTIVE, Boolean.class);

    /** Account name / code */
    @Column(length=60, mandatory=true, uniqueIndex = INDEX_NAME)
    public static final Property<Account,String> name = newProperty(String.class);

    /** Company relation */
    @Column(mandatory = true, uniqueIndex = INDEX_NAME)
    public static final Property<User, Company> company = newProperty($COMPANY, Company.class);

    /** Description */
    @Column(length=250, mandatory=false)
    public static final Property<Account,String> description = newProperty(String.class);

    /** The account for a non-commercial use */
    public static final Property<Account,Boolean> privateState = newProperty(false);

    /** Timestamp of creation */
    @Column(mandatory=true)
    public static final Property<Account, Date> created = newProperty(Date.class);

    /** Created by user */
    @Column(mandatory=!true)
    public static final Property<Account, User> createdBy = newProperty(User.class);

    /** Timestamp of the last modification */
    @Column(mandatory=true)
    public static final Property<Account, Date> modified = newProperty(Date.class);

    /** Modified by user */
    @Column(mandatory=!true)
    public static final Property<Account, User> modifiedBy = newProperty(User.class);

    /** Graph Color */
    @Comment("Graph Color")
    @Column(mandatory=true)
    public static final Property<Account, Color> graphColor = newProperty($GRAPH_COLOR, Color.RED);


    /** Property initialization */
    static { init(Account.class); }
    
    public Account() {
        active.setValue(this, true);
    }

    // --- SETTERS / GETTERS --------------------

    // <editor-fold defaultstate="collapsed" desc="SET/GET">
    @Override
    public Long getId() {
        return id.getValue(this);
    }
    // </editor-fold>

}
