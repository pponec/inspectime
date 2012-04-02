/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.gui.user;

import com.google.gwt.user.client.Element;
import com.inspectime.application.client.cbo.CUser;
import java.util.List;
import org.ujorm.gxt.client.CLoginRedirectable;
import org.ujorm.gxt.client.CujoProperty;
import org.ujorm.gxt.client.cquery.CCriterion;
import org.ujorm.gxt.client.cquery.CQuery;
import org.ujorm.gxt.client.gui.OldCujoBox;

/**
 * UserCombo
 * @author Pavel Ponec
 */
abstract public class UserCombo extends OldCujoBox<CUser> {

    public UserCombo(CujoProperty displayProperty, CLoginRedirectable loginRedirectable) {
        super(displayProperty, loginRedirectable);
    }

    @Override
    public void onChange(CUser selectedValue) {
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        setEmptyText("Select user ...");
    }




    @Override
    public CQuery getDefaultCQuery() {
        CQuery<CUser> userQuery = new CQuery<CUser>(CUser.class);
        CCriterion<CUser> crn1, crn2;
        crn1 = CUser.active.whereEq( true);
        crn2 = CUser.enabled.whereEq( true);
        userQuery.setCriterion(crn1.and(crn2));
        userQuery.addOrderBy(CUser.name);
        return userQuery;
    }

    /** Overwrite this method. */
    @Override
    protected void onDataLoadSuccess(List<CUser> data) {
        if (data.size()>0) {
            CUser user = data.get(0).createInstance();
            user.set(CUser.id, null);
            user.set(displayProperty, "[Me]");
            data.add(0, user);
        }
    }

}