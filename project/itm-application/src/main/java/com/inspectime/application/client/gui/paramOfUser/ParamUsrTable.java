/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.gui.paramOfUser;

import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.google.gwt.user.client.Element;
import com.inspectime.application.client.service.CParam4User;
import com.inspectime.application.client.cbo.CSingleUsrParam;
import com.inspectime.application.client.clientTools.AbstractEditDialog;
import org.ujorm.gxt.client.controller.TableControllerAsync;
import org.ujorm.gxt.client.cquery.CQuery;
import com.inspectime.application.client.clientTools.AbstractTable;
import java.util.List;
import org.ujorm.gxt.client.CujoProperty;

/**
 *
 * @author Ponec,Hampl
 */
public class ParamUsrTable<CUJO extends CSingleUsrParam> extends AbstractTable<CUJO> {

    static TableControllerAsync service;

    public ParamUsrTable(CQuery<CUJO> query) {
        super(query);
    }

    public ParamUsrTable() {
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        grid.setAutoExpandColumn(getQuery().getColumnConfig(CSingleUsrParam.description).getId());
        grid.setAutoExpandMax(Integer.MAX_VALUE);
    }

    /** Create a new edit dialog. */
    @Override
    protected AbstractEditDialog<CUJO> createDialogInstance() {
        return new ParamUsrEditDialog();
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
        CQuery<CSingleUsrParam> result = CQuery.newInstance(CSingleUsrParam.class, createTableColumns());
        result.addOrderBy(CSingleUsrParam.key);
        result.getColumnConfig(CSingleUsrParam.key).setWidth(200);
        return result;
    }

    @Override
    protected void createButtons(final LayoutContainer buttonContainer, final PagingToolBar toolBar) {
        buttonSelect = null;
        buttonDelete = null;
        buttonCreate = null;
        buttonCopy   = null;
        super.createButtons(buttonContainer, toolBar);
        // to do: buton - selected row to default (with dialog)
    }

    @Override
    protected void onTableLoad(PagingLoadResult<CUJO> result) {
        List<CUJO> params = result.getData();
        CParam4User.getInstance().saveAllParameters(params);
    }

}
