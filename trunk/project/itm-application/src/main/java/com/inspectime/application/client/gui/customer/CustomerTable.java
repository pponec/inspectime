/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */


package com.inspectime.application.client.gui.customer;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.google.gwt.user.client.Element;
import com.inspectime.application.client.cbo.CCustomer;
import com.inspectime.application.client.clientTools.AbstractEditDialog;
import com.inspectime.application.client.clientTools.ColorRenderer;
import org.ujorm.gxt.client.CujoProperty;
import org.ujorm.gxt.client.cquery.CCriterion;
import org.ujorm.gxt.client.cquery.CQuery;
import com.inspectime.application.client.clientTools.AbstractTable;

/**
 *
 * @author Ponec
 */
public class CustomerTable<CUJO extends CCustomer> extends AbstractTable<CUJO> {


    public CustomerTable(CQuery<CUJO> query) {
        super(query);
    }

    public CustomerTable() {
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        grid.setAutoExpandColumn(getQuery().getColumnConfig(CCustomer.description).getId());
        grid.setAutoExpandMax(Integer.MAX_VALUE);
    }

    /** Create a new edit dialog. */
    @Override
    protected AbstractEditDialog<CUJO> createDialogInstance() {
        return new CustomerEditDialog();
    }

    /** Specify a list of the Table columns */
    @Override
    protected CujoProperty[] createTableColumns() {
        return new CujoProperty[]
        {
        };
    }

    @Override
    protected CQuery<? super CUJO> createDefaultQuery() {
        CQuery<CCustomer> result = CQuery.newInstance(CCustomer.class, createTableColumns());
        result.addOrderBy(CCustomer.name);
        CCriterion<CCustomer> crn = CCriterion.where(CCustomer.active, true);
        result.setCriterion(crn);

        // Properties
        result.getColumnConfig(CCustomer.name).setWidth(200);
        result.getColumnConfig(CCustomer.graphColor).setRenderer(ColorRenderer.getInstance());

        return result;
    }

    @Override
    protected void createButtons(final LayoutContainer buttonContainer, final PagingToolBar toolBar) {
        super.createButtons(buttonContainer, toolBar);
    }



}
