/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */


package com.inspectime.application.client.gui.userTask;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.inspectime.application.client.cbo.CUser;
import com.inspectime.application.client.cbo.CUserTask;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.inspectime.application.client.Application;
import com.inspectime.application.client.ClientContext;
import org.ujorm.gxt.client.ao.ValidationMessage;
import com.inspectime.application.client.cbo.CEvent;
import com.inspectime.application.client.cbo.CTask;
import org.ujorm.gxt.client.tools.MessageDialog;
import org.ujorm.gxt.client.controller.TableControllerAsync;
import org.ujorm.gxt.client.cquery.CCriterion;
import org.ujorm.gxt.client.cquery.CQuery;
import com.inspectime.application.client.clientTools.AbstractTable;
import org.ujorm.gxt.client.ClientCallback;
import com.extjs.gxt.ui.client.widget.Info;
import com.inspectime.application.client.clientTools.AbstractEditDialog;
import org.ujorm.gxt.client.CujoProperty;

/**
 * Task
 * @author Ponec
 */
public class UserTaskTable<CUJO extends CUserTask> extends AbstractTable<CUJO> {

    private CTask taskFilter;
    protected String buttonTask = "New UserTask";
    protected String buttonEvent = "New Event";

    public UserTaskTable(CQuery<CUJO> query) {
        super(query);
    }

    public UserTaskTable() {
    }

    /** Set filter to project */
    public UserTaskTable setTask(CTask taskFilter) {
        if (taskFilter!=null) {
            this.taskFilter = taskFilter;
            CCriterion<? super CUJO> crn = CCriterion.where(CUserTask.task, taskFilter);
            addCriterion(crn);
        }
        return this;
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        grid.setAutoExpandColumn(getQuery().getColumnConfig(CUserTask.description).getId());
        grid.setAutoExpandMax(Integer.MAX_VALUE);
    }

    /** Create a new edit dialog. */
    @Override
    protected UserTaskEditDialog<CUJO> createDialogInstance() {
        return new UserTaskEditDialog();
    }

    /** Create a new edit dialog. */
    @Override
    @SuppressWarnings("unchecked")
    protected AbstractEditDialog<CUJO> createTableEditDialog(final CUJO selectedItem, boolean newState, boolean clone) {
        final UserTaskEditDialog<CUJO> dialog = createDialogInstance();
        CUserTask cujo = newState ? dialog.createItem() : selectedItem ;
        if (clone) {
            copy(selectedItem, cujo);
        }
        else if (newState) {
            cujo.set(CUserTask.user, new CUser()); // Hide the user field in dialog
        }
        if (newState) {
            cujo.set(CUserTask.order, getNextTaskOrder());
        }
        dialog.init((CUJO)cujo, newState, getEditQuery(newState, selectedItem));
        dialog.setUser(cujo.get(CUserTask.user));

        return dialog;
    }

    /** Specify a list of the Table columns */
    @Override
    protected CujoProperty[] createTableColumns() {
        return new CujoProperty[]
           { CUserTask.order
           , CUserTask.task.add(CTask.code)
           , CUserTask.task.add(CTask.title)
           , CUserTask.description
           , CUserTask.privateState
        };
    }

    /** Returns a default database query. */
    @Override
    protected CQuery<? super CUJO> createDefaultQuery() {
        CQuery<CUserTask> result = CQuery.newInstance(CUserTask.class, createTableColumns());
        CCriterion<CUserTask> crn = CCriterion.where(CUserTask._isFinished, false);
        result.setCriterion(crn);
        result.orderBy(CUserTask.order, CUserTask.id);

        // Modify Labels:
        result.getColumnConfig(CUserTask.task.add(CTask.code)).setHeader("Code");
        result.getColumnConfig(CUserTask.task.add(CTask.title)).setHeader("Title");
        result.getColumnConfig(CUserTask.description).setHeader("Description");

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
                    final CTask selectedTask = selectedItem.getTask();

                    CEvent event = new CEvent().init();
                    event.set(CEvent.startTime, event.getStartTime().getTimeMinutes());
                    event.set(CEvent.task, selectedTask);
                    TableControllerAsync.Util.getInstance().saveOrUpdate(event, true, new ClientCallback<ValidationMessage>(redir()) {
                        @Override public void onSuccess(ValidationMessage result) {
                            ClientContext.getInstance().setRefreshEventTableRequest();
                            String msg = "<ul><li>"
                                    + (result.isOk() ? "New event was created: " : result.getMessage())
                                    + selectedTask.toString()
                                    + "<br/>"
                                    + selectedTask.get(CTask.description)
                                    + "</li></ul>"
                                    ;
                            Info.display(selectedTask.toString(), msg);
                        }
                    });
                }
            });
        }
    }

    @Override
    protected Button addButton(String label, AbstractImagePrototype icon, LayoutContainer c) {
        Button result = super.addButton(label, icon, c);
        if (label==buttonCreate
        ||  label==buttonUpdate
        ||  label==buttonDelete
        ||  label==buttonCopy
        ){
            if (!isSelectMode()) {
                result.addSelectionListener(getActionKeyListener());
            }
        }
        return result;
    }

    /** Create a action key listener. */
    private SelectionListener<ButtonEvent> getActionKeyListener() {

        return new SelectionListener<ButtonEvent>() {
                @Override
                public void componentSelected(ButtonEvent ce) {
                    ClientContext.getInstance().setRefreshHotKeysRequest();
                }
        };
    }

    /** Returns a next task order number */
    private Short getNextTaskOrder() {
        int result = 0;

        for (int i=grid.getStore().getCount()-1; i>=0; --i) {
            CUserTask userTask = grid.getStore().getAt(i);
            Short idx = userTask.get(userTask.order);
            if (idx!=null && idx.intValue()>result) {
                result = idx.intValue();
            }
        }

        return (short)(result+10);
    }

}
