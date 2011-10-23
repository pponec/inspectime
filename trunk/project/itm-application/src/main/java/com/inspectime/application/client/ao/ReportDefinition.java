/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.ao;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.SummaryColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.SummaryRenderer;
import com.extjs.gxt.ui.client.widget.grid.SummaryType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.inspectime.application.client.cbo.CReport;
import com.inspectime.application.client.gui.report.BaseReport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.ujorm.gxt.client.CujoModel;
import org.ujorm.gxt.client.CujoProperty;

/**
 * Report type Ponec
 * @author Pavel Ponec
 */
public class ReportDefinition implements Serializable {

    /** Decimal DEFAULT_DECIMAL_FORMAT */
    public static final NumberFormat DEFAULT_DECIMAL_FORMAT = NumberFormat.getFormat("0.00");

    public static final DateTimeFormat SQL_DAY_FORMAT = GWT.isClient()
            ? DateTimeFormat.getFormat("yyyy-MM-dd (EEE)")
            : null;

    final private BaseReport baseReport;

    private ReportRequest reportRequest;
    private String reportTitle;
    private String menuName;
    private CujoProperty namePropertyGrpB;

    private Map<CujoProperty, SummaryColumnConfig> columns = new LinkedHashMap<CujoProperty, SummaryColumnConfig>();

    public ReportDefinition(BaseReport baseReport) {
        this.baseReport = baseReport;
    }

    public ReportRequest getReportRequest() {
        return reportRequest;
    }

    public void setReportRequest(ReportRequest reportRequest) {
        this.reportRequest = reportRequest;
    }

    public BaseReport getBaseReport() {
        return baseReport;
    }

    /** Remove required column */
    public void removeColumn(CujoProperty p) {
        columns.remove(p);
    }

    /** Returns column list */
    public List<ColumnConfig> getColumns() {
        return new ArrayList<ColumnConfig>(columns.values());
    }

    public void addColumn(CujoProperty property) {
        addColumn(property, property.getLabel());
    }

    public void addColumn(CujoProperty p, String name) {

        SummaryColumnConfig config = new SummaryColumnConfig();
        config.setId(p.getName());
        config.setHeader(name);
        config.setWidth(100); // [px]
        config.setAlignment(p.isTypeOf(Number.class)
                ? HorizontalAlignment.RIGHT
                : HorizontalAlignment.LEFT);
        config.setSortable(true);

        if (p.isTypeOf(java.util.Date.class)) {
            config.setDateTimeFormat(CujoModel.DEFAULT_DATE_FORMAT);
        }
        if (p.isTypeOf(java.sql.Date.class)) {
            config.setDateTimeFormat(SQL_DAY_FORMAT);
        }
        if (p.isTypeOf(Boolean.class)) {
            config.setAlignment(HorizontalAlignment.CENTER);
            config.setRenderer(new GridCellRenderer() {

                @Override
                public Object render(ModelData model, String property, ColumnData config, int rowIndex, int colIndex, ListStore store, Grid grid) {
                    Boolean b = (Boolean) model.get(property);
                    return b ? "ok" : '-';
                }
            });
        }
        if (p.isTypeOf(Number.class)) {
            config.setSummaryType(SummaryType.SUM);
            config.setAlignment(HorizontalAlignment.RIGHT);
            if (false && p.isTypeOf(Float.class)) {
                config.setNumberFormat(DEFAULT_DECIMAL_FORMAT);
                config.setSummaryFormat(DEFAULT_DECIMAL_FORMAT);
            }
            if (CReport.time.equals(p)) {
                config.setRenderer(new GridCellRenderer() {

                    @Override
                    public Object render(ModelData model, String property, ColumnData config, int rowIndex, int colIndex, ListStore store, Grid grid) {
                        int minutes = ((CReport) model).getTimeMin();
                        return formatTime(minutes);
                    }
                });
                config.setSummaryRenderer(new SummaryRenderer() {

                    @Override
                    public String render(Number value, Map<String, Number> data) {
                        double minutes = (Double) value;
                        return formatTime((int) minutes);
                    }
                });
            }
            if (CReport.percentGroup.equals(p) || CReport.percentTotal.equals(p)) {
                config.setRenderer(new GridCellRenderer() {
                    @Override
                    public Object render(ModelData model, String property, ColumnData config, int rowIndex, int colIndex, ListStore store, Grid grid) {
                        float percent = (Float) model.get(property);
                        return formatPercent(percent);
                    }
                });
                config.setSummaryRenderer(new SummaryRenderer() {
                    @Override
                    public String render(Number value, Map<String, Number> data) {
                        double percent = (Double) value;
                        return formatPercent(percent);
                    }
                });
            }
        }

        if ("id".equals(p.getName())) {
            config.setHidden(true);
        }
        if ("active".equals(p.getName())) {
            config.setHidden(true);
        }

        if (CReport.levelB.add(namePropertyGrpB).equals(p)) {
            config.setSummaryType(SummaryType.COUNT);
            config.setSummaryRenderer(new SummaryRenderer() {

                @Override
                public String render(Number value, Map<String, Number> data) {
                    return value.intValue() > 1 ? "(" + value.intValue() + " Items)" : "(1 Item)";
                }
            });
        }

        config.setSortable(false);
        columns.put(p,config);
    }

    /** Format time to hourses */
    private String formatTime(int minutes) {
        double hours = (minutes / 60.0D) + 0.0001;
        return DEFAULT_DECIMAL_FORMAT.format(hours) + " hours";
    }

    /** Format time to hourses */
    private String formatPercent(double percent) {
        return DEFAULT_DECIMAL_FORMAT.format(percent) + " %";
    }

    /** Returns the GroupBY */
    public String getGrouBy() {
        switch (this.baseReport.getReportTypeEnum()) {
            case WORK_OPEN_DAYS:
                String result = CReport.levelB + "." + CReportWorkDay.user;
                return result;
            case WORK_EVENTS:
                result = CReport.levelB + "." + CReportWorkDay.day;
                return result;
            default:
                String eventProperty = reportRequest.getEventProperties()[0];
                int i = 1 + eventProperty.lastIndexOf('.');
                result = CReport.levelA + "." + eventProperty.substring(i);
                return result;
        }
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getReportTitle() {
        return reportTitle;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    /** TODO */
    public String getColumnNameA() {
        return "A";
    }

    /** TODO */
    public String getColumnNameB() {
        return "B";
    }

    public CujoProperty getNamePropertyGrpB() {
        return namePropertyGrpB;
    }

    public void setNamePropertyGrpB(CujoProperty lastPropertyGrpB) {
        this.namePropertyGrpB = lastPropertyGrpB;
    }

    /** Is report type of overview ? */
    public boolean isOverview() {
        return baseReport.isOverview();
    }
}
