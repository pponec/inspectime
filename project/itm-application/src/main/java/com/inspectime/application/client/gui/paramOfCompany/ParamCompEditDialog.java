/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.gui.paramOfCompany;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import com.google.gwt.user.client.Element;
import com.inspectime.application.client.cbo.CSingleComParam;
import com.inspectime.application.client.clientTools.AbstractEditDialog;
import org.ujorm.gxt.client.controller.TableControllerAsync;

/**
 * ParamCompEditDialog
 * @author Hampl,Ponec
 */
public class ParamCompEditDialog<CUJO extends CSingleComParam> extends AbstractEditDialog<CUJO> {

    final static protected TableControllerAsync service = TableControllerAsync.Util.getInstance();

    @Override
    public CUJO createItem() {
        return (CUJO) new CSingleComParam();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreateWidgets(Element parent, int pos) {
        //super.onRender(parent, pos);
        setClosable(true);
        setModal(true);
        setHeading(newState ? "New AppParam" : "Edit AppParam");
        setWidth(500);
        setHeight(350);
        setLayout(new FillLayout());

        panel = new FormPanel();
        panel.setHeaderVisible(false);
        panel.setFieldWidth(300);
        panel.setLabelWidth(120);
        panel.setLabelAlign(LabelAlign.RIGHT);
        panel.setButtonAlign(HorizontalAlignment.RIGHT);

        createWidget(CSingleComParam.value, new TextField<String>());
        createWidget(CSingleComParam.defaultValue, new TextField<String>()).setEnabled(false);
        createWidget(CSingleComParam.description, createTextArea(100, false)).setEnabled(false);
        createWidget(CSingleComParam.type).setEnabled(false);
        createWidget(CSingleComParam.key).setEnabled(false);
        createWidget(CSingleComParam.index).setEnabled(false);
        
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
