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
import org.ujorm.implementation.orm.RelationToMany;
import org.ujorm.orm.OrmKeyFactory;
import org.ujorm.orm.annot.Column;
import org.ujorm.orm.annot.Comment;

/**
 * Customer
 * @author Ponec
 */
final public class Customer extends AbstractBo {

    //** Index name */
    private static final String INDEX_NAME = "idx_customer";
    
    private static final OrmKeyFactory f = (OrmKeyFactory) newFactory(Customer.class);

    /** Primary Key */
    @Column(pk=true)
    public static final Key<Customer,Long> id = f.newKey();

    /** Not deleted. The null value means a logical deleted state. */
    @Column(uniqueIndex=INDEX_NAME)
    public static final Key<Customer,Boolean> active = f.newKey($ACTIVE);

    /** Customer Name */
    @Column(length=100, mandatory=true, uniqueIndex=INDEX_NAME)
    public static final Key<Customer,String> name = f.newKey();

    /** Company */
    @Column(name = "id_company", mandatory = true, uniqueIndex=INDEX_NAME)
    public static final Key<Product, Company> company = f.newKey($COMPANY);

    /** Desccription */
    @Column(length=250)
    public static final Key<Customer,String> description = f.newKey();

    /** Timestamp of creation */
    @Column(mandatory=true)
    public static final Key<Customer, Date> created = f.newKey();
    /** Created by user */
    @Column(mandatory=!true)
    public static final Key<Customer, User> createdBy = f.newKey();
    /** Timestamp of the last modification */
    @Column(mandatory=true)
    public static final Key<Customer, Date> modified = f.newKey();
    /** Modified by user */
    @Column(mandatory=!true)
    public static final Key<Customer, User> modifiedBy = f.newKey();

    /** All projects of this customer */
    public static final RelationToMany<Customer,Project> projects = f.newRelation();

    /** Graph Color */
    @Comment("Graph Color")
    @Column(mandatory=true)
    public static final Key<Customer, Color> graphColor = f.newKey($GRAPH_COLOR, Color.RED);

    /** Property initialization */
    static { f.lock(); }

    
    public Customer() {
        active.setValue(this, true);
    }

    /** Create customer with a default ID */
    public Customer(long _id) {
        this();
        id.setValue(this, _id);
    }

    // --- SETTERS / GETTERS --------------------

    // <editor-fold defaultstate="collapsed" desc="SET/GET">
    @Override
    public Long getId() {
        return id.getValue(this);
    }
    // </editor-fold>


}
