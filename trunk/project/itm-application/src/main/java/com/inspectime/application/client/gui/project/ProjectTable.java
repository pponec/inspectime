/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.inspectime.application.client.gui.project;

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
import com.inspectime.application.client.cbo.CCustomer;
import com.inspectime.application.client.cbo.CProduct;
import com.inspectime.application.client.cbo.CProject;
import com.inspectime.application.client.cbo.CRelProjUsGroup;
import com.inspectime.application.client.clientTools.AbstractEditDialog;
import com.inspectime.application.client.clientTools.ColorRenderer;
import org.ujorm.gxt.client.tools.MessageDialog;
import org.ujorm.gxt.client.cquery.CCriterion;
import org.ujorm.gxt.client.cquery.CQuery;
import com.inspectime.application.client.clientTools.AbstractTable;
import org.ujorm.gxt.client.gui.TableListDialog;
import com.inspectime.application.client.gui.projectUsrGroup.ProjectUsrGroupTable;
import com.inspectime.application.client.gui.release.ReleaseTable;
import com.inspectime.application.client.gui.task.TaskTable;
import java.util.List;
import org.ujorm.gxt.client.CujoProperty;

/**
 *
 * @author Ponec
 */
public class ProjectTable<CUJO extends CProject> extends AbstractTable<CUJO> {

    protected String buttonRelease = "Releases";
    protected String buttonTasks    = "Tasks";

    private CProduct productFilter;


    public ProjectTable(CQuery<CUJO> query) {
        super(query);
    }

    public ProjectTable() {
    }

    public ProjectTable(CProduct productFilter) {
        if (productFilter!=null) {
            this.productFilter = productFilter;
            CCriterion<? super CUJO> crn = CProject.product.whereEq(productFilter);
            addCriterion(crn);
        }
    }

    /** Create a new edit dialog. */
    @Override
    protected ProjectEditDialog createDialogInstance() {
        return new ProjectEditDialog();
    }

    /** Create a new edit dialog. */
    @Override
    @SuppressWarnings("unchecked")
    protected AbstractEditDialog<CUJO> createTableEditDialog(final CUJO selectedItem, boolean newState, boolean clone) {
        final ProjectEditDialog dialog = createDialogInstance();
        CProject cujo = newState ? dialog.createItem() : selectedItem ;
        if (clone) copy(selectedItem, cujo);
        cujo.set(CProject.modifiedBy, null);
        dialog.init((CUJO)cujo, newState, getEditQuery(newState, selectedItem));
        dialog.setProduct(productFilter);
        return dialog;
    }

    /** Specify a list of the Table columns */
    @Override
    protected CujoProperty[] createTableColumns() {
        return new CujoProperty[]
          { CProject.finished
          , CProject.name
          , CProject.description
          , CProject.product.add(CProduct.name)
          , CProject.customer.add(CCustomer.name)
          , CProject.completionDate
          , CProject.graphColor
          , CProject.created
          , CProject.createdBy
          , CProject.modified
          , CProject.modifiedBy
        };
    }

    @Override
    protected CQuery<? super CUJO> createDefaultQuery() {
        CQuery<CProject> result = CQuery.newInstance(CProject.class, createTableColumns());

        CCriterion<CProject> crn = isSelectMode()
            ? createProjectCriterion()
            : CProject.active.whereEq(true)
            ;

        result.setCriterion(crn);
        result.getColumnConfig(CProject.name).setWidth(200);
        result.addOrderBy(CProject.name);

        // Modify label(s):
        result.getColumnConfig(CProject.product.add(CProduct.name)).setHeader("Product");
        result.getColumnConfig(CProject.customer.add(CCustomer.name)).setHeader("Customer");
        result.getColumnConfig(CProject.graphColor).setRenderer(ColorRenderer.getInstance());

        // Hide columns(s):
        result.getColumnConfig(CProject.created).setHidden(true);
        result.getColumnConfig(CProject.createdBy).setHidden(true);
        result.getColumnConfig(CProject.modified).setHidden(true);
        result.getColumnConfig(CProject.modifiedBy).setHidden(true);

        return result;
    }

    /** Create Criterion for select a valid Projects. */
    public static CCriterion<CProject> createProjectCriterion() {
            final CCriterion<CProject> crn, crn1, crn2, crn3, crn4;
            crn1 = CProject.active.whereEq(true);
            crn2 = CProject.finished.whereEq( false);
            crn3 = CProject.product.add(CProduct.active).whereEq( true) ;
            crn4 = CProject.customer.add(CCustomer.active).whereEq( true) ;
            crn  = crn1.and(crn2).and(crn3).and(crn4);

            return crn;
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        grid.setAutoExpandColumn(getQuery().getColumnConfig(CProject.description).getId());
        grid.setAutoExpandMax(Integer.MAX_VALUE);
    }


    @Override
    protected void createButtons(final LayoutContainer buttonContainer, final PagingToolBar toolBar) {
        super.createButtons(buttonContainer, toolBar);

        Button btnTasks = addButton(buttonTasks, Application.ICONS.list(), buttonContainer);
        Button btnRelease = null; // addButton(buttonRelease, Application.ICONS.list(), buttonContainer);

        // All Tasks:
        if (btnTasks != null) {
            btnTasks.addSelectionListener(new SelectionListener<ButtonEvent>() {

                @Override
                public void componentSelected(ButtonEvent ce) {
                    final List<CUJO> selectedItems = grid.getSelectionModel().getSelectedItems();
                    if (selectedItems.size() != 1) {
                        MessageDialog.getInstance("Select one row to update.").show();
                        return;
                    }
                    final TableListDialog editDialog = createTasktListDialog(selectedItems.get(0));
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

        // Releases:
        if (btnRelease != null) {
            btnRelease.addSelectionListener(new SelectionListener<ButtonEvent>() {

                @Override
                public void componentSelected(ButtonEvent ce) {
                    final List<CUJO> selectedItems = grid.getSelectionModel().getSelectedItems();
                    if (selectedItems.size() != 1) {
                        MessageDialog.getInstance("Select one row to update.").show();
                        return;
                    }
                    final TableListDialog editDialog = createReleaseListDialog(selectedItems.get(0));
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

    /** Create a new List dialog. */
    @SuppressWarnings("unchecked")
    protected TableListDialog createGroupListDialog(final CUJO selectedProject) {
        try {
            CProject project = selectedProject;

            ProjectUsrGroupTable table = new ProjectUsrGroupTable();
            table.addCriterion(CCriterion.where(CRelProjUsGroup.project, project));

            TableListDialog result = new TableListDialog(table, null);
            table.setSelectMode(null, result);
            return result;

        } catch (Throwable e) {
            GWT.log("Can't create dialog.", e);
            return null;
        }
    }

    /** Create a new List dialog. */
    @SuppressWarnings("unchecked")
    protected TableListDialog createReleaseListDialog(final CUJO selectedProject) {
        try {
            CProject project = selectedProject;

            ReleaseTable table = new ReleaseTable(project);
            TableListDialog result = new TableListDialog(table, null);
            table.setSelectMode(null, result);
            return result;

        } catch (Throwable e) {
            GWT.log("Can't create dialog.", e);
            return null;
        }
    }


    /** Create a new List dialog. */
    @SuppressWarnings("unchecked")
    protected TableListDialog createTasktListDialog(final CUJO selectedProject) {
        try {
            CProject project = selectedProject;
            TaskTable table = new TaskTable().setProject(project);
            TableListDialog result = new TableListDialog(table, null);
            table.setSelectMode(null, result);
            return result;

        } catch (Throwable e) {
            GWT.log("Can't create dialog.", e);
            return null;
        }
    }





}
