/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.commons.bo;

import org.ujorm.Key;
import org.ujorm.extensions.Property;
import org.ujorm.orm.annot.Column;
import org.ujorm.orm.annot.Comment;

/**
 * Release
 * @author Ponec
 */
@Comment("Release of the Project")
final public class Release extends AbstractBo {

    private static final String INDEX_NAME = "idx_release";

    /** Primary Key */
    @Column(pk = true)
    public static final Key<Release, Long> id = newKey($ID);
    /** Not deleted. The null value means a logical deleted state. */
    @Comment("Not deleted. The null value means a logical deleted state")
    @Column(uniqueIndex = INDEX_NAME)
    public static final Key<Release, Boolean> active = newKey($ACTIVE);
    /*if workflow of release has been started*/
    public static final Key<Release, Boolean> wfStarted = newKey(false);
    /** Relase name */
    @Column(length = 60, mandatory = true, uniqueIndex = INDEX_NAME)
    public static final Key<Release, String> name = newKey();
    /** Planned release date */
    public static final Key<Release, java.util.Date> releaseDate = newKey();
    /** Description */
    @Column(length = 250, mandatory = false)
    public static final Key<Release, String> description = newKey();
    /** Project */
    @Column(name = "id_project", mandatory = true, uniqueIndex = "idx_release")
    public static final Key<Release, Project> project = newKey();

    /** Project Company */
    public static final Key<Release, Company> _company = project.add(Project._company);

    /** Property initialization */
    static {
        init(Release.class);
    }

    public Release() {
        active.setValue(this, true);
    }

    // --- SETTERS / GETTERS --------------------
    // <editor-fold defaultstate="collapsed" desc="SET/GET">
    @Override
    public Long getId() {
        return id.getValue(this);
    }

    public Project getProject() {
        return project.getValue(this);
    }

    public void setWFStarted(boolean b) {
        wfStarted.setValue(this, b);
    }
    // </editor-fold>
}
