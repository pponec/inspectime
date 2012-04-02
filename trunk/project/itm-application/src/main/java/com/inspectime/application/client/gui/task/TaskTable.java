/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.gui.task;

import com.inspectime.application.client.gui.project.ProjectCombo;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.inspectime.application.client.Application;
import com.inspectime.application.client.ClientContext;
import org.ujorm.gxt.client.ao.ValidationMessage;
import com.inspectime.application.client.cbo.CEvent;
import com.inspectime.application.client.cbo.CProject;
import com.inspectime.application.client.cbo.CTask;
import org.ujorm.gxt.client.tools.MessageDialog;
import org.ujorm.gxt.client.controller.TableControllerAsync;
import org.ujorm.gxt.client.cquery.CCriterion;
import org.ujorm.gxt.client.cquery.CQuery;
import com.inspectime.application.client.clientTools.AbstractTable;
import org.ujorm.gxt.client.ClientCallback;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.inspectime.application.client.cbo.CAccount;
import com.inspectime.application.client.cbo.CProduct;
import com.inspectime.application.client.clientTools.AbstractEditDialog;
import org.ujorm.gxt.client.CujoProperty;
import org.ujorm.gxt.client.gui.OldCujoBox;


/**
 * Task
 * @author Ponec
 */
public class TaskTable<CUJO extends CTask> extends AbstractTable<CUJO> {

    private CProject projectFilter = null;
    protected String buttonEvent = "New Event";

    public TaskTable(CQuery<CUJO> query) {
        super(query);
    }

    public TaskTable() {
    }

    @Override
    protected void onRender(com.google.gwt.user.client.Element parent, int index) {
       super.onRender(parent, index);

     grid.setAutoExpandColumn(getQuery().getColumnConfig(CTask.description).getId());
     grid.setAutoExpandMax(Integer.MAX_VALUE);
        
     ToolBar toolBar = new ToolBar();
        
       OldCujoBox projectBox = new ProjectCombo(CProject.name, redir()) {
            @Override
            public void onChange(CProject value) {
                projectFilter = value!=null && value.getId()!=null
                        ? value
                        : null
                        ;
                reloadTable();
            }
        };
        toolBar.add(new Label("Project: "));
        toolBar.add(projectBox);
        cpPanel.setTopComponent(toolBar);
    }

    /** Set filter to project */
    public TaskTable setProject(CProject projectFilter) {
        if (projectFilter!=null) {
            this.projectFilter = projectFilter;
            CCriterion<? super CUJO> crn = CTask.project.whereEq(projectFilter);
            addCriterion(crn);
        }
        return this;
    }

    /** Create a new edit dialog. */
    @Override
    protected TaskEditDialog<CUJO> createDialogInstance() {
        return new TaskEditDialog();
    }
    /** Create a new edit dialog. */
    @Override
    @SuppressWarnings("unchecked")
    protected AbstractEditDialog<CUJO> createTableEditDialog(final CUJO selectedItem, boolean newState, boolean clone) {
        final TaskEditDialog<CUJO> dialog = createDialogInstance();
        CTask cujo = newState ? dialog.createItem() : selectedItem ;
        if (clone) copy(selectedItem, cujo);
        cujo.set(CTask.modifiedBy, null);
        dialog.init((CUJO)cujo, newState, getEditQuery(newState, selectedItem));
        if (projectFilter!=null) {
            dialog.setProject(projectFilter);
        }
        return dialog;
    }

    /** Specify a list of the Table columns */
    @Override
    protected CujoProperty[] createTableColumns() {
        return new CujoProperty[]
          { CTask.code
          , CTask.title
        //, CTask.name
        //, CTask.release.add(CRelease.name)
          , CTask.project.add(CProject.name)
          , CTask.account.add(CAccount.name)
          , CTask.description
//          , CTask.taskState
//          , CTask.type
          , CTask.parent
          , CTask.finished
          , CTask.privateState
          , CTask.created
          , CTask.createdBy
          , CTask.modified
          , CTask.modifiedBy
        };
    }

    /** Returns a default database query. */
    @Override
    protected CQuery<? super CUJO> createDefaultQuery() {
        CQuery<CTask> result = CQuery.newInstance(CTask.class, createTableColumns());

        CCriterion<CTask> crn = CTask.active.whereEq(true);
        if (projectFilter!=null) {
            crn = crn.and(CTask.project.whereEq(projectFilter));
        }
        else if(isSelectMode()) {
            final CCriterion<CTask> crn1, crn2, crn3, crn4, crn5;
            crn1 = CTask.finished.whereEq(false) ;
            crn2 = CTask.project.add(CProject.active).whereEq( true);
            crn3 = CTask.project.add(CProject.finished).whereEq( false) ;
            crn4 = CTask.project.add(CProject.product).add(CProduct.active).whereEq( true) ;
            crn5 = CTask.account.add(CAccount.active).whereEq( true);
            crn  = crn.and(crn1).and(crn2).and(crn3).and(crn4).and(crn5);
        }

        result.setCriterion(crn);
        result.orderBy(CTask.code);

        // Modify Header(s):
        // result.getColumnConfig(CTask.release.add(CRelease.name)).setHeader("Release");
        result.getColumnConfig(CTask.project.add(CProject.name)).setHeader("Project");
        result.getColumnConfig(CTask.account.add(CAccount.name)).setHeader("Account");

        // Hide field(s):
        result.getColumnConfig(CTask.created).setHidden(true);
        result.getColumnConfig(CTask.createdBy).setHidden(true);
        result.getColumnConfig(CTask.modified).setHidden(true);
        result.getColumnConfig(CTask.modifiedBy).setHidden(true);

        // Width:
        result.getColumnConfig(CTask.finished).setWidth(60);
        result.getColumnConfig(CTask.privateState).setWidth(40);

        return result;
    }

    @Override
    protected void createButtons(final LayoutContainer buttonContainer, final PagingToolBar toolBar) {
        super.createButtons(buttonContainer, toolBar);

        Button buttonE = addButton(buttonEvent, Application.ICONS.add(), buttonContainer);

        // New Event
        if (buttonE != null) {
            buttonE.addSelectionListener(new SelectionListener<ButtonEvent>() {

                @Override
                public void componentSelected(ButtonEvent ce) {
                    final CUJO selectedItem = getFirstSelectedItem();
                    if (selectedItem == null) {
                        MessageDialog.getInstance("No row is selected.").show();
                        return;
                    }
                    CEvent event = new CEvent().init();
                    event.set(CEvent.startTime, event.getStartTime().getTimeMinutes());
                    event.set(CEvent.task, selectedItem);
                    TableControllerAsync.Util.getInstance().saveOrUpdate(event, true, new ClientCallback<ValidationMessage>(redir()) {
                        @Override public void onSuccess(ValidationMessage result) {
                            ClientContext.getInstance().setRefreshEventTableRequest();
                            String msg = "<ul><li>"
                                    + (result.isOk() ? "New event was created: " : result.getMessage())
                                    + selectedItem.toString()
                                    + "<br/>"
                                    + selectedItem.get(CTask.DISPLAY_PROPERTY)
                                    + "</li></ul>"
                                    ;
                            Info.display(selectedItem.toString(), msg);
                        }
                    });
                }
            });
        }
    }


}
