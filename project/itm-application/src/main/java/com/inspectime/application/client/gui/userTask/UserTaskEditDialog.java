/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.gui.userTask;

import com.inspectime.application.client.cbo.CUser;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import com.google.gwt.user.client.Element;
import com.inspectime.application.client.cbo.CTask;
import com.inspectime.application.client.cbo.CUserTask;
import com.inspectime.application.client.clientTools.AbstractEditDialog;
import com.inspectime.application.client.gui.task.TaskTable;
import com.inspectime.application.client.gui.user.UserTable;
import org.ujorm.gxt.client.gui.OldCujoField;

/**
 * Account
 * @author Ponec
 */
public class UserTaskEditDialog<CUJO extends CUserTask> extends AbstractEditDialog<CUJO> {

    private CTask taskFilter;
    private CUser userFilter;

    @Override
    public CUJO createItem() {
        return (CUJO) new CUserTask();
    }

    public void setTask(CTask taskFilter) {
        if (taskFilter!=null) {
            this.taskFilter = taskFilter;
            cujo.set(cujo.task, taskFilter);
        }
    }

    public void setUser(CUser userFilter) {
        if (userFilter!=null) {
            this.userFilter = userFilter;
            cujo.set(cujo.user, userFilter);
        }
    }

    @Override
    protected void onCreateWidgets(Element parent, int pos) {
        //super.onRender(parent, pos);
        setClosable(true);
        setModal(true);
        setHeading(newState ? "New UserTask" : "Edit UserTask");
        setWidth(500);
        setHeight(400);
        setLayout(new FillLayout());

        panel = new FormPanel();
        panel.setHeaderVisible(false);
        panel.setFieldWidth(300);
        panel.setLabelWidth(120);
        panel.setLabelAlign(LabelAlign.RIGHT);
        panel.setButtonAlign(HorizontalAlignment.RIGHT);

        createWidget(CUserTask.order);
        if (userFilter==null) createWidgetRelation(CUserTask.user, new UserTable());
        if (taskFilter==null) { 
            OldCujoField f = (OldCujoField) createWidgetRelation(CUserTask.task, new TaskTable());
            f.setDisplayProperty(CTask.DISPLAY_PROPERTY);
        }
        createWidget(CUserTask.description).setEnabled(false);
        createWidget(CUserTask.privateState).setEnabled(false);

        // ------------------
        add(panel);

        final Button update = newOkButton(newState);
        panel.addButton(update);
        update.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                onSubmit();
            }
        });
        createQuitButton(panel);


    }


}
