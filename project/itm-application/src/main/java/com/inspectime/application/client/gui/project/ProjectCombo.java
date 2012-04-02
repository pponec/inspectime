/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.gui.project;

import com.inspectime.application.client.cbo.CProduct;
import com.inspectime.application.client.cbo.CProject;
import java.util.List;
import org.ujorm.gxt.client.CLoginRedirectable;
import org.ujorm.gxt.client.CujoProperty;
import org.ujorm.gxt.client.cquery.CCriterion;
import org.ujorm.gxt.client.cquery.CQuery;
import org.ujorm.gxt.client.gui.OldCujoBox;

/**
 *
 * @author Pavel Ponec
 */
abstract public class ProjectCombo extends OldCujoBox<CProject> {

    public ProjectCombo(CujoProperty displayProperty, CLoginRedirectable loginRedirectable) {
        super(displayProperty, loginRedirectable);
    }

    @Override
    public void onChange(CProject selectedValue) {
    }

    @Override
    public CQuery getDefaultCQuery() {
        CQuery<CProject> projectQuery = new CQuery<CProject>(CProject.class);
        CCriterion<CProject> crn1, crn2, crn3;
        crn1 = CProject.active.whereEq(true);
        crn2 = CProject.finished.whereEq(false);
        crn3 = CProject.product.add(CProduct.active).whereEq(true);
        projectQuery.setCriterion(crn1.and(crn2).and(crn3));
        projectQuery.addOrderBy(CProject.name);
        return projectQuery;
    }

    /** Overwrite this method. */
    @Override
    protected void onDataLoadSuccess(List<CProject> data) {
        if (data.size()>0) {
            CProject project = data.get(0).createInstance();
            project.set(CProject.id, null);
            project.set(displayProperty, "[All values]");
            data.add(0, project);
        }
    }

}