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
import org.ujorm.implementation.orm.RelationToMany;
import org.ujorm.orm.OrmKeyFactory;
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
    
    /** Factory */
    private static final OrmKeyFactory<Project> f = newFactory(Project.class);    
    
    /** Primary Key */
    @Column(pk = true)
    public static final Key<Project, Long> id = f.newKey($ID);

    /** Not deleted. The null value means a logical deleted state. */
    @Comment("Not deleted. The null value means a logical deleted state")
    @Column(uniqueIndex = INDEX_NAME)
    public static final Key<Project, Boolean> active = f.newKey($ACTIVE);

    /** Is the project finished? */
    @Column()
    public static final Key<Project, Boolean> finished = f.newKeyDefault(false);

    /** Project name (login) */
    @Column(length = 100, mandatory = true, uniqueIndex = INDEX_NAME)
    public static final Key<Project, String> name = f.newKey();

    /** Product description */
    @Column(name = "description", length = 250, mandatory = false)
    public static final Key<Project, String> description = f.newKey();

    /** Product */
    @Column(name = "id_product", mandatory = true, uniqueIndex = INDEX_NAME)
    public static final Key<Project, Product> product = f.newKey();

    /** Customer */
    @Column(name="id_customer", mandatory = true)
    public static final Key<Project, Customer> customer = f.newKeyDefault(DEFAULT_CUSTOMER);

    /** Completion date */
    @Comment("Completion date")
    @Column(name="completion_date", mandatory = false)
    public static final Key<Project, java.sql.Date> completionDate = f.newKey();

    /** Timestamp of creation */
    @Column(mandatory=true)
    public static final Key<Project, Date> created = f.newKey();

    /** Created by user */
    @Column(mandatory=true)
    public static final Key<Project, User> createdBy = f.newKey();

    /** Timestamp of the last modification */
    @Comment("Timestamp of the last modification")
    @Column(mandatory=true)
    public static final Key<Project, Date> modified = f.newKey();

    /** Modified by user */
    @Comment("Modified by user")
    @Column(mandatory=!true)
    public static final Key<Project, User> modifiedBy = f.newKey();

    /** Graph Color */
    @Comment("Graph Color")
    @Column(mandatory=true)
    public static final Key<Project, Color> graphColor = f.newKey($GRAPH_COLOR, Color.RED);

    public static final RelationToMany<Project, Event> events = f.newRelation();

    /** Product Company */
    public static final Key<Project, Company> _company = product.add(Product.company);


    /** Property initialization */
    static {
        f.lock();
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
