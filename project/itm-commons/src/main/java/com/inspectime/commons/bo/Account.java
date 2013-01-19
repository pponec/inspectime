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
import org.ujorm.Key;
import org.ujorm.core.KeyFactory;
import org.ujorm.orm.annot.Column;
import org.ujorm.orm.annot.Comment;

/**
 * Account to calculate costs of taks.
 * @author Ponec
 */
@Comment("Account to calculate costs of taks")
final public class Account extends AbstractBo {

    private static final String INDEX_NAME = "idx_account";
    
    /** Factory */
    private static final KeyFactory<Account> f = newFactory(Account.class);

    /** Primary Key */
    @Comment("Primary Key")
    @Column(pk=true)
    public static final Key<Account,Long> id = f.newKey($ID);

    /** Not deleted. The null value means a logical deleted state. */
    @Comment("Not deleted. The null value means a logical deleted state")
    @Column(uniqueIndex = INDEX_NAME)
    public static final Key<Account,Boolean> active = f.newKey($ACTIVE);

    /** Account name / code */
    @Column(length=60, mandatory=true, uniqueIndex = INDEX_NAME)
    public static final Key<Account,String> name = f.newKey();

    /** Company relation */
    @Column(mandatory = true, uniqueIndex = INDEX_NAME)
    public static final Key<Account, Company> company = f.newKey($COMPANY);

    /** Description */
    @Column(length=250, mandatory=false)
    public static final Key<Account,String> description = f.newKey();

    /** The account for a non-commercial use */
    public static final Key<Account,Boolean> privateState = f.newKeyDefault(false);

    /** Timestamp of creation */
    @Column(mandatory=true)
    public static final Key<Account, Date> created = f.newKey();

    /** Created by user */
    @Column(mandatory=!true)
    public static final Key<Account, User> createdBy = f.newKey();

    /** Timestamp of the last modification */
    @Column(mandatory=true)
    public static final Key<Account, Date> modified = f.newKey();

    /** Modified by user */
    @Column(mandatory=!true)
    public static final Key<Account, User> modifiedBy = f.newKey();

    /** Graph Color */
    @Comment("Graph Color")
    @Column(mandatory=true)
    public static final Key<Account, Color> graphColor = f.newKey($GRAPH_COLOR, Color.RED);


    /** Property initialization */
    static { 
        f.lock(); 
    }
    
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
