/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */


package com.inspectime.application.client.gui.release;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.google.gwt.core.client.GWT;
import com.inspectime.application.client.Application;
import com.inspectime.application.client.cbo.CProject;
import com.inspectime.application.client.cbo.CRelease;
import com.inspectime.application.client.clientTools.AbstractEditDialog;
import org.ujorm.gxt.client.tools.MessageDialog;
import org.ujorm.gxt.client.cquery.CCriterion;
import org.ujorm.gxt.client.cquery.CQuery;
import org.ujorm.gxt.client.gui.TableListDialog;
import com.inspectime.application.client.clientTools.AbstractTable;
import com.inspectime.application.client.gui.task.TaskTable;
import org.ujorm.gxt.client.CujoProperty;

/**
 *
 * @author Ponec
 */
public class ReleaseTable<CUJO extends CRelease> extends AbstractTable<CUJO> {

    protected String buttonInsts = "Installments"; // Deployments

    private CProject projectFilter;

    public ReleaseTable(CQuery<CUJO> query) {
        super(query);
    }

    public ReleaseTable() {
    }

    public ReleaseTable(CProject projectFilter) {
        if (projectFilter!=null) {
            this.projectFilter = projectFilter;
            CCriterion<? super CUJO> crn = CCriterion.where(CRelease.project, projectFilter);
            addCriterion(crn);
        }
    }

    /** Create a new edit dialog. */
    @Override
    protected ReleaseEditDialog createDialogInstance() {
        return new ReleaseEditDialog();
    }

    /** Create a new edit dialog. */
    @Override
    @SuppressWarnings("unchecked")
    protected AbstractEditDialog<CUJO> createTableEditDialog(final CUJO selectedItem, boolean newState, boolean clone) {
        ReleaseEditDialog dialog = (ReleaseEditDialog) super.createTableEditDialog(selectedItem, newState, clone);
        dialog.setProject(projectFilter);
        return dialog;
    }

    @Override
    protected void createButtons(final LayoutContainer buttonContainer, final PagingToolBar toolBar) {
        super.createButtons(buttonContainer, toolBar);

        Button btnInsts = addButton(buttonInsts, Application.ICONS.list(), buttonContainer);

        // User Groups:
        if (btnInsts != null) {
            btnInsts.addSelectionListener(new SelectionListener<ButtonEvent>() {

                @Override
                public void componentSelected(ButtonEvent ce) {
                    final CUJO selectedItem = getFirstSelectedItem();
                    if (selectedItem==null) {
                        MessageDialog.getInstance("Select one row to update.").show();
                        return;
                    }
                    final TableListDialog editDialog = createInstallListDialog(selectedItem);
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
        CQuery<CRelease> result = CQuery.newInstance(CRelease.class, createTableColumns());
        CCriterion<CRelease> crn = CCriterion.where(CRelease.active, true);
        result.setCriterion(crn);
        result.orderBy(CRelease.name);
        return result;
    }


    /** Create a new List dialog. */
    @SuppressWarnings("unchecked")
    protected TableListDialog createInstallListDialog(final CUJO selectedRelease) {
        try {

            TaskTable table = new TaskTable(); //.setRelease(selectedRelease);
            TableListDialog result = new TableListDialog(table, null);
            table.setSelectMode(null, result);
            return result;

        } catch (Throwable e) {
            GWT.log("Can't create dialog.", e);
            return null;
        }
    }



}
