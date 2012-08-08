/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.commons.bo;

import org.ujorm.Key;
import org.ujorm.implementation.orm.RelationToMany;
import org.ujorm.orm.annot.Column;
import org.ujorm.orm.annot.Comment;

/**
 * Company
 * @author Ponec
 */
@Comment("Company")
final public class Company extends AbstractBo {

    //** Index name */
    //private static final String INDEX_NAME = "idx_company_name";

    /** Primary Key */
    @Comment("Primary Key")
    @Column(pk=true)
    public static final Key<Company,Long> id = newKey($ID);

    /** Not deleted. The null value means a logical deleted state. */
    @Comment("Not deleted. The null value means a logical deleted state")
    public static final Key<Company,Boolean> active = newKey($ACTIVE);

    /** Company name can't be unique. */
    @Comment("Company name can't be unique.")
    @Column(length=100, mandatory=true)
    public static final Key<Company,String> name = newKey();

    /** Desccription */
    @Comment("Description")
    @Column(length=250)
    public static final Key<Company,String> description = newKey();

    /** Task Code sequence contains the value to next assign. */
    @Comment("Task Code sequence contains the value to next assign")
    @Column(mandatory=true)
    public static final Key<Company,Integer> taskCodeSeq = newKey(10000);

    /** All products of the Company */
    public static final RelationToMany<Company,Product> products = newRelation();

    /** Property initialization */
    static { init(Company.class); }

    // ------------------------------------------

    /** Returns a first product by ID. */
    private Product firstProduct;

    
    public Company() {
        active.setValue(this, true);
    }

    /** Find the first product by the ID or return null */
    public Product getFirstProduct() {
        if (firstProduct==null) {
            firstProduct = readSession().createQuery(Product.class).setLimit(1).orderBy(Product.id).uniqueResult();
        }
        return firstProduct;
    }

    // --- SETTERS / GETTERS --------------------

    // <editor-fold defaultstate="collapsed" desc="SET/GET">
    @Override
    /** Company ID */
    public Long getId() {
        return id.getValue(this);
    }

    /**  Returns Company name */
    public String getName() {
        return name.getValue(this);
    }
    // </editor-fold>

    /** Get next task Code sequence and imcrement it */
    public int nextTaskCodeSeq() {
        int result = taskCodeSeq.getValue(this);
        taskCodeSeq.setValue(this, result+1);
        return result;
    }

}
