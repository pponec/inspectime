/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */


package com.inspectime.application.client.gui.report;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.google.gwt.core.client.GWT;
import com.inspectime.application.client.ao.CReportWorkDay;
import com.inspectime.application.client.ao.ReportDefinition;
import com.inspectime.application.client.ao.ReportRequest;
import com.inspectime.application.client.ao.ReportTypeEnum;
import com.inspectime.application.client.cbo.CAccount;
import com.inspectime.application.client.cbo.CCustomer;
import com.inspectime.application.client.cbo.CEvent;
import com.inspectime.application.client.cbo.CProduct;
import com.inspectime.application.client.cbo.CProject;
import com.inspectime.application.client.cbo.CReport;
import com.inspectime.application.client.cbo.CTask;
import com.inspectime.application.client.cbo.CUser;
import com.inspectime.application.client.service.CParam4Company;
import com.inspectime.application.client.service.CParam4System;
import org.ujorm.gxt.client.CPathProperty;
import org.ujorm.gxt.client.CujoProperty;

/**
 * Report factory
 * @author Pavel Ponec
 */
public class ReportFactory implements GridCellRenderer {

    public ReportDefinition createReportDefinition(CujoProperty<CEvent, CAccount> _account, CujoProperty<CEvent, CCustomer> _customer) {
        final BaseReport baseReport = new BaseReport(ReportTypeEnum.EVENT_TWO_LEVEL, _account, _customer);
        return createReportDefinition(baseReport);
    }

    /** Create Report Definition include table colums */
    public ReportDefinition createReportDefinition(BaseReport baseReport) {
        ReportDefinition result = new ReportDefinition(baseReport);
        result.setMenuName(getMenuName(baseReport));
        result.setReportTitle(getReportName(baseReport));

        CPathProperty grpA2Name = getDisplayProperty(baseReport.grpA);
        CPathProperty grpB2Name = getDisplayProperty(baseReport.grpB);
        result.setReportRequest(new ReportRequest(baseReport.getReportTypeEnum(), grpA2Name, grpB2Name));
        result.setNamePropertyGrpB(grpB2Name.getLastProperty());

        switch (baseReport.getReportTypeEnum()) {
            case WORK_EVENTS:
                result.addColumn(CReport.levelB.add(CEvent.day), "Day");
                result.addColumn(CReport.levelB.add(CEvent.startTime_), "Start Time");
                result.addColumn(CReport.levelB.add(CEvent.period_), "Period");
                result.addColumn(CReport.levelA.add(CProject.name), "Project");
                result.addColumn(CReport.levelB.add(CEvent.task).add(CTask.name), "Task");
                result.addColumn(CReport.levelB.add(CEvent.description), "Description");
                
                if (CParam4Company.getInstance().isReportPrivateEvents()) {
                    result.addColumn(CReport.levelB.add(CEvent.task).add(CTask.privateState), "Private");
                }

                break;
                
            case WORK_OPEN_DAYS:
                result.addColumn(CReport.levelB.add(CReportWorkDay.user));
            case WORK_ATTENDANCE:
                result.addColumn(CReport.levelB.add(CReportWorkDay.day));
                result.addColumn(CReport.levelB.add(CReportWorkDay.arrival));
                result.addColumn(CReport.levelB.add(CReportWorkDay.departure));
                result.addColumn(CReport.levelB.add(CReportWorkDay.workTime), "Work time");
                result.addColumn(CReport.levelB.add(CReportWorkDay.privateTime), "Private time");
                result.addColumn(CReport.levelB.add(CReportWorkDay.closed), "Closed");
                result.addColumn(CReport.levelB.add(CReportWorkDay.taskNames), "Tasks");
                break;

            default:
                result.addColumn(CReport.levelA.add(grpA2Name.getLastProperty()), getGridColumnName(baseReport.grpA));
                result.addColumn(CReport.levelB.add(grpB2Name.getLastProperty()), getGridColumnName(baseReport.grpB));
                result.addColumn(CReport.time);
                result.addColumn(CReport.percentGroup, "Percent by group");
                result.addColumn(CReport.percentTotal, "Percent total");

                if (baseReport.isOverview()) {
                    result.removeColumn(CReport.levelB.add(grpA2Name.getLastProperty()));
                    result.removeColumn(CReport.percentGroup);
                } else {
                    result.getColumns().get(0).setRenderer(getBlindRenderer());
                    if (CParam4System.getInstance().isDebug()) {
                        result.getColumns().get(1).setRenderer(getIdRenderer());
                    }
                }
        }

        return result;
    }

    @Override
    public Object render(ModelData model, String property, ColumnData config, int rowIndex, int colIndex, ListStore store, Grid grid) {
        return "";
    }

    /** Get Empty Renderer. */
    public GridCellRenderer getBlindRenderer() {
        return this;
    }

    /** Get Empty Renderer. */
    public GridCellRenderer getIdRenderer() {
        return new GridCellRenderer() {
            @Override
            public Object render(ModelData model, String property, ColumnData config, int rowIndex, int colIndex, ListStore store, Grid grid) {
                String result = model.get(property) + " (" + model.get(CReport.levelB+".id") + ")";
                return result;
            }
        };
    }


    /** Return s report definition with the BaseReport instance */
    public ReportDefinition createBaseReport(CujoProperty grp1, CujoProperty grp2) {
        return createBaseReport(ReportTypeEnum.EVENT_TWO_LEVEL, grp1, grp2);
    }

    /** Return s report definition with the BaseReport instance */
    public ReportDefinition createBaseReport(ReportTypeEnum reportEnum, CujoProperty grp1, CujoProperty grp2) {
        BaseReport baseReport = new BaseReport(reportEnum, grp1, grp2);
        ReportDefinition result = new ReportDefinition(baseReport);
        result.setMenuName(getMenuName(baseReport));
        result.setReportTitle(result.getMenuName());

        return result;
    }


    /** Get Report Name */
    private String getReportName(BaseReport baseReport) throws UnsupportedOperationException {
        final String result = getGridColumnName(baseReport.getGrpA()) + " " + getMenuName(baseReport);
        return result;
    }

    /** Get Menu Name */
    private String getMenuName(BaseReport baseReport) throws UnsupportedOperationException {
        switch(baseReport.reportTypeEnum) {
            case WORK_EVENTS:
                return "Event preview";
            case WORK_ATTENDANCE:
                return "Attendance at work";
            case WORK_OPEN_DAYS:
                return "Open days";
            default:
                return getMenuName(baseReport.grpA, baseReport.grpB);
        }
    }

    /** Get Menu Name */
    private String getMenuName(final CujoProperty grpA, final CujoProperty grpB) throws UnsupportedOperationException {

        String menuName;
        if (grpB.equals(grpA)) {
            menuName = "Overview";
        } else {
            menuName = "-- by " + getGridColumnName(grpB);
        }
        return menuName;
    }

    /** Get Main Graph name */
    public String getGraphName(final BaseReport baseReport) throws UnsupportedOperationException {
        switch (baseReport.getReportTypeEnum()) {
            case EVENT_TWO_LEVEL:
                return getGridColumnName(baseReport.grpA) + "s";
            default:
                return "Graph is not available";
        }
                
        
    }

    /** Get Grid Column name */
    private String getGridColumnName(final CujoProperty grp) throws UnsupportedOperationException {

        String menuName;
         if (CEvent._account.equals(grp)) {
            menuName = "Account";
        } else if (CEvent._customer.equals(grp)) {
            menuName = "Customer";
        } else if (CEvent._project.equals(grp)) {
            menuName = "Project";
        } else if (CEvent._product.equals(grp)) {
            menuName = "Product";
        } else if (CEvent._task.equals(grp)) {
            menuName = "Task";
        } else if (CEvent._user.equals(grp)) {
            menuName = "User";
        } else {
            menuName = grp.getLabel();
            GWT.log("An undefined GridColumnName for the: " + grp);
        }
        return menuName;
    }

    /** Returns display property */
    public CPathProperty getDisplayProperty(CujoProperty grp) {

        CujoProperty result;

        if (CEvent._account.equals(grp)) {
            result = grp.add(CAccount.name);
        } else if (CEvent._customer.equals(grp)) {
            result = grp.add(CCustomer.name);
        } else if (CEvent._project.equals(grp)) {
            result = grp.add(CProject.name);
        } else if (CEvent._product.equals(grp)) {
            result = grp.add(CProduct.name);
        } else if (CEvent._task.equals(grp)) {
            result = grp.add(CTask.DISPLAY_PROPERTY);
        } else if (CEvent._user.equals(grp)) {
            result = grp.add(CUser.name);
        } else {
            result = CPathProperty.newInstance(grp);
        }

        return (CPathProperty) result;
    }

}
