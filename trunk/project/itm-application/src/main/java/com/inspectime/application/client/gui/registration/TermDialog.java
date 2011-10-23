/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.gui.registration;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.google.gwt.user.client.Element;
import com.inspectime.application.client.service.CParam4System;
import org.ujorm.gxt.client.commons.Icons;

/**
 * Dialog pro souhlas s podmínkami.
 * @author Hampl
 */
public class TermDialog extends Dialog {

    private static TermDialog instance;
    private String message;
    private CheckBox agreement;

    public TermDialog(String message) {
        this.message = message;
        setButtons(Dialog.OK);
        setStyleName("commonMessageDialog");
    }

    public static TermDialog getInstance(String message) {
        if (instance == null) {
            instance = new TermDialog("");
        }
        instance.setMessage(message);
        instance.setButtons(Dialog.OK);
        return instance;
    }

    public void setMessage(String message) {
        this.message = message;
        if (rendered) {
            addText(message);
            show();
        }
    }

    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);
        setIcon(Icons.Pool.help());
        setHeading(message);
        setBodyStyleName("pad-text");
        setScrollMode(Scroll.AUTO);
        setHideOnButtonClick(true);
        setModal(true);
        setClosable(true);      // TODO: why it does not work ?
        setBlinkModal(true);

        agreement = new LinkCheckBox(CParam4System.getInstance().getTermsOfUseLink(), true);
        agreement.setLabelSeparator("");
        agreement.setFieldLabel(""); // "User Agreement"
        agreement.setBoxLabel("I agree with the ");
        agreement.setValue(false);
        add(agreement);
        //addText(message);
    }

    public boolean isClickedOk(WindowEvent be) {
        return OK.equals(be.getButtonClicked().getItemId()) && agreement.getValue();
    }
}
