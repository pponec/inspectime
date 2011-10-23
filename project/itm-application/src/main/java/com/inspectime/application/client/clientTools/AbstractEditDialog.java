/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */


package com.inspectime.application.client.clientTools;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.inspectime.application.client.gui.UIManager;
import org.ujorm.gxt.client.CLoginRedirectable;
import org.ujorm.gxt.client.Cujo;
import org.ujorm.gxt.client.PropertyMetadata;
import org.ujorm.gxt.client.CujoProperty;
import org.ujorm.gxt.client.ao.ValidationMessage;

import org.ujorm.gxt.client.gui.TableEditDialog;
import org.ujorm.gxt.client.tools.ColorGxt;
import org.ujorm.gxt.client.tools.MessageDialog;

/**
 * AbstractEditDialog
 * @author Pavel Ponec
 */
abstract public class AbstractEditDialog<CUJO extends Cujo> extends TableEditDialog<CUJO> {

    /** Create widget by property */
    @SuppressWarnings("unchecked")
    @Override
    protected Field createWidget(CujoProperty<? super CUJO, ?> p, Field widget, String aLabel) {

        if (p.isTypeOf(ColorGxt.class)) {
            PropertyMetadata metadata = getMetadata(p);
            String label = aLabel != null ? aLabel : metadata.getSideLabel();

            ColorField colorField = new ColorField();

            colorField.setName(p.getName());
            colorField.setFieldLabel(label);
            colorField.setToolTip(metadata.getDescription());

            panel.add(colorField);
            bind(p, colorField);
            return colorField;
        } else {
            return super.createWidget(p, widget, aLabel);
        }
    }

    @Override
    protected void copyValueFromComponent(CujoProperty p, Object value) throws Exception {
        if (value!=null && p.isTypeOf(ColorGxt.class)) {
            value = new ColorGxt((String) value);
            cujo.set(p, value);
        } else {
            super.copyValueFromComponent(p, value);
        }
    }

    @Override
    protected void copyValueToComponent(Field w, CujoProperty p, Object value) throws NumberFormatException {
        if (p.isTypeOf(ColorGxt.class)) {
            w.setValue(value!=null ? value.toString() : null);
        } else {
            super.copyValueToComponent(w, p, value);
        }
    }

    @Override
    public void redirectToLogin() {
        UIManager.getInstance().redirectToLogin();
    }

    /** Get object redirectable to login */
    protected CLoginRedirectable redir() {
        return this;
    }

    /** Create the quit panel button */
    protected void createQuitButton(FormPanel panel) {
        final Button quitButton = newQuitButton(newState);
        panel.addButton(quitButton);
        quitButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                hide();
            }
        });
    }

    /** Mark Invalid Field */
    @Override
    protected void markInvalidField(ValidationMessage msg) {
        new MessageDialog("Invalid input: " + msg).show();
        super.markInvalidField(msg);
    }

}
