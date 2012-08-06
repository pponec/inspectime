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
import org.ujorm.core.UjoIterator;
import org.ujorm.extensions.Property;
import org.ujorm.implementation.orm.RelationToMany;
import org.ujorm.orm.annot.Column;
import org.ujorm.orm.annot.Comment;

/**
 * Project
 * @author Ponec
 */
@Comment("Project of the Product for Customer")
final public class Project extends AbstractBo {

    /** TODO: remove the default customer. */
    private static final Customer DEFAULT_CUSTOMER = new Customer(0L);

    /** Unique index name */
    private static final String INDEX_NAME = "idx_project";

    /** Primary Key */
    @Column(pk = true)
    public static final Property<Project, Long> id = newProperty($ID, Long.class);

    /** Not deleted. The null value means a logical deleted state. */
    @Comment("Not deleted. The null value means a logical deleted state")
    @Column(uniqueIndex = INDEX_NAME)
    public static final Property<Project, Boolean> active = newProperty($ACTIVE, Boolean.class);

    /** Is the project finished? */
    @Column()
    public static final Property<Project, Boolean> finished = newProperty(false);

    /** Project name (login) */
    @Column(length = 100, mandatory = true, uniqueIndex = INDEX_NAME)
    public static final Property<Project, String> name = newProperty(String.class);

    /** Product description */
    @Column(name = "description", length = 250, mandatory = false)
    public static final Property<Project, String> description = newProperty(String.class);

    /** Product */
    @Column(name = "id_product", mandatory = true, uniqueIndex = INDEX_NAME)
    public static final Property<Project, Product> product = newProperty(Product.class);

    /** Customer */
    @Column(name="id_customer", mandatory = true)
    public static final Property<Project, Customer> customer = newProperty(DEFAULT_CUSTOMER);

    /** Completion date */
    @Comment("Completion date")
    @Column(name="completion_date", mandatory = false)
    public static final Property<Project, java.sql.Date> completionDate = newProperty(java.sql.Date.class);

    /** Timestamp of creation */
    @Column(mandatory=true)
    public static final Property<Project, Date> created = newProperty(Date.class);

    /** Created by user */
    @Column(mandatory=true)
    public static final Property<Project, User> createdBy = newProperty(User.class);

    /** Timestamp of the last modification */
    @Comment("Timestamp of the last modification")
    @Column(mandatory=true)
    public static final Property<Project, Date> modified = newProperty(Date.class);

    /** Modified by user */
    @Comment("Modified by user")
    @Column(mandatory=!true)
    public static final Property<Project, User> modifiedBy = newProperty(User.class);

    /** Graph Color */
    @Comment("Graph Color")
    @Column(mandatory=true)
    public static final Property<Project, Color> graphColor = newProperty($GRAPH_COLOR, Color.RED);

    public static final RelationToMany<Project, Event> events = newRelation(Event.class);

    /** Product Company */
    public static final Key<Project, Company> _company = product.add(Product.company);


    /** Property initialization */
    static {
        init(Project.class);
    }

    // --------------------------

    private Release firstRelease;

    public Project() {
        active.setValue(this, true);
    }

    /** Find or create a first Release by the ID. */
    public Release getFirstRelease() {
        if (firstRelease==null) {
            firstRelease = readSession().createQuery(Release.class).setLimit(1).orderBy(Release.id).uniqueResult();
            if (firstRelease==null) {
                firstRelease = new Release();
                firstRelease.set(Release.project, this);
                firstRelease.set(Release.name, "-");
                readSession().save(firstRelease);
            }
        }
        return firstRelease;
    }


    // <editor-fold defaultstate="collapsed" desc="SET/GET">
    @Override
    public Long getId() {
        return id.getValue(this);
    }

    public UjoIterator<Event> getEnviroments() {
        return events.getValue(this);
    }
    // </editor-fold>
}
