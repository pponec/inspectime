/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.gui.task;

import com.inspectime.application.client.cbo.CProject;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import com.google.gwt.user.client.Element;
import com.inspectime.application.client.cbo.CTask;
import com.inspectime.application.client.clientTools.AbstractEditDialog;
import com.inspectime.application.client.controller.CompanyControllerAsync;
import org.ujorm.gxt.client.ClientCallback;
import com.inspectime.application.client.gui.account.AccountTable;
import com.inspectime.application.client.gui.project.ProjectTable;

/**
 * Account
 * @author Ponec
 */
public class TaskEditDialog<CUJO extends CTask> extends AbstractEditDialog<CUJO> {

    private CProject projectFilter;

    @Override
    public CUJO createItem() {
        return (CUJO) new CTask();
    }

    public void setProject(CProject projectFilter) {
        if (projectFilter!=null) {
            this.projectFilter = projectFilter;
            cujo.set(cujo.project, projectFilter);

        }
    }

    @Override
    protected void onCreateWidgets(Element parent, int pos) {
        //super.onRender(parent, pos);
        setClosable(true);
        setModal(true);
        setHeading(newState ? "New Task" : "Edit Task");
        setWidth(500);
        setHeight(400);
        setLayout(new FillLayout());

        panel = new FormPanel();
        panel.setHeaderVisible(false);
        panel.setFieldWidth(300);
        panel.setLabelWidth(120);
        panel.setLabelAlign(LabelAlign.RIGHT);
        panel.setButtonAlign(HorizontalAlignment.RIGHT);

        createWidget(CTask.title);
        createWidget(CTask.code);
        createWidget(CTask.description);
        createWidgetRelation(CTask.project, new ProjectTable()).setEnabled(projectFilter==null);
//        createWidget(CTask.taskState);
//        createWidget(CTask.type);
        createWidgetRelation(CTask.account, new AccountTable());
        createWidget(CTask.finished);

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


        if (super.newState) {
            CompanyControllerAsync.Util.getInstance().getNextTaskSequence(new ClientCallback<Integer>(redir()) {
                @Override
                public void onSuccess(Integer value) {
                    findField(CTask.code).setValue(value);
                }
            });
        }
    }


}
