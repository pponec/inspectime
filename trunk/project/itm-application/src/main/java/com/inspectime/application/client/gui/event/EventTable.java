/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */
package com.inspectime.application.client.gui.event;

import com.extjs.gxt.charts.client.Chart;
import com.extjs.gxt.charts.client.model.ChartModel;
import com.extjs.gxt.charts.client.model.charts.PieChart;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.DelayedTask;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ToggleButton;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout.VBoxLayoutAlign;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayoutData;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.inspectime.application.client.Application;
import com.inspectime.application.client.ClientContext;
import com.inspectime.application.client.ao.CRoleEnum;
import com.inspectime.application.client.ao.WTime;
import com.inspectime.application.client.cbo.CAccount;
import com.inspectime.application.client.cbo.CCustomer;
import com.inspectime.application.client.cbo.CEvent;
import com.inspectime.application.client.cbo.CProject;
import com.inspectime.application.client.cbo.CTask;
import com.inspectime.application.client.cbo.CUser;
import com.inspectime.application.client.clientTools.AbstractEditDialog;
import com.inspectime.application.client.controller.EventControllerAsync;
import com.inspectime.application.client.gui.liveEvent.LiveEventPanel;
import com.inspectime.application.client.gui.project.ProjectTable;
import com.inspectime.application.client.service.CParam4Company;
import com.inspectime.application.client.service.CParam4User;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.ujorm.gxt.client.CProperty;
import org.ujorm.gxt.client.ClientCallback;
import org.ujorm.gxt.client.CujoProperty;
import org.ujorm.gxt.client.ao.ValidationMessage;
import org.ujorm.gxt.client.cquery.CCriterion;
import org.ujorm.gxt.client.cquery.CQuery;
import org.ujorm.gxt.client.gui.OldCujoBox;
import org.ujorm.gxt.client.tools.ColorGxt;
import org.ujorm.gxt.client.tools.MessageDialog;

/**
 *
 * @author Ponec
 */
public class EventTable<CUJO extends CEvent> extends AbstractEventTable<CUJO> {

    public static final String CHART_TITLE_STYLE = "font-size: 12px; font-family: Verdana; text-align: center;";
    private static final long ONE_DAY = 24 * 60 * 60 * 1000;
    public static final DateTimeFormat DEFAULT_DAY_FORMAT = GWT.isClient()
            ? DateTimeFormat.getFormat("yyyy-MM-dd (EEE)")
            : null;
    protected DateField calendar;
    protected String buttonHelp = "Help";
    protected String buttonActions = "Actions";
    protected String buttonScripts = "Scripts";
    protected String buttonNodes = "Nodes";
    private HotButtons hotButtons;
    protected TextField<String> totalTime;
    private DelayedTask noticeTask;
    private DelayedTask refreshTaskTime;
    private Chart chartProjects;
    private LayoutContainer infoProjects;
    private Map<Long, ChartItem> mapProjects;
    private Chart chartAccounts;
    private LayoutContainer infoAccounts;
    private Map<Long, ChartItem> mapAccounts;
    private Chart chartCustomers;
    private LayoutContainer infoCustomers;
    private Map<Long, ChartItem> mapCustomers;
    public static final Integer DEFAUL_NOTICE = 1000 * 60 * 20; // notice ater 20 minutes
    private OldCujoBox<CProject> projectBox;
    private OldCujoBox<CTask> taskBox;
    private ToggleButton lockButton;
    private boolean enablePomodoroDialog = true;
    private ContentPanel cpInfo;
    private int MAX_INFO_ROWS = 3;

    public EventTable(CQuery<CUJO> query) {
        super(query);
    }

    public EventTable() {
    }

    /**
     * Create Chart Item
     */
    protected ChartItem createChartItem(final Long id, final String name, final ColorGxt color) {
        final boolean calcByTime = true;
        return new ChartItem(id, name, color, calcByTime);
    }

//    public EventTable setProject(CProject projectFilter) {
//        if (projectFilter!=null) {
//            this.projectFilter = projectFilter;
//            CCriterion<? super CUJO> crn = CCriterion.where(CEvent.project, projectFilter);
//            addCriterion(crn);
//        }
//        return this;
//    }
    /**
     * Is today ?
     */
    public boolean isToday() {
        try {
            String d1 = DEFAULT_DAY_FORMAT.format(calendar.getValue());
            String d2 = DEFAULT_DAY_FORMAT.format(new java.util.Date());
            return d1.equals(d2);
        } catch (Throwable e) {
            return true;
        }
    }

    /**
     * Create a new instance of the Edit dialog, no initializaton.
     */
    @Override
    protected EventEditDialog createDialogInstance() {
        return new EventEditDialog();
    }

    /**
     * Create a new edit dialog.
     */
    @Override
    @SuppressWarnings("unchecked")
    protected AbstractEditDialog<CUJO> createTableEditDialog(final CUJO selectedItem, boolean newState, boolean clone) {
        final EventEditDialog<CUJO> dialog = createDialogInstance();
        CEvent cujo = newState ? dialog.createItem() : selectedItem;
        if (clone) {
            copy(selectedItem, cujo);
            cujo.set((CProperty) CEvent.startTime_, getNextTime());
        } else if (newState) {
            cujo.set((CProperty) CEvent.day, calendar.getValue());
            cujo.set(CEvent.startTime_, getNextTime());
        }

        dialog.init((CUJO) cujo, newState, getEditQuery(newState, selectedItem));
        return dialog;
    }

    /**
     * Define a list of the Table columns
     */
    @Override
    protected CujoProperty[] createTableColumns() {
        return new CujoProperty[]{CEvent.day, CEvent.startTime_, CEvent.period_, CEvent.project.add(CProject.name), CEvent._task_code, CEvent.description, CEvent.user.add(CUser.login)
        };
    }

    /**
     * Returns selected day
     */
    @Override
    protected Date getSelectedDay() {
        final Date result = calendar != null ? calendar.getValue() : null;
        return result != null ? result : new Date();
    }

    /**
     * Returns a default database query.
     */
    @Override
    protected CQuery<? super CUJO> createDefaultQuery() {
        CQuery<CEvent> result = CQuery.newInstance(CEvent.class, createTableColumns());
        Date selectedDay = calendar != null ? calendar.getValue() : new Date();

        CCriterion<CEvent> crn1 = CEvent.active.whereEq(true);
        CCriterion<CEvent> crn2 = CCriterion.where((CujoProperty) CEvent.day, selectedDay); // Hack: http://code.google.com/p/google-web-toolkit/issues/detail?id=87
        result.setCriterion(crn1.and(crn2));
        result.setRelations(2);

        // GUI:
        //result.getColumnConfig(CEvent.startTime).setHidden(true);
        //result.getColumnConfig(CEvent.period).setHidden(true);
        result.getColumnConfig(CEvent.user.add(CUser.login)).setHidden(true);
        result.getColumnConfig(CEvent.day).setHidden(true);
        result.getColumnConfig(CEvent.startTime_).setWidth(70);
        result.getColumnConfig(CEvent.period_).setWidth(70);
        result.getColumnConfig(CEvent._task_code).setWidth(200);
        result.getColumnConfig(CEvent.day).setHidden(true);

        // Unsortable Columns:
        for (ColumnConfig cc : result.getCujoModel().getColumns()) {
            cc.setSortable(false);
        }

        result.getColumnConfig(CEvent.project.add(CProject.name)).setHeader("Project");
        result.getColumnConfig(CEvent._task_code).setHeader("Task");

        // Assign Renderer:
        GridCellRenderer<CEvent> renderer = createRenderer();
        for (ColumnConfig cc : result.getCujoModel().getColumns()) {
            cc.setRenderer(renderer);
        }

        return result;
    }

    /**
     * Create table renderer
     */
    public static GridCellRenderer<CEvent> createRenderer() {
        return new GridCellRenderer<CEvent>() {
            @Override
            public String render(CEvent event, String property, ColumnData config, int rowIndex, int colIndex, ListStore<CEvent> store, Grid<CEvent> grid) {
                boolean priv = event.getTask().isPrivate();
                boolean looong = event.getPeriod().getMinutes() > 12 * 60; // delsi jak 12h je podezřelé
                Object val = event.get(property);
                // jira regex :)
                if ("description".equals(property) && val instanceof String) {
                    final String jiraServerUrl = CParam4Company.getInstance().getJiraServerUrl();
                    if (jiraServerUrl != null && jiraServerUrl.length() > 0) {
                        String str = (String) val;
                        str = str.replaceAll("([A-Z]{2}\\-[0-9]*)", "<a href=\"" + jiraServerUrl + "browse/$1\" style=\"color:#0000FF\" target=\"_blank\">$1</a>");
                        val = str;
                    }
                }

                String result = "";
                if (looong) {
                    String color = "#808080";
                    result = "<span style='color:" + color + "'>" + (val != null ? val : "") + "</span>";
                } else if (priv) {
                    String color = "#5DA158";
                    result = "<span style='color:" + color + "'>" + (val != null ? val : "") + "</span>";
                } else {
                    result = val != null ? val.toString() : "";
                }
                if (event.getPeriod().isSecondsSupported()) {
                    return "<b>" + result + "</b>";
                } else {
                    return result;
                }
            }
        };
    }

    @Override
    protected void createButtons(final LayoutContainer buttonContainer, final PagingToolBar toolBar) {

        beforeCreateButtons();
        LabelToolItem quickLabel = new LabelToolItem("Quick new event:");
        quickLabel.setStyleName("hotTaskLabel");
        buttonContainer.add(quickLabel, new VBoxLayoutData(new Margins(0, 0, 5, 0)));
        buttonContainer.add(projectBox, new VBoxLayoutData(new Margins(0, 0, 5, 0)));
        buttonContainer.add(taskBox, new VBoxLayoutData(new Margins(0, 0, 15, 0)));

        super.createButtons(buttonContainer, toolBar);

        Button btnHelp = addButton(buttonHelp, Application.ICONS.help(), buttonContainer);
        Button btnActions = null; //addButton(buttonActions, Application.ICONS.list(), buttonContainer);

        // Instalations:
        if (btnHelp != null) {
            btnHelp.addSelectionListener(new SelectionListener<ButtonEvent>() {
                @Override
                public void componentSelected(ButtonEvent ce) {

                    // TODO: shoe an information box:
                    if (true) {
                        MessageDialog.getInstance("See the home page: inspectime.com").show();
                    } else {
                        refreshTotalTime();
                    }
                    return;
                }
            });
        }

        // Action Steps:
        if (btnActions != null) {
            btnActions.addSelectionListener(new SelectionListener<ButtonEvent>() {
                @Override
                public void componentSelected(ButtonEvent ce) {
                    final CUJO selectedItem = getFirstSelectedItem();
                    if (selectedItem == null) {
                        MessageDialog.getInstance("Select one row to update.").show();
                        return;
                    }
//                    final TableListDialog editDialog = createActionListDialog(selectedItem);
//                    editDialog.addListener(Events.BeforeHide, new Listener<WindowEvent>() {
//
//                        @Override
//                        public void handleEvent(WindowEvent be) {
//                            boolean ok = editDialog.isChangedData();
//                            if (ok) {
//                                // gridToolBar.refresh();
//                            }
//                        }
//                    });
//                    editDialog.show();
                }
            });
        }

        // Show the hot task buttons:
        int buttonCount = CParam4User.getInstance().getHotEventButtonMaxCount();
        Label hotTaskLabel = new Label("Favourites:");
        hotTaskLabel.setStyleName("hotTaskLabel");
        buttonContainer.add(hotTaskLabel, new VBoxLayoutData(new Margins(10, 0, 5, 0)));
        String label = " ";
        hotButtons = new HotButtons(this, buttonContainer);
        while (--buttonCount >= 0) {
            hotButtons.addButton(addButton(label, null, buttonContainer));
        }
        hotButtons.init();

    }

    @Override
    protected void onRender(com.google.gwt.user.client.Element parent, int index) {
        super.onRender(parent, index);

        noticeTask = new DelayedTask(new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                if (enablePomodoroDialog) {
                    Window.alert("No action in last " + (getNoticeTime() / 60 / 1000) + " minutes.");
                }
            }
        });

        if (isToolBarEnabled()) {
            refreshTaskTime = new DelayedTask(new Listener<BaseEvent>() {
                @Override
                public void handleEvent(BaseEvent be) {
                    refreshLastTime();
                }
            });
        }

        BorderLayout borderLayout = new BorderLayout();

        ContentPanel evTable = new ContentPanel();
        evTable.setHeaderVisible(false);
        evTable.setLayout(borderLayout);
        evTable.add(grid, new BorderLayoutData(LayoutRegion.CENTER, 0, 0, 0));

        if (this.isToolBarEnabled() && (CParam4User.getInstance() != null && CParam4User.getInstance().getRoles() != null && CParam4User.getInstance().getRoles().contains(CRoleEnum.MANAGER))) {
            evTable.add(new LiveEventPanel().setRoles(CRoleEnum.MANAGER), new BorderLayoutData(LayoutRegion.SOUTH, 220, 0, 0));
        }

        grid.setAutoExpandColumn(getQuery().getColumnConfig(CEvent.description).getId());
        grid.setAutoExpandMax(Integer.MAX_VALUE);

        cpTable.setHeaderVisible(false);
        borderLayout = new BorderLayout();
        cpTable.setLayout(borderLayout);
        cpTable.add(evTable, new BorderLayoutData(LayoutRegion.CENTER, 0, 0, 0));

        cpInfo = new ContentPanel();
        cpInfo.setHeaderVisible(false);
        final VBoxLayout vBoxLayout = new VBoxLayout();
        vBoxLayout.setVBoxLayoutAlign(VBoxLayoutAlign.STRETCH);
        cpInfo.setLayout(vBoxLayout);

        BorderLayoutData left = new BorderLayoutData(LayoutRegion.WEST, 150, 100, 250);
        left.setMargins(new Margins(5));
        cpTable.add(cpInfo, left);

        //String url = !Examples.isExplorer() ? "../../" : "";
        String url = "resources/chart/open-flash-chart.swf";

        chartProjects = new Chart(url);
        chartProjects.setBorders(false);
        chartAccounts = new Chart(url);
        chartAccounts.setBorders(false);
        chartCustomers = new Chart(url);
        chartCustomers.setBorders(false);
        infoProjects = new LayoutContainer();
        infoAccounts = new LayoutContainer();
        infoCustomers = new LayoutContainer();

        VBoxLayoutData dataf = new VBoxLayoutData();
        dataf.setFlex(1);
        VBoxLayoutData datad = new VBoxLayoutData();
        datad.setFlex(0);
        cpInfo.add(infoProjects, datad);
        cpInfo.add(chartProjects, dataf);
        cpInfo.add(infoAccounts, datad);
        cpInfo.add(chartAccounts, dataf);
        cpInfo.add(infoCustomers, datad);
        cpInfo.add(chartCustomers, dataf);

        if (isToolBarEnabled()) {
            createToolBar(evTable);
        }
    }

    /**
     * Create Tool Bar
     */
    protected void createToolBar(ContentPanel evTable) {
        ToolBar toolBar = new ToolBar();
        toolBar.setSpacing(5);
        evTable.setTopComponent(toolBar);
        calendar = new DateField();
        Button btn = new Button("Previous", Application.ICONS.goPrev());
        btn.setToolTip("Previous day");
        btn.setBorders(true);
        toolBar.add(btn);
        btn.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                Date date = calendar.getValue();
                date = date != null ? new Date(date.getTime() - ONE_DAY) : new Date();
                calendar.setValue(date);
                reloadTable();
            }
        });
        calendar.setName("SelectDay");
        calendar.setFieldLabel("Select day");
        calendar.setValue(new java.util.Date());
        //calendar.setToolTip(metadata.getDescription());
        calendar.getPropertyEditor().setFormat(DEFAULT_DAY_FORMAT);
        toolBar.add(calendar);
        calendar.getDatePicker().addListener(Events.Select, new Listener() {
            //
            @Override
            public void handleEvent(BaseEvent be) {
                reloadTable();
            }
        });
        btn = new Button("Next", Application.ICONS.goNext());
        btn.setToolTip("Next day");
        btn.setBorders(true);
        toolBar.add(btn);
        btn.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                Date date = calendar.getValue();
                date = date != null ? new Date(date.getTime() + ONE_DAY) : new Date();
                calendar.setValue(date);
                reloadTable();
            }
        });
        btn = new Button("Today", Application.ICONS.home());
        btn.setToolTip("Select the current day");
        btn.setBorders(true);
        toolBar.add(btn);
        btn.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                calendar.setValue(new Date());
                reloadTable();
            }
        });
        // --------------------------
        LabelToolItem label = new LabelToolItem("Period total:");
        toolBar.add(label);
        totalTime = new TextField<String>();
        totalTime.setWidth(40);
        toolBar.add(totalTime);

        //            label = new LabelToolItem("QUICK new event:");
        //            label.setId("instantAddEvent");
        //            toolBar.add(new SeparatorToolItem());
        //            toolBar.add(label);
        //            toolBar.add(projectBox);
        //            toolBar.add(taskBox);


        lockButton = new ToggleButton("Submit");
        toolBar.add(new SeparatorToolItem());
        toolBar.add(lockButton);
        setLockState(false);
        lockButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {

                String currentUser = CParam4User.getInstance().getLogin();
                if ("test@test.com".equals(currentUser)) {
                    // The common demo user case:
                    String msg = "The common test user " + currentUser + " can't approve events. Create you own user, please.";
                    final MessageDialog d = new MessageDialog(msg);
                    d.setButtons(Dialog.OK);
                    d.show();
                    return;
                }

                String message = "All events to the selected day will be approved. Are you sure?";
                final MessageDialog d = new MessageDialog(message);
                d.setButtons(Dialog.OKCANCEL);
                d.addListener(Events.BeforeHide, new Listener<WindowEvent>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public void handleEvent(WindowEvent be) {
                        boolean ok = d.isClickedOk(be);
                        setLockState(ok);
                        if (ok) {
                            final java.util.Date selectedDay = calendar.getValue();

                            // Call a service to Lock the current day;
                            EventControllerAsync.Util.getInstance().lockEventDay(selectedDay, new ClientCallback<String>(redir()) {
                                @Override
                                public void onSuccess(String result) {
                                    if (result != null && !result.isEmpty()) {
                                        new MessageDialog("Error: " + result).show();
                                    } else {
                                        reloadTable();
                                        ClientContext.getInstance().setRefreshLockRequest();
                                    }
                                }
                            });
                        }
                    }
                });
                d.show();
            }
        });
    }

    private void setLockState(boolean locked) {
        lockButton.toggle(locked);
        lockButton.setEnabled(!locked);
        if (locked) {
            lockButton.setIcon(Application.ICONS.lock());
            lockButton.setToolTip("Your events was submited");

        } else {
            lockButton.setIcon(Application.ICONS.ok());
            lockButton.setToolTip("Submit for approval");
        }
        enableActionPanel(!locked, getActionPanel());

    }

    /**
     * Before create buttons
     */
    private void beforeCreateButtons() {
        projectBox = new OldCujoBox<CProject>(CProject.name, redir()) {
            @Override
            public void onChange(CProject value) {
//                    if (isDataLoadedToComponent()) {
                taskBox.setValue(null);
                taskBox.reloadStore();
                taskBox.setEnabled(value != null);

                if (value != null) {
                    taskBox.setEmptyText("Select task...");
                    taskBox.focus();
                    taskBox.expand();
                } else {
                    taskBox.setEmptyText("First select a project.");
                }
//                    }
            }

            @Override
            public CQuery<CProject> getDefaultCQuery() {
                CQuery<CProject> projectQuery = new CQuery<CProject>(CProject.class);
                projectQuery.addOrderBy(CProject.name);
                CCriterion<CProject> crn = ProjectTable.createProjectCriterion();
                crn = crn.and(CProject.finished.whereEq(false));
                projectQuery.setCriterion(crn);
                return projectQuery;
            }

            @Override
            protected void onRender(Element parent, int index) {
                super.onRender(parent, index);
                setEmptyText("Select project...");
            }
        };

        taskBox = new OldCujoBox<CTask>(CTask.DISPLAY_PROPERTY, redir()) {
            @Override
            public CQuery<CTask> getDefaultCQuery() {
                CQuery<CTask> projectQuery = new CQuery<CTask>(CTask.class);
                projectQuery.addOrderBy(CTask.DISPLAY_PROPERTY);
                final CCriterion<CTask> crn1, crn2, crn3;
                crn1 = CTask.active.whereEq(true);
                crn2 = CTask.finished.whereEq(false);
                crn3 = CTask.project.whereEq(projectBox.getValue());
                projectQuery.setCriterion(crn1.and(crn2).and(crn2).and(crn3));
                return projectQuery;
            }

            @Override
            public void onChange(CTask selectedValue) {
                if (selectedValue != null) {
                    CEvent event = createEvent();
                    event.set(CEvent.project, projectBox.getValue());
                    event.set(CEvent.task, selectedValue);

                    getService().saveOrUpdate(event, true, new ClientCallback<ValidationMessage>(redir()) {
                        @Override
                        public void onSuccess(ValidationMessage msg) {
                            reloadTable();
                        }
                    });
                }
            }

            @Override
            protected void onRender(Element parent, int index) {
                super.onRender(parent, index);
                setEmptyText("First select a project.");
            }
        };
        taskBox.setEnabled(false);
    }

    /**
     * Is the Tool Bar Enabled ?
     */
    protected boolean isToolBarEnabled() {
        return true;
    }

    /**
     * Get a date selected in Calendar
     */
    public java.sql.Date getSelectedDate() {
        Date date = calendar.getValue();
        return new java.sql.Date(date.getTime());
    }

    /**
     * Get a date selected in Calendar
     */
    public CEvent createEvent() {
        CEvent result = new CEvent().init();

        WTime wTime = getNextTime();
        result.set(CEvent.startTime_, wTime);
        result.set(CEvent.startTime, wTime.getTimeMinutes());
        result.set(CEvent.day, getSelectedDate());
        return result;
    }

    /**
     * Reload table
     */
    @Override
    public void reloadTable() {
        if (totalTime != null) {
            totalTime.setValue("wait ...");
        }
        super.reloadTable();

    }

    public void initHotKeys() {
        this.hotButtons.init();
    }

    @Override
    protected void onTableLoad(PagingLoadResult<CUJO> result) {
        refreshLastTime();
        refreshTotalTime();
        refreshGraphs();
    }

    protected WTime calcuateTime() {
        WTime result = new WTime();
        ListStore<CUJO> store = grid.getStore();

        for (int i = 0, m = store.getCount(); i < m; ++i) {
            CUJO row = store.getAt(i);
            if (!row.isPrivate()) {
                result.addTime(row.getPeriod());
            }
        }

        return result;
    }

    protected void getProjectsChartData(ListStore<CUJO> store, Map<Long, ChartItem> map) {
        for (int i = 0, m = store.getCount(); i < m; ++i) {
            CUJO row = store.getAt(i);
            CProject project_ = row.get(row.project);
            if (!row.isPrivate() && project_ != null) {
                Long id = project_.get(CProject.id);
                ChartItem ci = map.get(id);
                if (ci == null) {
                    String name = project_.get(CProject.name);
                    ColorGxt color = project_.get(CProject.graphColor);
                    ci = createChartItem(id, name, color);
                    map.put(id, ci);
                }
                ci.addValue(row);
            }
        }
    }

    protected void getAccountsChartData(ListStore<CUJO> store, Map<Long, ChartItem> map) {
        for (int i = 0, m = store.getCount(); i < m; ++i) {
            CUJO row = store.getAt(i);
            CAccount account_ = row.get(row.task.add(CTask.account));
            if (!row.isPrivate() && account_ != null) {
                Long id = account_.get(CAccount.id);
                ChartItem ci = map.get(id);
                if (ci == null) {
                    String name = account_.get(CAccount.name);
                    ColorGxt color = account_.get(CAccount.graphColor);
                    ci = createChartItem(id, name, color);
                    map.put(id, ci);
                }
                ci.addValue(row);
            }
        }
    }

    protected void getCustomersChartData(ListStore<CUJO> store, Map<Long, ChartItem> map) {
        for (int i = 0, m = store.getCount(); i < m; ++i) {
            CUJO row = store.getAt(i);
            CProject project = row.get(row.project);
            if (!row.isPrivate() && project != null) {
                CCustomer customer_ = project.get(CProject.customer);
                if (customer_ != null) {
                    final Long id = customer_.getId();
                    ChartItem ci = map.get(id);
                    if (ci == null) {
                        ci = createChartItem(id, customer_.getName(), customer_.getGraphColor());
                        map.put(id, ci);
                    }
                    ci.addValue(row);
                }
            }
        }
    }

    protected String getProjectsChartTitle() {
        return "Time by Project";
    }

    protected String getAccountsChartTitle() {
        return "Time by Account";
    }

    protected String getCustomersChartTitle() {
        return "Time by Customer";
    }

    /**
     * Returns a last time or 8:00 if no row was found
     */
    private WTime getLastTime() {
        int result = 8 * 60; // 8:00

        for (int i = grid.getStore().getCount() - 1; i >= 0; --i) {
            final CEvent event = grid.getStore().getAt(i);
            Short idx = event.get(CEvent.startTime);
            if (idx != null && idx.intValue() > result) {
                result = idx.intValue();
            }
        }
        return new WTime((short) result);
    }

    /**
     * Returns a last time or 8:00 if no row was found
     */
    private WTime getNextTime() {
        return isToday()
                ? new WTime().setCurrentTime()
                : getLastTime();
    }

    /**
     * Notice time in [ms].
     */
    public Integer getNoticeTime() {
        int result = CParam4User.getInstance().getPomodoroInterval() * 60 * 1000;
        return result;
    }

    public DelayedTask getNoticeTask() {
        return noticeTask;
    }

    private void refreshTotalTime() {
        if (isToolBarEnabled()) {
            setLockState(getEventDay().isLocked());
            WTime _totalTime = calcuateTime();
            totalTime.setValue(_totalTime.toString());
        }
    }

    /**
     * Enable / disable buttons on a component panel
     */
    private void enableActionPanel(boolean enable, LayoutContainer panel) {
        for (Component component : panel.getItems()) {
            if (component.getClass().equals(LayoutContainer.class)) {
                enableActionPanel(enable, ((LayoutContainer) component));
            }
            if (!component.getClass().equals(Label.class)) {
                component.setEnabled(enable);
            }
        }
    }

    /**
     * Refresh last time and enable pomodoro dialog.
     */
    private void refreshLastTime() {
        if (isToolBarEnabled() && isToday()) {

            ListStore<CUJO> store = grid.getStore();
            int lastIndex = store.getCount() - 1;
            CUJO lastCujo = lastIndex >= 0 ? store.getAt(lastIndex) : null;
            enablePomodoroDialog = lastCujo == null || !lastCujo.isPrivate();
            if (lastCujo == null || lastCujo.isPrivate()) {
                return;
            }

            refreshTaskTime.delay(1000);
            WTime lastPeriod = lastCujo.getPeriod();
            WTime lastTime = lastCujo.getStartTime();
            WTime currentTime;

            if (lastPeriod.isSecondsSupported()) {
                lastPeriod.addOneSec();
                if (lastPeriod.isZeroSec()) {
                    refreshTotalTime();
                    switch (lastPeriod.getMinutes()) {
                        case (120):
                        case (60):
                        case (30):
                        case (20):
                            lastPeriod.setTime(new WTime(true).substract(lastTime)); // recalculate period
                        case (10):
                        case (5):
                        case (2):
                        case (1):
                            refreshGraphs();
                            break;
                    }
                }
            } else if (lastCujo != null && lastCujo.getStartTime().compareTo(currentTime = new WTime(true)) < 1) {
                int diff = currentTime.substract(lastTime);
                lastPeriod.enableSec().setTime(diff);
                lastCujo.set(lastCujo.period_, lastPeriod);
            }

            store.update(lastCujo);
        }
    }

    private ChartModel getProjectsChartModel() {
        ChartModel cm = new ChartModel(getProjectsChartTitle(), CHART_TITLE_STYLE);
        cm.setBackgroundColour("#fffff5");
        cm.setTitle(null);

        //Legend lg = new Legend(Position.TOP, false);
        //lg.setPadding(10);
        //cm.setLegend(lg);

        PieChart pie = new PieChart();
        pie.setAlpha(0.5f);
        pie.setNoLabels(true);
        pie.setTooltip("#label# #percent#");
        pie.setGradientFill(true);

        Map unsortedMap = new LinkedHashMap<Long, ChartItem>();
        List<String> colors = new ArrayList<String>(unsortedMap.size());
        ListStore<CUJO> store = grid.getStore();
        getProjectsChartData(store, unsortedMap);
        mapProjects = new TreeMap(new ValueComparator(unsortedMap));
        mapProjects.putAll(unsortedMap);

        for (ChartItem item : mapProjects.values()) {
            pie.addSlices(new PieChart.Slice(item.getPeriod(), item.getName(), item.getName()));
            colors.add(item.getColor());
        }

        pie.setColours(colors);
        cm.addChartConfig(pie);
        return cm;
    }

    private ChartModel getAccountsChartModel() {
        ChartModel cm = new ChartModel(getAccountsChartTitle(), CHART_TITLE_STYLE);
        cm.setBackgroundColour("#fffff5");
        cm.setTitle(null);
        //Legend lg = new Legend(Position.TOP, false);
        //lg.setPadding(10);
        //cm.setLegend(lg);

        PieChart pie = new PieChart();
        pie.setAlpha(0.5f);
        pie.setNoLabels(true);
        pie.setTooltip("#label# #percent#");
        pie.setGradientFill(true);

        Map unsortedMap = new LinkedHashMap<Long, ChartItem>();
        List<String> colors = new ArrayList<String>(unsortedMap.size());
        ListStore<CUJO> store = grid.getStore();
        getAccountsChartData(store, unsortedMap);
        mapAccounts = new TreeMap(new ValueComparator(unsortedMap));
        mapAccounts.putAll(unsortedMap);

        for (ChartItem item : mapAccounts.values()) {
            pie.addSlices(new PieChart.Slice(item.getPeriod(), item.getName(), item.getName()));
            colors.add(item.getColor());
        }

        pie.setColours(colors);
        cm.addChartConfig(pie);
        return cm;
    }

    private ChartModel getCustomersChartModel() {
        ChartModel cm = new ChartModel(getCustomersChartTitle(), CHART_TITLE_STYLE);
        cm.setBackgroundColour("#fffff5");
        cm.setTitle(null);
        //Legend lg = new Legend(Position.TOP, false);
        //lg.setPadding(10);
        //cm.setLegend(lg);

        PieChart pie = new PieChart();
        pie.setAlpha(0.5f);
        pie.setNoLabels(true);
        pie.setTooltip("#label# #percent#");
        pie.setGradientFill(true);

        Map unsortedMap = new LinkedHashMap<Long, ChartItem>();
        ListStore<CUJO> store = grid.getStore();
        List<String> colors = new ArrayList<String>(unsortedMap.size());
        getCustomersChartData(store, unsortedMap);
        mapCustomers = new TreeMap(new ValueComparator(unsortedMap));
        mapCustomers.putAll(unsortedMap);

        for (ChartItem item : mapCustomers.values()) {
            pie.addSlices(new PieChart.Slice(item.getPeriod(), item.getName(), item.getName()));
            colors.add(item.getColor());
        }

        pie.setColours(colors);
        cm.addChartConfig(pie);
        return cm;
    }

    private void refreshGraphs() {
        final ChartModel projectsChartModel = getProjectsChartModel();
        chartProjects.setChartModel(projectsChartModel);
        infoProjects.removeAll();
        int i = 0;
        StringBuilder table = new StringBuilder();
        table.append("<table><tr><td width='115px' style='font-size:10px;color:black;font-weight:bold'> " + getProjectsChartTitle() + ":</td><td></td></tr>");
        for (Iterator<ChartItem> it = mapProjects.values().iterator(); it.hasNext();) {
            if (i++ > MAX_INFO_ROWS) {
                break;
            }
            table.append("<tr>");
            ChartItem ci = it.next();
            String key = ci.getName();
            String value = new WTime((int) ci.getPeriod()).toString();
            table.append("<td width='115px' style='font-size:10px;color:" + ci.getColor() + ";'>" + key + "</td>");
            table.append("<td style='font-size:10px;color:" + ci.getColor() + ";'> " + value + "</td>");
            table.append("</tr>");
        }
        table.append("</table>");
        infoProjects.addText(table.toString());

        final ChartModel accountsChartModel = getAccountsChartModel();
        chartAccounts.setChartModel(accountsChartModel);
        infoAccounts.removeAll();
        i = 0;
        table = new StringBuilder();
        table.append("<table><tr><td width='115px' style='font-size:10px;color:black;font-weight:bold'> " + getAccountsChartTitle() + ":</td><td></td></tr>");
        for (Iterator<ChartItem> it = mapAccounts.values().iterator(); it.hasNext();) {
            if (i++ > MAX_INFO_ROWS) {
                break;
            }
            table.append("<tr>");
            ChartItem ci = it.next();
            String key = ci.getName();
            String value = new WTime((int) ci.getPeriod()).toString();
            table.append("<td width='115px' style='font-size:10px;color:" + ci.getColor() + ";'>" + key + "</td>");
            table.append("<td style='font-size:10px;color:" + ci.getColor() + ";'> " + value + "</td>");
            table.append("</tr>");
        }
        table.append("</table>");
        infoAccounts.addText(table.toString());


        final ChartModel customersChartModel = getCustomersChartModel();
        chartCustomers.setChartModel(customersChartModel);
        infoCustomers.removeAll();
        i = 0;
        table = new StringBuilder();
        table.append("<table><tr><td width='115px' style='font-size:10px;color:black;font-weight:bold'> " + getCustomersChartTitle() + ":</td><td></td></tr>");
        for (Iterator<ChartItem> it = mapCustomers.values().iterator(); it.hasNext();) {
            if (i++ > MAX_INFO_ROWS) {
                break;
            }
            table.append("<tr>");
            ChartItem ci = it.next();
            String key = ci.getName();
            String value = new WTime((int) ci.getPeriod()).toString();
            table.append("<td width='115px' style='font-size:10px;color:" + ci.getColor() + ";'>" + key + "</td>");
            table.append("<td style='font-size:10px;color:" + ci.getColor() + ";'> " + value + "</td>");
            table.append("</tr>");
        }
        table.append("</table>");
        infoCustomers.addText(table.toString());


        cpInfo.layout(true);
    }

    class ValueComparator implements Comparator {

        Map base;

        public ValueComparator(Map base) {
            this.base = base;
        }

        @Override
        public int compare(Object a, Object b) {

            if (((ChartItem) base.get(a)).getPeriod() < ((ChartItem) base.get(b)).getPeriod()) {
                return 1;
            } else if (((ChartItem) base.get(a)).getPeriod() == ((ChartItem) base.get(b)).getPeriod()) {
                return 0;
            } else {
                return -1;
            }
        }
    }
}
