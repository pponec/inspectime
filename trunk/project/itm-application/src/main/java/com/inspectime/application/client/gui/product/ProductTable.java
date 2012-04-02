/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.gui.product;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.inspectime.application.client.Application;
import com.inspectime.application.client.cbo.CProduct;
import com.inspectime.application.client.clientTools.AbstractEditDialog;
import com.inspectime.application.client.clientTools.ColorRenderer;
import org.ujorm.gxt.client.tools.MessageDialog;
import org.ujorm.gxt.client.cquery.CCriterion;
import org.ujorm.gxt.client.cquery.CQuery;
import org.ujorm.gxt.client.gui.TableListDialog;
import com.inspectime.application.client.clientTools.AbstractTable;
import com.inspectime.application.client.gui.project.ProjectTable;
import java.util.List;
import org.ujorm.gxt.client.CujoProperty;

/**
 *
 * @author Ponec
 */
public class ProductTable<CUJO extends CProduct> extends AbstractTable<CUJO> {

    protected String buttonComps = "Components";
    protected String buttonProjs = "Projects";

    public ProductTable(CQuery<CUJO> query) {
        super(query);
    }

    public ProductTable() {
        super();
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        grid.setAutoExpandColumn(getQuery().getColumnConfig(CProduct.description).getId());
        grid.setAutoExpandMax(Integer.MAX_VALUE);
    }

    @Override
    protected void createButtons(LayoutContainer buttonContainer, PagingToolBar toolBar) {
        super.createButtons(buttonContainer, toolBar);

        Button btnProjs = addButton(buttonProjs, Application.ICONS.list(), buttonContainer);


        // Projects:
        if (btnProjs != null) {
            btnProjs.addSelectionListener(new SelectionListener<ButtonEvent>() {

                @Override
                public void componentSelected(ButtonEvent ce) {
                    final List<CUJO> selectedItems = grid.getSelectionModel().getSelectedItems();
                    if (selectedItems.size() != 1) {
                        MessageDialog.getInstance("Select one row to update.").show();
                        return;
                    }
                    final TableListDialog editDialog = createProjectListDialog(selectedItems.get(0));
                    editDialog.addListener(Events.BeforeHide, new Listener<WindowEvent>() {
                        @Override
                        public void handleEvent(WindowEvent be) {
                            boolean ok = editDialog.isChangedData();
                            if (ok) {
                                // toolBar.refresh();
                            }
                        }
                    });
                    editDialog.show();
                }
            });
        }
    }


    /** Create a new edit dialog. */
    @Override
    protected AbstractEditDialog<CUJO> createDialogInstance() {
        return new ProductEditDialog();
    }

    /** Create a new List dialog. */
    @SuppressWarnings("unchecked")
    protected TableListDialog createProjectListDialog(final CUJO selectedProduct) {
        try {
            ProjectTable table = new ProjectTable(selectedProduct);
            TableListDialog result = new TableListDialog(table, null);
            table.setSelectMode(null, result);
            return result;

        } catch (Throwable e) {
            GWT.log("Can't create dialog.", e);
            return null;
        }
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
        CQuery<CProduct> result = CQuery.newInstance(CProduct.class, createTableColumns());
        //
        result.addOrderBy(CProduct.id); // Default orderBy
        result.setCriterion(CProduct.active.whereEq(true));
        result.getColumnConfig(CProduct.active).setHidden(true);
        result.getColumnConfig(CProduct.company).setHidden(true);
        result.getColumnConfig(CProduct.created).setHidden(true);
        result.getColumnConfig(CProduct.createdBy).setHidden(true);
        result.getColumnConfig(CProduct.modified).setHidden(true);
        result.getColumnConfig(CProduct.modifiedBy).setHidden(true);

        result.getColumnConfig(CProduct.name).setWidth(200);
        result.getColumnConfig(CProduct.graphColor).setRenderer(ColorRenderer.getInstance());

        return result;
    }


}
