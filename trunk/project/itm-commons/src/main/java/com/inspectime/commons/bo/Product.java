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
import org.ujorm.orm.annot.Column;
import org.ujorm.orm.annot.Comment;

/**
 * Product
 * @author Ponec
 */
@Comment("Product of Company")
final public class Product extends AbstractBo {

    /** Unique index name */
    private static final String INDEX_NAME = "idx_product";

    /** Primary Key */
    @Column(pk=true)
    public static final Key<Product,Long> id = newKey($ID);

    /** Company */
    @Column(name = "id_company", mandatory = true, uniqueIndex=INDEX_NAME)
    public static final Key<Product, Company> company = newKey($COMPANY);

    /** Not deleted. The null value means a logical deleted state. */
    @Comment("Not deleted. The null value means a logical deleted state")
    @Column(uniqueIndex=INDEX_NAME)
    public static final Key<Product,Boolean> active = newKey($ACTIVE);

    /** Only the one product can have got an undefied state per Company (TODO) */
    @Column(name="undefined_state")
    public static final Key<Product,Boolean> undefined = newKey(false);

    /** Product name */
    @Column(name="product_name", length=100, mandatory=true, uniqueIndex=INDEX_NAME)
    public static final Key<Product,String> name = newKey();

    /** Product description */
    @Column(name="description", length=250, mandatory=false)
    public static final Key<Product,String> description = newKey();

    /** Timestamp of creation */
    @Column(mandatory=true)
    public static final Key<Product, Date> created = newKey();

    /** Created by user */
    @Column(mandatory=true)
    public static final Key<Product, User> createdBy = newKey();

    /** Timestamp of the last modification */
    @Column(mandatory=true)
    public static final Key<Product, Date> modified = newKey();

    /** Modified by user */
    @Column(mandatory=true)
    public static final Key<Product, User> modifiedBy = newKey();

    /** Graph Color */
    @Comment("Graph Color")
    @Column(mandatory=true)
    public static final Key<Product, Color> graphColor = newKey($GRAPH_COLOR, Color.RED);



    /** Property initialization */
    static { init(Product.class); }
    
    public Product() {
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
