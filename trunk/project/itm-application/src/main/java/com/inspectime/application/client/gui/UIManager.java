/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.gui;

import com.inspectime.application.client.gui.registration.LoginDialog;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.HistoryListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtincubator.security.client.SpringLoginHandler;
import com.inspectime.application.client.controller.UserControllerAsync;
import com.inspectime.application.client.gui.registration.RegistrationDialog;
import java.util.Date;
import org.ujorm.gxt.client.CLoginRedirectable;
import org.ujorm.gxt.client.ClientCallback;
import org.ujorm.gxt.client.tools.MessageDialog;

/**
 *
 * @author gola
 */
@SuppressWarnings("deprecation")
public class UIManager implements HistoryListener, CLoginRedirectable {

    public static final String STATE_LOGIN = "Login";
    public static final String STATE_REGISTRATION = "Registration";
    public static final String STATE_EVENT = "Activity";
    public static final String STATE_LIVE = "Team";
    public static final String STATE_REPORT = "Reports";
    public static final String STATE_USER_TASK = "Favourites";
    public static final String STATE_TASK = "Tasks";
    public static final String STATE_PROJECT = "Projects";
    public static final String STATE_CUSTOMER = "Customers";
    public static final String STATE_COMPANY = "Company";
    public static final String STATE_PRODUCT = "Products";
    public static final String STATE_USER = "Users";
    public static final String STATE_LOCK = "Approvals";
    public static final String STATE_USER_GROUP = "UserGroup";
    public static final String STATE_PARAM2USER = "Preferences";
    public static final String STATE_PARAM2COMP = "Settings";
    public static final String STATE_RELEASE = "Release";
    public static final String STATE_ACCOUNT = "Accounts";
    // public static final String STATE_HAMPL = "hampl";
    private static UIManager _instance;

    public static UIManager getInstance() {
        if (_instance == null) {
            _instance = new UIManager();
        }
        return _instance;
    }
    public boolean forceLogin = false;
    private Viewport viewport;

    @Override
    public void onHistoryChanged(String historyToken) {
        if (STATE_LOGIN.equals(historyToken)) {

            if (!forceLogin && Cookies.getCookie("ilogin") != null && Cookies.getCookie("ilogin").length() > 0) {
                SpringLoginHandler springLoginHandler = new SpringLoginHandler() {

                    @Override
                    public String getLogin() {
                        return Cookies.getCookie("ilogin");
                    }

                    @Override
                    public String getPassword() {
                        return Cookies.getCookie("ipass");
                    }

                    @Override
                    public boolean getRememberMe() {
                        return true;
                    }

                    @Override
                    public void onSuccessLogin() {
                        long duration = 1000 * 60 * 60 * 24 * 365; // 1 year in ms
                        Date expires = new Date(System.currentTimeMillis() + duration);
                        Cookies.setCookie("ilogin", getLogin(), expires, null, "/", false);
                        Cookies.setCookie("ipass", getPassword(), expires, null, "/", false);

                        LoginDialog.loadServerUserContext(new Runnable() {

                            @Override
                            public void run() {
//                                UIManager.getInstance().gotoState(UIManager.STATE_EVENT);
                                checkTermOfUse();
                            }
                        });
                    }

                    @Override
                    public void onFailureLogin(Request request) {
                        clearLoginCookies();
                        showScreen(new LoginDialog());
                    }

                    @Override
                    public void onErrorLogin(Throwable exception) {
                        clearLoginCookies();
                        showScreen(new LoginDialog());
                    }
                };
                springLoginHandler.onClick(null);
                return;
            }

            forceLogin = false;
            showScreen(new LoginDialog());
            return;
        }
        if (STATE_REGISTRATION.equals(historyToken)) {
            showScreen(new RegistrationDialog());
            return;
        }

        MainScreen ms = new MainScreen();
        ms.setState(historyToken);
        showScreen(ms);

//        if (tabItems!=null && tabItems.get(historyToken)!=null) {
//            MainScreen ms = new MainScreen();
//            ms.setState(historyToken);
//            showScreen(ms);
//        } else {
//            com.google.gwt.user.client.Window.alert(historyToken);
//        }
    }

    private void checkTermOfUse() {
        UserControllerAsync.Util.getInstance().isUserAgreemnt(new Date(), new ClientCallback<Boolean>(UIManager.getInstance()) {

            @Override
            public void onSuccess(Boolean agreement) {
                if (agreement) {
                    UIManager.getInstance().gotoState(UIManager.STATE_EVENT);
                } else {
                    forceLogin = true;
                    clearLoginCookies();
                    showScreen(new LoginDialog());
                }
            }
            
            
            
        });
    }

    public void initHistorySupport() {
        History.addHistoryListener(this);
        // check to see if there are any tokens passed at startup via the browser's URI
        String token = History.getToken();
        if (token.length() == 0) {
            gotoState(STATE_LOGIN);
        } else {
            onHistoryChanged(token);
        }
    }

    public void startup() {
        initHistorySupport();
    }

    public void gotoState(String newState) {
        History.newItem(newState);
    }

    private void showScreen(Widget widget) {
        RootPanel.get().clear();
        viewport = new Viewport();
        viewport.setLayout(new BorderLayout());
        viewport.add(widget, new BorderLayoutData(LayoutRegion.CENTER));
        RootPanel.get().add(viewport);
    }

    public void showRegistrationDialog() {
        final Widget dialog = new RegistrationDialog(); // RegistrationDialog();
        showScreen(dialog);
    }

    /** Redirect to login */
    @Override
    public void redirectToLogin() {
        final String message = "Session is absolete, make new login please.";
        GWT.log(message);
        final MessageDialog d = new MessageDialog(message);
        d.setButtons(Dialog.OK);
        d.addListener(Events.BeforeHide, new Listener<WindowEvent>() {

            @Override
            @SuppressWarnings("unchecked")
            public void handleEvent(WindowEvent be) {
                gotoState(UIManager.STATE_LOGIN);
            }
        });
        d.show();
    }

    public static void clearLoginCookies() {
        long duration = 1000L * 60L * 60L * 24L * 365L; // 1 year in ms
        Date expires = new Date(System.currentTimeMillis() + duration);
        Cookies.setCookie("ilogin", "", expires, null, "/", false);
        Cookies.setCookie("ipass", "", expires, null, "/", false);
    }
}
