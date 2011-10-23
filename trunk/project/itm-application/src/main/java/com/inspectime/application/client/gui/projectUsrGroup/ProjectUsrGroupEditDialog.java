/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.gui.projectUsrGroup;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import com.google.gwt.user.client.Element;
import com.inspectime.application.client.cbo.CProject;
import com.inspectime.application.client.cbo.CRelProjUsGroup;
import com.inspectime.application.client.cbo.CUserGroup;
import com.inspectime.application.client.clientTools.AbstractEditDialog;
import com.inspectime.application.client.gui.project.ProjectTable;
import com.inspectime.application.client.gui.userGroup.UserGroupTable;

/**
 * ProjectUsrGroupEditDialog
 * @author Ponec
 */
public class ProjectUsrGroupEditDialog<CUJO extends CRelProjUsGroup> extends AbstractEditDialog<CUJO> {

    private CProject project;
    private CUserGroup userGroup;

    @Override
    public CUJO createItem() {
        return (CUJO) new CRelProjUsGroup();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreateWidgets(Element parent, int pos) {
        //super.onRender(parent, pos);
        setClosable(true);
        setModal(true);
        setHeading(newState
            ? "New Relation: Project - User Group"
            : "Edit Relation: Project - User Group"
            );
        setWidth(500);
        setHeight(350);
        setLayout(new FillLayout());

        panel = new FormPanel();
        panel.setHeaderVisible(false);
        panel.setFieldWidth(300);
        panel.setLabelWidth(120);
        panel.setLabelAlign(LabelAlign.RIGHT);
        panel.setButtonAlign(HorizontalAlignment.RIGHT);

        createWidgetRelation(CRelProjUsGroup.project, new ProjectTable());
        createWidgetRelation(CRelProjUsGroup.userGroup, new UserGroupTable());

        if (project!=null) {
            Field field = findWidget(CRelProjUsGroup.project);
            field.setValue(project);
            //field.setEnabled(false); // ??
            field.setVisible(false);
        }
        if (userGroup!=null) {
            Field field = findWidget(CRelProjUsGroup.userGroup);
            field.setValue(userGroup);
            //field.setEnabled(false); // ??
            field.setVisible(false);
        }

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

    public void setProject(CProject project) {
        this.project = project;
    }

    public void setUserGroup(CUserGroup userGroup) {
        this.userGroup = userGroup;
    }


}
