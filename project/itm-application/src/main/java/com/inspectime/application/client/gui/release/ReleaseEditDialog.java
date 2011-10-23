/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.gui.release;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import com.google.gwt.user.client.Element;
import com.inspectime.application.client.cbo.CProject;
import com.inspectime.application.client.cbo.CRelease;
import com.inspectime.application.client.clientTools.AbstractEditDialog;
import com.inspectime.application.client.gui.project.ProjectTable;

/**
 * ReleaseEditDialog
 * @author Ponec
 */
public class ReleaseEditDialog<CUJO extends CRelease> extends AbstractEditDialog<CUJO> {

    private CProject project;

    @Override
    public CUJO createItem() {
        return (CUJO) new CRelease();
    }

    public void setProject(CProject projectFilter) {
        if (projectFilter!=null) {
            this.project = projectFilter;
            cujo.set(cujo.project, project);
        }
    }


    @Override
    protected void onCreateWidgets(Element parent, int pos) {
        //super.onRender(parent, pos);
        setClosable(true);
        setModal(true);
        setHeading(newState ? "New Release" : "Edit Release");
        setWidth(500);
        setHeight(350);
        setLayout(new FillLayout());

        panel = new FormPanel();
        panel.setHeaderVisible(false);
        panel.setFieldWidth(300);
        panel.setLabelWidth(120);
        panel.setLabelAlign(LabelAlign.RIGHT);
        panel.setButtonAlign(HorizontalAlignment.RIGHT);

        createWidget(CRelease.name);
        createWidget(CRelease.releaseDate);
        createWidget(CRelease.description);
        if (project==null) createWidgetRelation(CRelease.project, new ProjectTable());


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
