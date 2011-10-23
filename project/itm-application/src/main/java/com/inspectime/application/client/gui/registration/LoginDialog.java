/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */
package com.inspectime.application.client.gui.registration;

import com.google.gwt.user.client.Element;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.fx.FxConfig;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.gwtincubator.security.client.SpringLoginHandler;
import com.inspectime.application.client.Application;
import com.inspectime.application.client.ao.CUserContext;
import com.inspectime.application.client.service.CParam4User;
import com.inspectime.application.client.controller.UserControllerAsync;
import com.inspectime.application.client.gui.UIManager;
import com.inspectime.application.client.ao.CRoleEnum;
import com.inspectime.application.client.service.CParam4Company;
import com.inspectime.application.client.service.CParam4System;
import java.util.Date;
import org.ujorm.gxt.client.ClientCallback;
import org.ujorm.gxt.client.commons.KeyCodes;
import org.ujorm.gxt.client.tools.MessageDialog;

public class LoginDialog extends LayoutContainer {

    private SpringLoginHandler springLoginHandler;
    private TextField<String> inputLogin;
    private TextField<String> inputPassword;
    private CheckBox checkRememberMe;
    private LayoutContainer loginContainer;
    private LayoutContainer centerContainer;
    private FormPanel loginForm;
    private Label messageLogin;

    @Override
    public void onRender(Element parent, int index) {
        super.onRender(parent, index);

        setLayout(new FitLayout());
        setScrollMode(Scroll.NONE);
        setStyleAttribute("background-color", "white");

        centerContainer = new LayoutContainer(new CenterLayout());
        loginContainer = new LayoutContainer(new BorderLayout());
        loginContainer.setStyleAttribute("background-color", "white");
        loginContainer.setSize(350, 250);

        if (true) {
            ContentPanel cp = new ContentPanel();
            cp.setBodyStyleName("largeLogo");
            cp.setHeaderVisible(false);
            cp.setBodyBorder(false);
            loginContainer.add(cp, new BorderLayoutData(LayoutRegion.NORTH, 100f));
        } else {
            final Label labelHeader = new Label("The Inspectime");
            labelHeader.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
            loginContainer.add(labelHeader, new BorderLayoutData(LayoutRegion.NORTH, 30));
        }

        loginForm = createLoginForm();
        loginContainer.add(loginForm, new BorderLayoutData(LayoutRegion.CENTER));

        centerContainer.add(loginContainer);

        add(centerContainer);
    }

    private FormPanel createLoginForm() {
        FormPanel loginPanel = new FormPanel();

        //loginPanel.setHeading("Login Form");
        loginPanel.setHeaderVisible(false);
        loginPanel.setFrame(true);
        //loginPanel.setWidth(250);

        inputLogin = new TextField<String>();
        inputLogin.setFieldLabel("Email (login)");
        inputLogin.setAllowBlank(false);
        inputLogin.setValidationDelay(500);
        inputLogin.focus();
        loginPanel.add(inputLogin);

        inputPassword = new TextField<String>();
        inputPassword.setPassword(true);
        inputPassword.setFieldLabel("Password");
        inputPassword.setAllowBlank(false);
        loginPanel.add(inputPassword);

        checkRememberMe = new CheckBox();
        checkRememberMe.setLabelSeparator("");
        checkRememberMe.setBoxLabel("Remember me");
        checkRememberMe.setValue(false);
        loginPanel.add(checkRememberMe);

        messageLogin = new Label();
        messageLogin.addStyleDependentName("message");
        messageLogin.setVisible(false);
        loginPanel.add(messageLogin);

        // ---------------------------------------

        final Button buttonLogin = new Button("Login");
        buttonLogin.setTitle("Submit input fields to log-in the application");
        buttonLogin.setId("loginButton");
        buttonLogin.setIcon(Application.ICONS.ok());
        loginPanel.addButton(buttonLogin);
        loginPanel.setButtonAlign(HorizontalAlignment.CENTER);
        buttonLogin.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                doLogin();
            }
        });

        // ---------------------------------------------



        final Button buttonRegistration = new Button("Registration");
        buttonRegistration.setTitle("Create a new account (include new company)");
        buttonRegistration.setIcon(Application.ICONS.user());
        buttonRegistration.setWidth(110);
        loginPanel.addButton(buttonRegistration);
        buttonRegistration.setType("button");
        buttonRegistration.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                doRegister();
            }
        });


        // ----------------------------------------------


        inputLogin.addKeyListener(new KeyListener() {

            @Override
            public void componentKeyPress(ComponentEvent event) {
                if (event.getKeyCode() == KeyCodes.ENTER) {
                    inputPassword.focus();
                }
                super.componentKeyPress(event);
            }
        });
        inputPassword.addKeyListener(new KeyListener() {

            @Override
            public void componentKeyPress(ComponentEvent event) {
                if (event.getKeyCode() == KeyCodes.ENTER) {
                    doLogin();
                }
                super.componentKeyPress(event);
            }
        });
        return loginPanel;
    }

    private void doLogin() {
        getSpringLoginHandler().onClick(null);
    }

    /** Show the registration form */
    private void doRegister() {
        UIManager.getInstance().gotoState(UIManager.STATE_REGISTRATION);
    }

    private SpringLoginHandler getSpringLoginHandler() {

        if (springLoginHandler == null) {
            springLoginHandler = new SpringLoginHandler() {

                @Override
                public String getLogin() {
                    return inputLogin.getValue();
                }

                @Override
                public String getPassword() {
                    return inputPassword.getValue();
                }

                @Override
                public boolean getRememberMe() {
                    return checkRememberMe.getValue();
                }

                @Override
                public void onSuccessLogin() {
                    long duration = 1000L * 60L * 60L * 24L * 365L; // 1 year in ms
                    Date expires = new Date(System.currentTimeMillis() + duration);
                    System.out.println("Expires on="+expires);
                    if (getRememberMe()) {
                        Cookies.setCookie("ilogin", getLogin(), expires, null, "/", false);
                        Cookies.setCookie("ipass", getPassword(), expires, null, "/", false);
                    } else {
                        Cookies.setCookie("ilogin", "", expires, null, "/", false);
                        Cookies.setCookie("ipass", "", expires, null, "/", false);
                    }


                    loadServerUserContext(new Runnable() {

                        @Override
                        public void run() {
                            checkTermOfUse();
                        }
                    });
                }

                @Override
                public void onFailureLogin(Request request) {
                    messageLogin.setText("Invalid username or password. Try it again.");
                    messageLogin.setVisible(true);
                    loginForm.el().blink(FxConfig.NONE);
                    new Timer() {

                        @Override
                        public void run() {
                            inputPassword.selectAll();
                            inputPassword.focus();
                        }
                    }.schedule(1000);
                }

                @Override
                public void onErrorLogin(Throwable exception) {
                    messageLogin.setText("Error while connection to server.");
                    messageLogin.setVisible(true);

                }
            };
        }
        return springLoginHandler;
    }

    /** Check a Term of the use */
    private void checkTermOfUse() {
        UserControllerAsync.Util.getInstance().isUserAgreemnt(new Date(), new ClientCallback<Boolean>(UIManager.getInstance()) {

            @Override
            public void onSuccess(Boolean agreement) {
                if (agreement) {
                    UIManager.getInstance().gotoState(UIManager.STATE_EVENT);
                } else {
                    checkTermOfUseDialog();
                }
            }
        });
    }

    /** Check a Term of the use */
    private void checkTermOfUseDialog() {

        final TermDialog dialog = new TermDialog("Agreement is required");
        dialog.setButtons(MessageDialog.OKCANCEL);
        dialog.addListener(Events.BeforeHide, new Listener<WindowEvent>() {

            @Override
            @SuppressWarnings("unchecked")
            public void handleEvent(WindowEvent be) {
                boolean ok = dialog.isClickedOk(be);
                if (ok) {
                    UIManager.getInstance().gotoState(UIManager.STATE_EVENT);
                    UserControllerAsync.Util.getInstance().sendUserAgreemnt(new ClientCallback<Boolean>(UIManager.getInstance()) {

                        @Override
                        public void onSuccess(Boolean agreement) {
                        }
                    });
                }
            }
        });
        dialog.show();
    }

    /** Load parameters */
    public static void loadServerUserContext(final Runnable runnable) {

        // Load the user session:
        UserControllerAsync.Util.getInstance().getUserContext(new ClientCallback<CUserContext>(UIManager.getInstance()) {

            @Override
            protected void onAnotherException(Throwable e) {
                super.onOtherException(e);
                if (runnable != null) {
                    runnable.run();
                }
            }

            @Override
            public void onSuccess(CUserContext result) {
                GWT.log("User Context Loaded ...");
                //
                CParam4System.getInstance().saveAllParameters(result.getSystemParams());
                CParam4Company.getInstance().saveAllParameters(result.getCompanyParams());
                CParam4User.getInstance().saveAllParameters(result.getUserParams());
                CParam4User.getInstance().setLogin(result.getUserLogin());
                CParam4User.getInstance().setCompany(result.getCompanyName());
                CParam4User.getInstance().setRoles(CRoleEnum.parseString(result.getRoles()));

                if (runnable != null) {
                    runnable.run();
                }
            }
        });

    }
}
