/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.ao;

import com.inspectime.application.client.cbo.CReport;
import com.inspectime.application.client.gui.report.ReportChartItem;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.ujorm.gxt.client.AbstractCujo;

/**
 * Report Data
 * @author Ponec
 */
public class ReportData implements Serializable {

    private List<CReport> reports;
    private Map<Long, AbstractCujo> entityMap;
    private AbstractCujo type;
    private int totalTimeMin;
    private ReportChartItem[] chart;

    public Map<Long, AbstractCujo> getEntityMap() {
        return entityMap;
    }

    public void setEntityMap(Map<Long, AbstractCujo> entityMap) {
        this.entityMap = entityMap;
    }

    public List<CReport> getReports() {
        return reports;
    }

    public void setReports(List<CReport> reports) {
        this.reports = reports;
    }

    public AbstractCujo getType() {
        return type;
    }

    public void setType(AbstractCujo type) {
        this.type = type;
    }

    public int getTotalTimeMin() {
        return totalTimeMin;
    }

    public void setTotalTimeMin(int totalTimeMin) {
        this.totalTimeMin = totalTimeMin;
    }
    
    /** Attach map to the list. */
    public List<CReport> attachMap() {
        for (CReport report : reports) {
            Long id = report.get(CReport.levelAid);
            if (id!=null) {
                AbstractCujo value = entityMap.get(id);
                report.set(CReport.levelA, value);
                // report.set(CReport.levelAid, null); // TODO ?
            }
        }
        entityMap = new HashMap<Long, AbstractCujo>(0);
        return reports;
    }

    /** Set chart data */
    public ReportChartItem[] getChart() {
        return chart;
    }

    /** Set chart data */
    public void setChart(List<ReportChartItem> chart) {
        this.chart = chart.toArray(new ReportChartItem[chart.size()]);
    }

}
