/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.gui.report;

import com.inspectime.application.client.cbo.CTask;
import org.ujorm.gxt.client.tools.MessageDialog;
import com.inspectime.application.client.gui.UIManager;
import org.ujorm.gxt.client.CLoginRedirectable;
import org.ujorm.gxt.client.AbstractCujo;
import com.inspectime.application.client.gui.event.ChartItem;
import java.util.LinkedHashMap;
import java.util.Map;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.charts.client.Chart;
import com.extjs.gxt.charts.client.model.ChartModel;
import com.extjs.gxt.charts.client.model.charts.PieChart;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridView;
import com.extjs.gxt.ui.client.widget.grid.GroupSummaryView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowData;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayoutData;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.inspectime.application.client.Application;
import com.inspectime.application.client.ao.ReportData;
import com.inspectime.application.client.ao.ReportDefinition;
import com.inspectime.application.client.ao.ReportTypeEnum;
import com.inspectime.application.client.cbo.CEvent;
import com.inspectime.application.client.cbo.CReport;
import com.inspectime.application.client.controller.ReportControllerAsync;
import java.util.ArrayList;
import java.util.List;
import org.ujorm.gxt.client.CMessageException;
import org.ujorm.gxt.client.CujoProperty;
import org.ujorm.gxt.client.ao.Permissions;
import org.ujorm.gxt.client.commons.Icons;
import org.ujorm.gxt.client.tools.ColorGxt;

import static com.inspectime.application.client.cbo.CEvent.*;

/**
 * Report Table
 * @author Ponec
 */
public class ReportTable extends LayoutContainer implements CLoginRedirectable {

    public static final String CHART_TITLE_STYLE = "font-size: 12px; font-family: Verdana; text-align: center;";
    public static final DateTimeFormat DEFAULT_DAY_FORMAT = GWT.isClient()
            ? DateTimeFormat.getFormat("yyyy-MM-dd (EEE)")
            : null;
    public static final String REPORT_NAME_KEY = "name";
    public static final String REPORT_MENU_KEY = "report";

    protected ReportFactory reportFactory = new ReportFactory();
    protected ReportDefinition reportDefinition;
    protected DateField calendar;
    protected ContentPanel cpPanel;
    //protected ContentPanel cpTable;
    protected Permissions displayPermission;
    protected DateFilterPanel dateFilterPanel;
    protected Grid grid;
    protected PagingToolBar gridToolBar = new PagingToolBar(500);
    protected Chart chartProjects;
    protected ModelData firstMenuItem;

    public ReportTable() {
        setLayout(new FitLayout());
    }

    @Override
    protected void onRender(com.google.gwt.user.client.Element parent, int index) {
        super.onRender(parent, index);

        cpPanel = new ContentPanel();
        cpPanel.setHeaderVisible(false);
        cpPanel.setSize("98%", "450px");
        cpPanel.setLayout(new BorderLayout());
        cpPanel.setIcon(Icons.Pool.table());
        cpPanel.setCollapsible(false);
        cpPanel.setAnimCollapse(false);

        createTree();
        createGraphs();
        createCenter();

//        BorderLayoutData west = new BorderLayoutData(LayoutRegion.CENTER);
//        west.setMargins(new Margins(5));
//        west.setSplit(true);
//        cpPanel.add(cpTable, west);

        add(cpPanel, new FlowData(0));
    }

    protected void createCenter() {

        BorderLayout borderLayout = new BorderLayout();
        ContentPanel panel = new ContentPanel();
        panel.setHeaderVisible(false);
        panel.setLayout(borderLayout);

        BorderLayoutData center = new BorderLayoutData(LayoutRegion.CENTER, 200, 150, 250);
        center.setMargins(new Margins(5));
        cpPanel.add(panel, center);

//        ToolBar toolBar = new ToolBar();
//        toolBar.setSpacing(5);
//        panel.setTopComponent(toolBar);

        dateFilterPanel = new DateFilterPanel().setReportTable(this);
        panel.add(dateFilterPanel, new BorderLayoutData(LayoutRegion.NORTH, 150));
        //
        //dateFilterPanel = new DateFilterPanel();
        //panel.add(new Label("Center"), new BorderLayoutData(LayoutRegion.CENTER, 250));
        panel.add(grid=createGrid(), new BorderLayoutData(LayoutRegion.CENTER, 250));

        setTheReport((BaseReport) firstMenuItem.get(REPORT_MENU_KEY), false);
        dateFilterPanel.setRangeToCalendars();
    }

    /** Reload the grid. */
    public void reloadGrid() {
        boolean load = grid.getStore().getLoader().load();
        com.google.gwt.core.client.GWT.log("Report data loading: " + load, null);
    }

    /** Create Grid */
    protected Grid<CReport> createGrid() {
        return reconfigureGrid(null);
    }

    /** Create Grid */
    protected Grid<CReport> reconfigureGrid(Grid<CReport> grid) {

        final PagingLoader<PagingLoadResult<ModelData>> loader = createLodader();
        final GroupingStore<CReport> store = new GroupingStore<CReport>(loader);
        boolean groupByMode = false;
        if (reportDefinition!=null) switch (reportDefinition.getBaseReport().getReportTypeEnum()) {
            case EVENT_TWO_LEVEL:
                groupByMode = !reportDefinition.isOverview();
                break;
            case WORK_EVENTS:
            case WORK_OPEN_DAYS:
                groupByMode = true;
                break;
            case WORK_ATTENDANCE:
            default:
                groupByMode = false;
        }

        if (groupByMode) {
             store.groupBy(getReportDefinition().getGrouBy());
        }

        // -----------

        ColumnModel cm = new ColumnModel(getReportDefinition().getColumns());
        if (grid==null) {            
            grid = new Grid<CReport>(store, cm);
            grid.setView(createGridView());
            grid.setBorders(true);
            grid.setStateId(getClass().getName());
            grid.setStateful(true);
            grid.setLoadMask(true);
            grid.setBorders(true);
            //cpPanel.setBottomComponent(gridToolBar);
        } else {
            grid.reconfigure(store, cm);
        }
        return grid;
    }

    /** Create Grid View */
    private GridView createGridView() {
        final GroupSummaryView summary = new GroupSummaryView();
        summary.setForceFit(true);
        summary.setShowGroupedColumn(true); // Important value, do not modify it.
        summary.setShowDirtyCells(true);    // Important value, do not modify it.
        return summary;
    }

    /** Create default report definition. */
    private ReportDefinition createDefaultReportDefinition() {
        ReportDefinition result = reportFactory.createReportDefinition(_account, _customer);
        return result;
    }


    /** Create tree panel */
    protected void createTree() {
        ContentPanel treePanel = new ContentPanel();
        treePanel.setHeaderVisible(false);
        treePanel.setScrollMode(Scroll.AUTO);
        BorderLayoutData left = new BorderLayoutData(LayoutRegion.WEST, 200, 150, 250);
        left.setMargins(new Margins(5));
        cpPanel.add(treePanel, left);

        TreeStore<ModelData> store = new TreeStore<ModelData>();
        TreePanel<ModelData> tree = new TreePanel<ModelData>(store);
        tree.setDisplayProperty(REPORT_NAME_KEY);
        tree.getStyle().setLeafIcon(Application.ICONS.report());
        treePanel.add(tree);

        // ---- Items ----
        CujoProperty grpA = null;
        ModelData root = null;

        root = newTreeItem("By User");
        grpA = CEvent._user;
        store.add(root, false);
        store.add(root, newTreeItem4Attendance("Attendance at work"), false);
        store.add(root, newTreeItem4OpenDays("Open days"), false);
        store.add(root, newTreeItem4Event("Event preview"), false);
        store.add(root, newTreeItem(grpA, grpA), false);
        store.add(root, newTreeItem(grpA,_account), false);
        store.add(root, newTreeItem(grpA,_project), false);
        store.add(root, newTreeItem(grpA,_task), false);
        store.add(root, newTreeItem(grpA,_product), false);

        root = newTreeItem("By Project");
        grpA = CEvent._project;
        store.add(root, false);
        store.add(root, newTreeItem(grpA, grpA), false);
        store.add(root, newTreeItem(grpA,_account), false);
        store.add(root, newTreeItem(grpA,_task), false);
        store.add(root, newTreeItem(grpA,_user), false);

        root = newTreeItem("By Product");
        grpA = CEvent._product;
        store.add(root, false);
        store.add(root, newTreeItem(grpA, grpA), false);
        store.add(root, newTreeItem(grpA,_account), false);
        store.add(root, newTreeItem(grpA,_project), false);
        store.add(root, newTreeItem(grpA,_task), false);
        store.add(root, newTreeItem(grpA,_customer), false);
        store.add(root, newTreeItem(grpA,_user), false);
        
        root = newTreeItem("By Customer");
        grpA = CEvent._customer;
        store.add(root, false);
        store.add(root, firstMenuItem=newTreeItem(grpA,grpA), false);
        store.add(root, newTreeItem(grpA,_account), false);
        store.add(root, newTreeItem(grpA,_project), false);
        store.add(root, newTreeItem(grpA,_task), false);
        store.add(root, newTreeItem(grpA,_product), false);
        store.add(root, newTreeItem(grpA,_user), false);

        root = newTreeItem("By Account");
        grpA = CEvent._account;
        store.add(root, false);
        store.add(root, newTreeItem(grpA, grpA), false);
        store.add(root, newTreeItem(grpA,_project), false);
        store.add(root, newTreeItem(grpA,_task), false);
        store.add(root, newTreeItem(grpA,_product), false);
        store.add(root, newTreeItem(grpA,_customer), false);
        store.add(root, newTreeItem(grpA,_user), false);

        // ---
        tree.expandAll();
        tree.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<ModelData>() {
            @Override
            public void selectionChanged(SelectionChangedEvent<ModelData> se) {
                final ModelData md = se.getSelectedItem();
                final BaseReport baseReport = md.get(REPORT_MENU_KEY);
                if (baseReport != null) {
                    setTheReport(baseReport, true);
                }
            }
        });
    }

    /** Set the report and reload table */
    private void setTheReport(BaseReport baseReport, boolean reloadTable) {
        this.reportDefinition = reportFactory.createReportDefinition(baseReport);
        this.dateFilterPanel.setReportTitle(reportDefinition.getReportTitle());
        this.dateFilterPanel.showUserFiler(baseReport.isUserFilter());
        this.reconfigureGrid(grid);
        if (reloadTable) {
            reloadGrid();
        }
    }

    /** Create Graph panel */
    protected void createGraphs() {
        ContentPanel cpInfo = new ContentPanel();
        cpInfo.setHeaderVisible(false);
        cpInfo.setScrollMode(Scroll.NONE);
        BorderLayoutData left = new BorderLayoutData(LayoutRegion.EAST, 200, 100, 300);
        left.setMargins(new Margins(5));
        cpPanel.add(cpInfo, left);

        String url = "resources/chart/open-flash-chart.swf";

        chartProjects = new Chart(url);
        chartProjects.setBorders(true);

        VBoxLayoutData data = new VBoxLayoutData();
        data.setFlex(1);
        cpInfo.add(chartProjects, data);
    }

    /** Create and set a new report Request */
    private void setGraphModel(ReportData reportData) {
        String title = reportFactory.getGraphName(reportDefinition.getBaseReport());
        ChartModel cm = new ChartModel(title, CHART_TITLE_STYLE);
        cm.setBackgroundColour("#fffff5");
        //Legend lg = new Legend(Position.TOP, false);
        //lg.setPadding(10);
        //cm.setLegend(lg);

        PieChart pie = new PieChart();
        pie.setAlpha(0.5f);
        pie.setNoLabels(true);
        pie.setTooltip("#label# #percent#");
        pie.setGradientFill(true);

        Map<Long, ChartItem> map = new LinkedHashMap<Long, ChartItem>();
        List<String> colors = new ArrayList<String>();

        Map<Long, AbstractCujo> cujoMap = reportData.getEntityMap();
        for (ReportChartItem chartItem : reportData.getChart()) {
            AbstractCujo grpA = cujoMap.get(chartItem.getId());
            String name = grpA.get(REPORT_NAME_KEY);
            pie.addSlices(new PieChart.Slice((Integer)chartItem.getPeriod(), name, name));
            ColorGxt color = grpA.get("graphColor");
            colors.add(color!=null ? color.getColor() : ColorGxt.getRandomColor());
        }

        pie.setColours(colors);
        cm.addChartConfig(pie);
        chartProjects.setChartModel(cm);
    }

    private ModelData newTreeItem(String text) {
        final ModelData m = new BaseModelData();
        m.set(REPORT_NAME_KEY, text);
        return m;
    }

    private ModelData newTreeItem4Event(String text) {
        final ReportDefinition rd = reportFactory.createBaseReport(ReportTypeEnum.WORK_EVENTS, CEvent.task.add(CTask.project), CEvent.task);
        final ModelData m = new BaseModelData();
        m.set(REPORT_NAME_KEY, rd.getMenuName());
        m.set(REPORT_MENU_KEY, rd.getBaseReport());
        return m;
    }

    private ModelData newTreeItem4Attendance(String text) {
        final ReportDefinition rd = reportFactory.createBaseReport(ReportTypeEnum.WORK_ATTENDANCE, CEvent.task.add(CTask.project), CEvent.task);
        final ModelData m = new BaseModelData();
        m.set(REPORT_NAME_KEY, rd.getMenuName());
        m.set(REPORT_MENU_KEY, rd.getBaseReport());
        return m;
    }

    private ModelData newTreeItem4OpenDays(String text) {
        final ReportDefinition rd = reportFactory.createBaseReport(ReportTypeEnum.WORK_OPEN_DAYS, CEvent.task.add(CTask.project), CEvent.task);
        final ModelData m = new BaseModelData();
        m.set(REPORT_NAME_KEY, rd.getMenuName());
        m.set(REPORT_MENU_KEY, rd.getBaseReport());
        return m;
    }

    private ModelData newTreeItem(CujoProperty grp1, CujoProperty grp2) {
        final ReportDefinition rd = reportFactory.createBaseReport(grp1, grp2);
        final ModelData m = new BaseModelData();
        m.set(REPORT_NAME_KEY, rd.getMenuName());
        m.set(REPORT_MENU_KEY, rd.getBaseReport());
        return m;
    }

    /** Set Permission Roles */
    public ReportTable setRoles(Enum... roles) {
        this.displayPermission = new Permissions(roles);
        return this;
    }

    /** Create Loader */
    protected PagingLoader<PagingLoadResult<ModelData>> createLodader() {
        RpcProxy<PagingLoadResult<CReport>> proxy = new RpcProxy<PagingLoadResult<CReport>>() {
            @Override
            public void load(final Object loadConfig, final AsyncCallback<PagingLoadResult<CReport>> callback) {
                final AsyncCallback<ReportData> callback2 = new AsyncCallback<ReportData>() {

                    @Override
                    public void onFailure(final Throwable caught) {
                        if (CMessageException.isSessionTimeout(caught)) {
                            redirectToLogin();
                            return;
                        }
                        callback.onFailure(caught);
                        String msg = "Unsupported operation";
                        GWT.log(msg, caught);
                        new MessageDialog(msg).show();
                        grid.unmask();
                    }

                    @Override
                    public void onSuccess(ReportData result) {
                        setGraphModel(result);
                        result.attachMap();

                        BasePagingLoadResult<CReport> pg = new BasePagingLoadResult<CReport>(result.getReports(), 0, result.getReports().size());
                        //BasePagingLoadResult<CReport> pg = new BasePagingLoadResult<CReport>(result.getReports());
                        callback.onSuccess(pg);
                    }
                };
                getService().getReportData
                        ( getReportDefinition().getReportRequest()
                        , dateFilterPanel.getDateFrom()
                        , dateFilterPanel.getDateTo()
                        , dateFilterPanel.getUserId()
                        , callback2
                        );
            }
        };

        // loader
        final PagingLoader<PagingLoadResult<ModelData>> loader = new BasePagingLoader<PagingLoadResult<ModelData>>(proxy);
        loader.setRemoteSort(true);
        //gridToolBar.bind(loader);
        
        return loader;
    }

    /** Create new report Request */
    private ReportControllerAsync getService() {
        return ReportControllerAsync.Util.getInstance();
    }

    public ReportDefinition getReportDefinition() {
        if (reportDefinition==null) {
            reportDefinition = createDefaultReportDefinition();
        }
        GWT.log("Test: " + reportDefinition.getColumns().size());
        return reportDefinition;
    }

    public void setReportDefinition(ReportDefinition reportDefinition) {
        this.reportDefinition = reportDefinition;
    }

    @Override
    public void redirectToLogin() {
        UIManager.getInstance().redirectToLogin();
    }

}
