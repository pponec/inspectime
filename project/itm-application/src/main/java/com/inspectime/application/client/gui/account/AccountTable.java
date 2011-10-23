/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */


package com.inspectime.application.client.gui.account;

import com.google.gwt.user.client.Element;
import com.inspectime.application.client.cbo.CAccount;
import com.inspectime.application.client.clientTools.AbstractEditDialog;
import com.inspectime.application.client.clientTools.AbstractTable;
import com.inspectime.application.client.clientTools.ColorRenderer;
import org.ujorm.gxt.client.CujoProperty;
import org.ujorm.gxt.client.cquery.CCriterion;
import org.ujorm.gxt.client.cquery.CQuery;

/**
 * Account
 * @author Ponec
 */
public class AccountTable<CUJO extends CAccount> extends AbstractTable<CUJO> {

    public AccountTable(CQuery<CUJO> query) {
        super(query);
    }

    public AccountTable() {
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        grid.setAutoExpandColumn(getQuery().getColumnConfig(CAccount.description).getId());
        grid.setAutoExpandMax(Integer.MAX_VALUE);
    }

    /** Create a new edit dialog. */
    @Override
    protected AbstractEditDialog<CUJO> createDialogInstance() {
        return new AccountEditDialog();
    }

    /** Specify a list of the Table columns */
    @Override
    protected CujoProperty[] createTableColumns() {
        return new CujoProperty[]
          { CAccount.name
          , CAccount.description
          , CAccount.privateState
          , CAccount.graphColor
        };
    }

    /** Returns a default database query. */
    @Override
    protected CQuery<? super CUJO> createDefaultQuery() {
        CQuery<CAccount> result = CQuery.newInstance(CAccount.class, createTableColumns());
        CCriterion<CAccount> crn = CCriterion.where(CAccount.active, true);
        result.setCriterion(crn);
        result.orderBy(CAccount.name);

        // Properties:
        result.getColumnConfig(CAccount.name).setWidth(200);
        result.getColumnConfig(CAccount.graphColor).setRenderer(ColorRenderer.getInstance());

        return result;
    }




}
