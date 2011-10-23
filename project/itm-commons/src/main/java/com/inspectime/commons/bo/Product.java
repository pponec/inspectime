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
 * Product
 * @author Ponec
 */
@Comment("Product of Company")
final public class Product extends AbstractBo {

    /** Unique index name */
    private static final String INDEX_NAME = "idx_product";

    /** Primary Key */
    @Column(pk=true)
    public static final Property<Product,Long> id = newProperty($ID, Long.class);

    /** Company */
    @Column(name = "id_company", mandatory = true, uniqueIndex=INDEX_NAME)
    public static final Property<Product, Company> company = newProperty($COMPANY, Company.class);

    /** Not deleted. The null value means a logical deleted state. */
    @Comment("Not deleted. The null value means a logical deleted state")
    @Column(uniqueIndex=INDEX_NAME)
    public static final Property<Product,Boolean> active = newProperty($ACTIVE, Boolean.class);

    /** Only the one product can have got an undefied state per Company (TODO) */
    @Column(name="undefined_state")
    public static final Property<Product,Boolean> undefined = newProperty(false);

    /** Product name */
    @Column(name="product_name", length=100, mandatory=true, uniqueIndex=INDEX_NAME)
    public static final Property<Product,String> name = newProperty(String.class);

    /** Product description */
    @Column(name="description", length=250, mandatory=false)
    public static final Property<Product,String> description = newProperty(String.class);

    /** Timestamp of creation */
    @Column(mandatory=true)
    public static final Property<Product, Date> created = newProperty(Date.class);

    /** Created by user */
    @Column(mandatory=true)
    public static final Property<Product, User> createdBy = newProperty(User.class);

    /** Timestamp of the last modification */
    @Column(mandatory=true)
    public static final Property<Product, Date> modified = newProperty(Date.class);

    /** Modified by user */
    @Column(mandatory=true)
    public static final Property<Product, User> modifiedBy = newProperty(User.class);

    /** Graph Color */
    @Comment("Graph Color")
    @Column(mandatory=true)
    public static final Property<Product, Color> graphColor = newProperty($GRAPH_COLOR, Color.RED);



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
