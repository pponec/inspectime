/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.gui.event;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import com.google.gwt.user.client.Element;
import com.inspectime.application.client.ao.WTime;
import com.inspectime.application.client.cbo.CEvent;
import com.inspectime.application.client.cbo.CProject;
import com.inspectime.application.client.cbo.CTask;
import com.inspectime.application.client.clientTools.AbstractEditDialog;
import com.inspectime.application.client.gui.project.ProjectTable;
import org.ujorm.gxt.client.gui.OldCujoBox;
import org.ujorm.gxt.client.CujoProperty;
import org.ujorm.gxt.client.cquery.CCriterion;
import org.ujorm.gxt.client.cquery.CQuery;

/**
 * EventEditDialog
 * @author Ponec
 */
public class EventEditDialog<CUJO extends CEvent> extends AbstractEditDialog<CUJO> {

    private OldCujoBox<CTask> taskBox;
    private OldCujoBox<CProject> projectBox;

    @Override
    public CUJO createItem() {
        return (CUJO) new CEvent().init();
    }

    @Override
    protected void onCreateWidgets(Element parent, int pos) {
        //super.onRender(parent, pos);
        setClosable(true);
        setModal(true);
        setHeading(newState ? "New Event" : "Edit Event");
        setWidth(500);
        setHeight(350);
        setLayout(new FillLayout());

        panel = new FormPanel();
        panel.setHeaderVisible(false);
        panel.setFieldWidth(300);
        panel.setLabelWidth(120);
        panel.setLabelAlign(LabelAlign.RIGHT);
        panel.setButtonAlign(HorizontalAlignment.RIGHT);

        if (newState && false) {
            createWidget(CEvent.day); // The day can not be modified.
        }
        createWidget(CEvent.startTime_);

        projectBox = new OldCujoBox<CProject>(CProject.name, redir()) {
            @Override
            public void onChange(CProject value) {
                if (isDataLoadedToComponent()) {
                    taskBox.setValue(null);
                    taskBox.reloadStore();
                    //taskBox.setEnabled(value != null);

                    if (value != null) {
                        taskBox.setEmptyText("Select task...");
                        taskBox.focus();
                        taskBox.expand();
                    } else {
                        taskBox.setEmptyText("First select a project.");
                    }
                }
            }

            @Override
            public CQuery<CProject> getDefaultCQuery() {
                CQuery<CProject> projectQuery = new CQuery<CProject>(CProject.class);
                CCriterion<CProject> crn = ProjectTable.createProjectCriterion();
                projectQuery.setCriterion(crn);
                projectQuery.addOrderBy(CProject.name);
                return projectQuery;
            }
        };
        createWidget(CEvent.project, projectBox);

        taskBox = new OldCujoBox<CTask>(CTask.DISPLAY_PROPERTY, redir()) {
            @Override
            public CQuery<CTask> getDefaultCQuery() {
                CQuery<CTask> projectQuery = new CQuery<CTask>(CTask.class);
                projectQuery.addOrderBy(CTask.DISPLAY_PROPERTY);
                final CCriterion<CTask> crn1, crn2, crn3;
                crn1 = CCriterion.where(CTask.active, true);
                crn2 = CCriterion.where(CTask.finished, false);
                crn3 = CCriterion.where(CTask.project, projectBox.getValue());
                projectQuery.setCriterion(crn1.and(crn2).and(crn2).and(crn3));
                return projectQuery;
            }

            @Override
            public void onChange(CTask selectedValue) {
                //
            }
        };

        createWidget(CEvent.task, taskBox);
        createWidget(CEvent.description);


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

    /** Copy one value per one component */
    @Override
    protected void copyValueFromComponent(CujoProperty p, Object value) throws Exception {

        if (p.isTypeOf(WTime.class)) {
            value = new WTime((String) value);
            cujo.set(p, value);
        } else {
            super.copyValueFromComponent(p, value);
        }
    }

    @Override
    protected boolean copyValuesFromComponent() {
        boolean result = super.copyValuesFromComponent();
        if (result) {
            cujo.timeSync();
        }
        return result;
    }

    @Override
    protected boolean copyValuesToComponent() {
        boolean result = super.copyValuesToComponent();
        ((TextField)findField(CEvent.startTime_)).selectAll(); // TODO: Nefunguje při INSERT
        return result;
    }


}
