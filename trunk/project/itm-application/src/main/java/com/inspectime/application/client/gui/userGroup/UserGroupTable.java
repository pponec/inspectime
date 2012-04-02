/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */


package com.inspectime.application.client.gui.userGroup;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.inspectime.application.client.Application;
import com.inspectime.application.client.cbo.CRelProjUsGroup;
import com.inspectime.application.client.cbo.CUserGroup;
import com.inspectime.application.client.clientTools.AbstractEditDialog;
import org.ujorm.gxt.client.tools.MessageDialog;
import org.ujorm.gxt.client.cquery.CCriterion;
import org.ujorm.gxt.client.cquery.CQuery;
import com.inspectime.application.client.clientTools.AbstractTable;
import org.ujorm.gxt.client.gui.TableListDialog;
import com.inspectime.application.client.gui.projectUsrGroup.ProjectUsrGroupTable;
import java.util.List;
import org.ujorm.gxt.client.CujoProperty;

/**
 *
 * @author Ponec
 */
public class UserGroupTable<CUJO extends CUserGroup> extends AbstractTable<CUJO> {

    protected String buttonGroups = "Projects";

    public UserGroupTable(CQuery<CUJO> query) {
        super(query);
    }

    public UserGroupTable() {
    }

    /** Create a new edit dialog. */
    @Override
    protected AbstractEditDialog<CUJO> createDialogInstance() {
        return new UserGroupEditDialog();
    }

    /** Specify a list of the Table columns */
    @Override
    protected CujoProperty[] createTableColumns() {
        return new CujoProperty[]
        {
        };
    }

    /** Returns a default database query. */
    @Override
    protected CQuery<? super CUJO> createDefaultQuery() {
        CQuery<CUserGroup> result = CQuery.newInstance(CUserGroup.class, createTableColumns());
        CCriterion<CUserGroup> crn = CUserGroup.active.whereEq( true);
        result.setCriterion(crn);
        result.orderBy(CUserGroup.name);
        return result;
    }


    @Override
    protected void createButtons(final LayoutContainer buttonContainer, final PagingToolBar toolBar) {
        super.createButtons(buttonContainer, toolBar);

        Button buttonGr = addButton(buttonGroups, Application.ICONS.list(), buttonContainer);

        // UPDATE button action:
        if (buttonGr != null) {
            buttonGr.addSelectionListener(new SelectionListener<ButtonEvent>() {

                @Override
                public void componentSelected(ButtonEvent ce) {
                    final List<CUJO> selectedItems = grid.getSelectionModel().getSelectedItems();
                    if (selectedItems.size() != 1) {
                        MessageDialog.getInstance("Select one row to update.").show();
                        return;
                    }
                    final TableListDialog editDialog = createGroupEditDialog(selectedItems.get(0));
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

    /** Create a new edit dialog.
     * (ProjectUsrGroupTable)
     */
    @SuppressWarnings("unchecked")
    protected TableListDialog createGroupEditDialog(final CUJO selectedProject) {
        CUserGroup userGroup = selectedProject ;

        ProjectUsrGroupTable table = new ProjectUsrGroupTable();
        table.addCriterion(CRelProjUsGroup.userGroup.whereEq(userGroup));

        TableListDialog result = new TableListDialog(table, null);
        table.setSelectMode(null, result);
        return result;
    }


}
