/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.commons.bo;

import org.ujorm.Key;
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
    public static final Key<RelProjectUsGroup,Long> id = newKey($ID);

    /** Project */
    @Column(mandatory=true, name="id_project")
    public static final Key<RelProjectUsGroup,Project> project = newKey();

    /** User Group */
    @Column(mandatory=true, name="id_user_group")
    public static final Key<RelProjectUsGroup,UserGroup> userGroup = newKey();


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
