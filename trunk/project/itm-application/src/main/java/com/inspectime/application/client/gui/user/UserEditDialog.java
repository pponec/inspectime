/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.gui.user;

import com.inspectime.application.client.cbo.CUser;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.google.gwt.user.client.Element;
import com.inspectime.application.client.ao.CTimeZone;
import com.inspectime.application.client.clientTools.AbstractEditDialog;
import org.ujorm.gxt.client.CEnum;
import org.ujorm.gxt.client.ClientClassConfig;

/**
 * UserEditDialog
 * @author Hampl,Ponec
 */
public class UserEditDialog<CUJO extends CUser> extends AbstractEditDialog<CUJO> {

    public static final String EMAIL_REGEXP = "^[\\w\\.=-]+@[\\w\\.-]+\\.[\\w]{2,3}$";

    public static final boolean SPRING_CREDENTIALS_SUPPORTED = false;
    public static final boolean LOGIN_VISIBLE = false;

    private ComboBox<CTimeZone> timeZoneWidget = null;

    @Override
    public CUJO createItem() {
        return (CUJO) new CUser().init();
    }

    @Override
    protected void onCreateWidgets(Element parent, int pos) {
        //super.onRender(parent, pos);
        setHeading(newState ? "New User" : "Edit User");
        setWidth(500);
        setHeight(420);

        panel = new FormPanel();
        panel.setHeaderVisible(false);
        panel.setFieldWidth(300);
        panel.setLabelWidth(120);
        panel.setLabelAlign(LabelAlign.RIGHT);
        panel.setButtonAlign(HorizontalAlignment.RIGHT);

        createWidget(CUser.email);
        createWidget(CUser.login).setVisible(LOGIN_VISIBLE);
        createWidget(CUser.name);
        createWidget(CUser.pid, null, "Personal ID");
        ((TextField)createWidget(CUser.passwordNative)).setAllowBlank(!newState);
        createWidget(CUser.workFundStafPerWeek);
        // panel.add(timeZoneWidget=getTimeZoneWidget());
        createWidget(CUser.description, createTextArea(80, true));
     // createWidgetRelation(CUser.userGroup, new UserGroupTable()); // Not supported now
        createWidget(CUser.roles, new RoleFileld(UserTable.EDITABLE_ROLES)).setFieldLabel("Access rights");
        createWidget(CUser.enabled);

        if (SPRING_CREDENTIALS_SUPPORTED) {
            createWidget(CUser.accountNonExpired);      // Document it first !
            createWidget(CUser.accountNonLocked);       // Document it first !
            createWidget(CUser.credentialsNonExpired);  // Document it first !
        }

        // --- Button(s) -----
        add(panel);

        final Button update = newOkButton(newState);
        panel.addButton(update);
        createQuitButton(panel);


        // --- Initializaton -----

        if (newState) {
            cujo.set(CUser.timeZone, "B");
        }

        TextField<String> pwd = findField(CUser.passwordNative);
        pwd.setMinLength(4);
        pwd.setMaxLength(80);

        TextField<String> email = findField(CUser.email);
        email.setRegex(EMAIL_REGEXP);
        email.setValidator(new Validator() {
            @Override public String validate(Field<?> field, String value) {
                if (newState) {
                    String email = (String) field.getValue();
                    binding.get(CUser.login).setValue(email);
                }
                return null;
            }
        });

        update.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                onSubmit();
            }
        });

    }

    /** Returns TimeZone */
    static public ComboBox<CTimeZone> getTimeZoneWidget() {
        ComboBox<CTimeZone> result = new ComboBox<CTimeZone>();
        result.setEditable(false);

        ListStore<CEnum> store = new ListStore<CEnum>();
        store.add(ClientClassConfig.getEnumItems(CTimeZone.getSimpleClassName()));

        result.setName(CTimeZone.getSimpleClassName());
        result.setDisplayField(CTimeZone.name.getName());
        result.setTriggerAction(TriggerAction.ALL);
        result.setFieldLabel("Time Zone");
        result.setStore( (ListStore<CTimeZone>)(Object) store);
        
        return result;
    }

    @Override
    protected boolean copyValuesFromComponent() {
        boolean result = super.copyValuesFromComponent();
        if (timeZoneWidget!=null) {
            cujo.set(CUser.timeZone, ""+timeZoneWidget.getValue().getRawZone());
        }
        if (!LOGIN_VISIBLE) {
            cujo.set(CUser.login, CUser.email.getValue(cujo));
        }
        return result;
    }

    @Override
    protected boolean copyValuesToComponent() {
        if (timeZoneWidget != null) {
            timeZoneWidget.setValue(new CTimeZone(cujo.get(CUser.timeZone)));
        }
        return super.copyValuesToComponent();
    }


}
