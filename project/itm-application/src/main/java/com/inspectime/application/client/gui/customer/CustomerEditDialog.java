/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.gui.customer;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import com.google.gwt.user.client.Element;
import com.inspectime.application.client.cbo.CCustomer;
import com.inspectime.application.client.clientTools.AbstractEditDialog;

/**
 * CustomerEditDialog
 * @author Ponec
 */
public class CustomerEditDialog<CUJO extends CCustomer> extends AbstractEditDialog<CUJO> {

    
    @Override
    public CUJO createItem() {
        return (CUJO) new CCustomer();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreateWidgets(Element parent, int pos) {
        //super.onRender(parent, pos);
        setClosable(true);
        setModal(true);
        setHeading(newState ? "New Customer" : "Edit Customer");
        setWidth(500);
        setHeight(350);
        setLayout(new FillLayout());

        panel = new FormPanel();
        panel.setHeaderVisible(false);
        panel.setFieldWidth(300);
        panel.setLabelWidth(120);
        panel.setLabelAlign(LabelAlign.RIGHT);
        panel.setButtonAlign(HorizontalAlignment.RIGHT);

        createWidget(CCustomer.name);
        createWidget(CCustomer.description);
        createWidget(CCustomer.graphColor);

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
