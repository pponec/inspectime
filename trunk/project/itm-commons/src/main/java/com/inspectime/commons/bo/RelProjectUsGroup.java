/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.commons.bo;

import org.ujorm.extensions.Property;
import org.ujorm.orm.annot.Column;
import org.ujorm.orm.annot.Comment;

/**
 * Relation: Project-UserGroup
 * @author ponec
 */
@Comment("Relation: Project-UserGroup")
final public class RelProjectUsGroup extends AbstractBo {

    /** Primary Key */
    @Column(pk=true)
    public static final Property<RelProjectUsGroup,Long> id = newProperty($ID, Long.class);

    /** Project */
    @Column(mandatory=true, name="id_project")
    public static final Property<RelProjectUsGroup,Project> project = newProperty(Project.class);

    /** User Group */
    @Column(mandatory=true, name="id_user_group")
    public static final Property<RelProjectUsGroup,UserGroup> userGroup = newProperty(UserGroup.class);


    /** Property initialization */
    static { init(RelProjectUsGroup.class); }
    
    public RelProjectUsGroup() {
    }

    // --- SETTERS / GETTERS --------------------

    // <editor-fold defaultstate="collapsed" desc="SET/GET">
    @Override
    public Long getId() {
        return id.getValue(this);
    }
    // </editor-fold>

}
