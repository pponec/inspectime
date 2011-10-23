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
import org.ujorm.implementation.orm.RelationToMany;
import org.ujorm.orm.annot.Column;
import org.ujorm.orm.annot.Comment;

/**
 * Customer
 * @author Ponec
 */
final public class Customer extends AbstractBo {

    //** Index name */
    private static final String INDEX_NAME = "idx_customer";

    /** Primary Key */
    @Column(pk=true)
    public static final Property<Customer,Long> id = newProperty(Long.class);

    /** Not deleted. The null value means a logical deleted state. */
    @Column(uniqueIndex=INDEX_NAME)
    public static final Property<Customer,Boolean> active = newProperty($ACTIVE, Boolean.class);

    /** Customer Name */
    @Column(length=100, mandatory=true, uniqueIndex=INDEX_NAME)
    public static final Property<Customer,String> name = newProperty(String.class);

    /** Company */
    @Column(name = "id_company", mandatory = true, uniqueIndex=INDEX_NAME)
    public static final Property<Product, Company> company = newProperty($COMPANY, Company.class);

    /** Desccription */
    @Column(length=250)
    public static final Property<Customer,String> description = newProperty(String.class);

    /** Timestamp of creation */
    @Column(mandatory=true)
    public static final Property<Customer, Date> created = newProperty(Date.class);
    /** Created by user */
    @Column(mandatory=!true)
    public static final Property<Customer, User> createdBy = newProperty(User.class);
    /** Timestamp of the last modification */
    @Column(mandatory=true)
    public static final Property<Customer, Date> modified = newProperty(Date.class);
    /** Modified by user */
    @Column(mandatory=!true)
    public static final Property<Customer, User> modifiedBy = newProperty(User.class);

    /** All projects of this customer */
    public static final RelationToMany<Customer,Project> projects = newRelation(Project.class);

    /** Graph Color */
    @Comment("Graph Color")
    @Column(mandatory=true)
    public static final Property<Customer, Color> graphColor = newProperty($GRAPH_COLOR, Color.RED);

    /** Property initialization */
    static { init(Customer.class); }

    
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
