/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.gui.registration;

import com.google.gwt.user.client.Element;
import com.inspectime.application.client.gui.*;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
//import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.inspectime.application.client.Application;
import com.inspectime.application.client.ao.CTimeZone;
import com.inspectime.application.client.cbo.CCompany;
import com.inspectime.application.client.cbo.CUser;
import com.inspectime.application.client.controller.UserControllerAsync;
import com.inspectime.application.client.gui.user.UserEditDialog;
import com.inspectime.application.client.service.CParam4System;
import java.util.HashMap;
import java.util.Map;
import org.ujorm.gxt.client.ClientCallback;
import org.ujorm.gxt.client.CujoProperty;
import org.ujorm.gxt.client.tools.MessageDialog;
import com.extjs.gxt.ui.client.widget.HtmlContainer;
import com.extjs.gxt.ui.client.widget.layout.BoxLayout.BoxLayoutPack;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout.HBoxLayoutAlign;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.inspectime.application.client.ao.CRoleEnum;
import com.inspectime.application.client.gui.user.UserTable;
import org.ujorm.gxt.client.CProperty;
//import com.inspectime.commons.bo.item.TimeZone;

public class RegistrationDialog extends LayoutContainer {

    /** Default the Term of Use link */
    public static final String DEFAULT_TERM_OF_USE_LINK = "http://www.inspectime.com/en/terms";

    private Map<CujoProperty, Field> binding = new HashMap<CujoProperty, Field>();
    private LayoutContainer loginContainer;
    private LayoutContainer centerContainer;
    private FormPanel loginForm;
    private TextField<String> sureName;
    private CheckBox agreement;

    /** Bind Key with a Field. */
    public Field bind(CujoProperty property, Field field) {
        if (property!=null) {
            binding.put(property, field);
        }
        loginForm.add(field);
        return field;
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        setLayout(new FitLayout());
        setScrollMode(Scroll.NONE);
        setStyleAttribute("background-color", "white");

        centerContainer = new LayoutContainer(new CenterLayout());

        loginContainer = new LayoutContainer(new BorderLayout());
        loginContainer.setStyleAttribute("background-color", "white");
        loginContainer.setSize(400, 300);

        if (false) {
            ContentPanel cp = new ContentPanel();
            cp.setBodyStyleName("largeLogo");
            cp.setHeaderVisible(false);
            cp.setBodyBorder(false);
            loginContainer.add(cp, new BorderLayoutData(LayoutRegion.NORTH, 80f));
        }

        loginForm = createLoginForm();
        loginContainer.add(loginForm, new BorderLayoutData(LayoutRegion.CENTER));
        //loginContainer.add(createTermPanel(), new BorderLayoutData(LayoutRegion.SOUTH)); // TODO
        centerContainer.add(loginContainer);
        add(centerContainer);
    }

    /** Term of use: */
    private LayoutContainer createTermPanel() {
        LayoutContainer result = new LayoutContainer();

        HBoxLayout layout = new HBoxLayout();
        layout.setPadding(new Padding(1));
        layout.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);
        layout.setPack(BoxLayoutPack.CENTER);

        result.setLayout(layout);
        result.setStyleName("termsPane");
        result.setHeight(15);

        HtmlContainer terms = new HtmlContainer("<a target='_blank' href='" + DEFAULT_TERM_OF_USE_LINK + "'>Read carefully the terms of use.</a>");
        result.add(terms, new HBoxLayoutData(new Margins(0)));
        return result;
    }

    private FormPanel createLoginForm() {
        FormPanel registrationPanel = new FormPanel();
        this.loginForm = registrationPanel;

        registrationPanel.setHeading("The Inspectime user registration");
        registrationPanel.setIcon(Application.ICONS.logo16());
        registrationPanel.setFrame(true);
        registrationPanel.setWidth(350);
        registrationPanel.setLabelWidth(130);

        TextField<String> field = new TextField<String>();
        field.setFieldLabel("User email (login)");
        field.setAllowBlank(false);
        field.focus();
        bind(CUser.email, field);
        field.setRegex(UserEditDialog.EMAIL_REGEXP);
        field.getMessages().setRegexText("The email must have format: user@domain.xyz");
//        field.setValidator(new Validator() {
//            @Override public String validate(Field<?> field, String value) {
//                initTimeZone();
//                String email = (String) field.getValue();
//                binding.get(CUser.login).setValue(email);
//                return null;
//            }
//        });

//      /* Login */
//      field = new TextField<String>();
//      field.setFieldLabel("User login");
//      field.setAllowBlank(false);
//      //field.focus();
//      bind(CUser.login, field);
//      field.setMinLength(4);
//      field.setMaxLength(80);
//      field.setEnabled(CParam4System.getInstance().isDebug());

        field = new TextField<String>();
        field.setPassword(true);
        field.setFieldLabel("Password");
        field.setAllowBlank(false);
        bind(CUser.passwordNative, field);
        field.setMinLength(4);
        field.setMaxLength(80);

        field = new TextField<String>();
        field.setPassword(true);
        field.setFieldLabel("Re-type password");
        field.setAllowBlank(false);
        bind(null, field);
        field.setMinLength(4);
        field.setMaxLength(80);
        field.setValidator(new Validator() {
            @Override public String validate(Field<?> field, String value) {
                String template = (String) binding.get(CUser.passwordNative).getValue();
                String result = template.equals(value) ? null : "The password must be the same";
                return result;
            }
        });
        registrationPanel.addText("&nbsp;");

        field = new TextField<String>();
        field.setFieldLabel("Firstname");
        field.setAllowBlank(false);
        bind(CUser.name, field);
        field.setMinLength(2);
        field.setMaxLength(80);


        sureName = new TextField<String>();
        sureName.setFieldLabel("Lastname");
        sureName.setAllowBlank(false);
        bind(null, sureName);
        sureName.setMinLength(2);
        sureName.setMaxLength(80);

//        /* Time Zone */
//        ComboBox timeZone = UserEditDialog.getTimeZoneWidget();
//        //timeZone.setValue(new CTimeZone());
//        bind(CUser.timeZone, timeZone);

        // Bot field:
        bind(new CProperty<CUser,String>("BotField", "".getClass(), "", -11), new BotField());

        agreement = new LinkCheckBox(CParam4System.getInstance().getTermsOfUseLink(), true);
        agreement.setLabelSeparator("");
        agreement.setFieldLabel(""); // "User Agreement"
        agreement.setBoxLabel("I agree with the ");
        agreement.setValue(false);
        //agreement.setTemplate();
        registrationPanel.add(agreement);
        agreement.addListener(Events.AfterEdit, new Listener<BaseEvent>() {
            @Override public void handleEvent(final BaseEvent be) {
                invalidAgreement(!agreement.getValue());
            }
        });

        // ================================

        final Button registrerButton = new Button("Submit");
        registrerButton.setIcon(Application.ICONS.ok());
        registrationPanel.addButton(registrerButton);
        registrationPanel.setButtonAlign(HorizontalAlignment.CENTER);

        registrerButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                doRegister();
            }
        });

        final Button cancelButton = new Button("Cancel");
        cancelButton.setIcon(Application.ICONS.delete());
        cancelButton.setType("button");
        registrationPanel.addButton(cancelButton);

        cancelButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                  UIManager.getInstance().gotoState(UIManager.STATE_LOGIN);
            }
        });

        return registrationPanel;
    }

    /** Init time zone must be lazy */
    private void initTimeZone() {
//        Field timeZone = binding.get(CUser.timeZone);
//        if (timeZone.getValue()==null) {
//            timeZone.setValue(new CTimeZone());
//        }
    }

    /** Set Invalid Agreement */
    private void invalidAgreement(boolean invalid) {
        if (invalid) {
            String msg = "You must agree with terms.";
            agreement.markInvalid(msg);
        } else {
            agreement.clearInvalid();
        }
    }

    private void doRegister() {
        if (!agreement.getValue()) {
            invalidAgreement(true);
            new MessageDialog("You must agree with terms.").show();
            return;
        }

        UserControllerAsync service = UserControllerAsync.Util.getInstance();
        CUser user = copyFormValuesToUser();
        if (user==null) { return; } // Invalid form data:
        CCompany company = user.getCompany();

        service.registerUser(user, company, new ClientCallback<String>(UIManager.getInstance()) {

            @Override
            public void onSuccess(String msg) {
                if ("".equals(msg)) {
                    showDialogOk();
                } else {
                    showErrorDialog(msg);
                }
            }
        });
    }

    private void showDialogOk() {
        String msg = "You registration is finished, log in to your account, please.";
        MessageDialog dialog = new MessageDialog(msg) {

            @Override
            protected void onButtonPressed(Button button) {
                super.onButtonPressed(button);
                UIManager.getInstance().gotoState(UIManager.STATE_LOGIN);
            }
        };
        dialog.show();
    }

    private void showErrorDialog(String msg) {
        new MessageDialog(msg).show();
    }

    /** Load data from Form to User */
    private CUser copyFormValuesToUser() {

        CUser user = new CUser().init();
        user.set(CUser.company, new CCompany().init());

        for (CujoProperty property : binding.keySet()) {
            Field field = binding.get(property);
            field.validate();
            if (field.isValid()) {
                if (property==CUser.timeZone) {
                    property.setValue(user, "" + ((CTimeZone)field.getValue()).getRawZone());
                } else {
                    property.setValue(user, field.getValue());
                }
            } else {
                return null;
            }
        }

        // Copy Email to Login:
        if (user.getLogin()==null) {
            user.set(CUser.login, user.getEmail());
        }

        // Set Time Zone:
        if (user.getTimeZone()==null) {
            user.set(CUser.timeZone,"A");
        }

        // Surename + FirstName:
        user.set(CUser.name, sureName.getValue() + " " + user.getName());

        // Set all editable user roles:
        user.set(CUser.roles, CRoleEnum.getRoles(UserTable.EDITABLE_ROLES));

        return user;
    }


}
