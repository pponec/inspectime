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
    public static final Key<Project, Long> id = newKey($ID);

    /** Not deleted. The null value means a logical deleted state. */
    @Comment("Not deleted. The null value means a logical deleted state")
    @Column(uniqueIndex = INDEX_NAME)
    public static final Key<Project, Boolean> active = newKey($ACTIVE);

    /** Is the project finished? */
    @Column()
    public static final Key<Project, Boolean> finished = newKey(false);

    /** Project name (login) */
    @Column(length = 100, mandatory = true, uniqueIndex = INDEX_NAME)
    public static final Key<Project, String> name = newKey();

    /** Product description */
    @Column(name = "description", length = 250, mandatory = false)
    public static final Key<Project, String> description = newKey();

    /** Product */
    @Column(name = "id_product", mandatory = true, uniqueIndex = INDEX_NAME)
    public static final Key<Project, Product> product = newKey($ACTIVE);

    /** Customer */
    @Column(name="id_customer", mandatory = true)
    public static final Key<Project, Customer> customer = newKey(DEFAULT_CUSTOMER);

    /** Completion date */
    @Comment("Completion date")
    @Column(name="completion_date", mandatory = false)
    public static final Key<Project, java.sql.Date> completionDate = newKey();

    /** Timestamp of creation */
    @Column(mandatory=true)
    public static final Key<Project, Date> created = newKey();

    /** Created by user */
    @Column(mandatory=true)
    public static final Key<Project, User> createdBy = newKey();

    /** Timestamp of the last modification */
    @Comment("Timestamp of the last modification")
    @Column(mandatory=true)
    public static final Key<Project, Date> modified = newKey();

    /** Modified by user */
    @Comment("Modified by user")
    @Column(mandatory=!true)
    public static final Key<Project, User> modifiedBy = newKey();

    /** Graph Color */
    @Comment("Graph Color")
    @Column(mandatory=true)
    public static final Key<Project, Color> graphColor = newKey($GRAPH_COLOR, Color.RED);

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
