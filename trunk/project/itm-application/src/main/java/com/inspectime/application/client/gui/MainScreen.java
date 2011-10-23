/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */


package com.inspectime.application.client.gui;

import com.extjs.gxt.ui.client.event.DomEvent;
import com.google.gwt.user.client.Event;
import org.ujorm.gxt.client.tools.Tools;
import com.google.gwt.core.client.GWT;
import java.util.Date;
import org.ujorm.gxt.client.gui.TablePanel;
import com.inspectime.application.client.gui.report.ReportTable;
import com.inspectime.application.client.gui.eventLock.EventLockTable;
import com.inspectime.application.client.service.CParam4User;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.widget.layout.BoxLayout.BoxLayoutPack;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout.HBoxLayoutAlign;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.google.gwt.user.client.Element;
import com.inspectime.application.client.gui.liveEvent.LiveEventTable;
import com.inspectime.application.client.gui.customer.CustomerTable;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.inspectime.application.client.Application;
import com.inspectime.application.client.ClientContext;
import com.inspectime.application.client.controller.UserControllerAsync;
import com.inspectime.application.client.gui.account.AccountTable;
import com.inspectime.application.client.gui.event.EventTable;
import com.inspectime.application.client.gui.paramOfCompany.ParamCompTable;
import com.inspectime.application.client.gui.paramOfUser.ParamUsrTable;
import com.inspectime.application.client.gui.product.ProductTable;
import com.inspectime.application.client.gui.project.ProjectTable;
import com.inspectime.application.client.gui.task.TaskTable;
import com.inspectime.application.client.gui.user.UserTable;
import com.inspectime.application.client.gui.userTask.UserTaskTable;
import java.util.HashMap;
import static com.inspectime.application.client.gui.UIManager.*;
import static com.inspectime.application.client.ao.CRoleEnum.*;

public class MainScreen extends LayoutContainer {

    private TabPanel tabPanel;
    private LiveEventTable liveEventTable;
    private HashMap<String, TabItem> tabItems = new HashMap<String, TabItem>(5);

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        //setLayout(new FitLayout());
        setLayout(new BorderLayout());
        setStyleAttribute("padding", "1px");
        setScrollMode(Scroll.NONE);
        setStyleAttribute("background-color", "white");

        createHeader(this);
        createBody(this);
    }

    /** Add the header title. */
    private void createHeader(LayoutContainer mainPanel) {

        LayoutContainer c = new LayoutContainer();
        BorderLayoutData northData = new BorderLayoutData(LayoutRegion.NORTH, 18);
        northData.setCollapsible(!true);
        northData.setFloatable(!true);
        northData.setHideCollapseTool(true);
        northData.setSplit(!true);
        northData.setMargins(new Margins(1));
        if (!false) {
            mainPanel.add(c, northData); // TODO (?)
        }         //
        HBoxLayout layout = new HBoxLayout();
        layout.setPadding(new Padding(0));
        layout.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);
        layout.setPack(BoxLayoutPack.END);
        c.setLayout(layout);

        HBoxLayoutData layoutData = new HBoxLayoutData(new Margins(0, 5, 0, 0));
        Label userLabel = new Label(CParam4User.getInstance().getLogin());
        userLabel.setStyleName("loginInfo");
        c.add(userLabel, layoutData);
        
        Label logoutLink = new Label("logout");
        logoutLink.setStyleName("loginInfo");
        logoutLink.setStyleAttribute("text-decoration","underline");
        logoutLink.setStyleAttribute("color","blue");
        logoutLink.setStyleAttribute("cursor","pointer");
        final Label labelL1 = new Label(" (");
        labelL1.setStyleName("loginInfo");
        final Label labelL2 = new Label(")");
        labelL2.setStyleName("loginInfo");
        c.add(labelL1);
        c.add(logoutLink);
        c.add(labelL2);
        
        logoutLink.sinkEvents(Event.ONCLICK);
        logoutLink.addListener(Events.OnClick, new Listener<DomEvent>() {
        
            @Override
            public void handleEvent(DomEvent be) {

                UserControllerAsync.Util.getInstance().logout(new Date(), new AsyncCallback<Boolean>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        GWT.log("Logout failed");
                        logout();
                    }

                    @Override
                    public void onSuccess(Boolean result) {
                        logout();
                    }

                    private void logout() {
                        UIManager.clearLoginCookies();
                        UIManager.getInstance().forceLogin = true;
                        UIManager.getInstance().gotoState(UIManager.STATE_LOGIN);
                    }
                });
            }
        });    
    }

    private void createBody(LayoutContainer mainPanel) {
        tabPanel = new TabPanel();
        tabPanel.setId("mainTabPanel");
        tabPanel.setPlain(true);
        tabPanel.setTabScroll(true);
        tabPanel.setAnimScroll(true);

        BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER);
        centerData.setMargins(new Margins(0));

        mainPanel.add(tabPanel, centerData);
        //
        newTabItem(STATE_EVENT, new EventTable(), Application.ICONS.time());
        newTabItem(STATE_LIVE, new LiveEventTable().setRoles(MANAGER), Application.ICONS.live());
        newTabItem(STATE_REPORT, new ReportTable().setRoles(MANAGER), Application.ICONS.chart());
        newTabItem(STATE_USER_TASK, new UserTaskTable(), Application.ICONS.star());
        newTabItem(STATE_TASK, new TaskTable().setRoles(MANAGER), Application.ICONS.task());
        //newTabItem(STATE_RELEASE, new ReleaseTable()Application.ICONS.edit());
        newTabItem(STATE_PROJECT, new ProjectTable().setRoles(MANAGER), Application.ICONS.project());
        newTabItem(STATE_PRODUCT, new ProductTable().setRoles(MANAGER), Application.ICONS.product());
        //newTabItem(STATE_PRODUCT, new ProductTable(), Application.ICONS.edit());
        newTabItem(STATE_ACCOUNT, new AccountTable().setRoles(MANAGER), Application.ICONS.account());
        newTabItem(STATE_CUSTOMER, new CustomerTable().setRoles(MANAGER), Application.ICONS.customer());
        //newTabItem(STATE_USER_GROUP, new UserGroupTable(), Application.ICONS.edit());
        newTabItem(STATE_USER, new UserTable().setRoles(ADMIN), Application.ICONS.user());
        newTabItem(STATE_LOCK, new EventLockTable()/*.setRoles(ADMIN)*/.setModifyRoles(ADMIN), Application.ICONS.unlock());
        newTabItem(STATE_PARAM2USER, new ParamUsrTable(), Application.ICONS.params());
        newTabItem(STATE_PARAM2COMP, new ParamCompTable().setRoles(MANAGER, ADMIN), Application.ICONS.params());
        // tabCustomer = newTabItem("Company", new CompanyTable(), Application.ICONS.edit());


        // ---------------------------

        liveEventTable = ((LiveEventTable) tabItems.get(STATE_LIVE).getWidget(0));

        // ======== LISTENERS ========

        tabItems.get(STATE_EVENT).addListener(Events.Select, new Listener<ComponentEvent>() {

            @Override
            public void handleEvent(ComponentEvent be) {
                if (ClientContext.getInstance().isRefreshEventTableRequest()) {
                    ((EventTable) tabItems.get(STATE_EVENT).getWidget(0)).reloadTable();
                }
                if (ClientContext.getInstance().isRefreshHotKeysRequest()) {
                    ((EventTable) tabItems.get(STATE_EVENT).getWidget(0)).initHotKeys();
                }
            }
        });

        tabItems.get(STATE_LOCK).addListener(Events.Select, new Listener<ComponentEvent>() {

            @Override
            public void handleEvent(ComponentEvent be) {
                if (ClientContext.getInstance().isRefreshLockRequest()) {
                    TabItem tabItem = tabItems.get(STATE_LOCK);
                    if (tabItem != null) {
                        ((EventLockTable) tabItem.getWidget(0)).reloadTable();
                    }
                }
            }
        });

        tabItems.get(STATE_LIVE).addListener(Events.Select, new Listener<ComponentEvent>() {

            @Override
            public void handleEvent(ComponentEvent be) {
                liveEventTable.onSelectLiveEventTable();
            }
        });

    }

    /** Register an tab event */
    private void registerEventHandler(final TabItem tab, final String newState) {

        tabItems.put(newState, tab);

        if (tab != null) {
            tab.addListener(Events.Select, new Listener<ComponentEvent>() {

                @Override
                public void handleEvent(ComponentEvent be) {
                    String oldState = History.getToken();
                    System.out.println("old=" + oldState);
                    System.out.println("new=" + newState);
                    if (!newState.equals(oldState)) {
                        History.newItem(newState, false);
                    }
                }
            });
        }
    }

    /** Create new TabItem. */
    private TabItem newTabItem(String title, TablePanel tablePanel, AbstractImagePrototype icon) {
        boolean displayAllowed = tablePanel.istDisplayAllowed(CParam4User.getInstance().getRoles());

        TabItem result = new TabItem(Tools.getLabel(title));
        if (icon != null) {
            result.setIcon(icon);
        }
        result.setLayout(new FitLayout());
        result.add(tablePanel);
        tablePanel.setTitle(null);

        if (displayAllowed) {
            tabPanel.add(result);
        } else {
            result.setVisible(displayAllowed);
        }

        registerEventHandler(result, title);
        return result;
    }

    /** Create new TabItem. */
    private TabItem newTabItem(String title, LayoutContainer container, AbstractImagePrototype icon) {
        TabItem result = new TabItem(title);
        if (icon != null) {
            result.setIcon(icon);
        }
        result.setLayout(new FitLayout());
        result.add(container);
        container.setTitle(null);
        tabPanel.add(result);

        registerEventHandler(result, title);
        return result;
    }

    public void setState(String newState) {
        TabItem panel = tabItems.get(newState);
        if (panel != null) {
            tabPanel.setSelection(panel);
        }
    }
}
