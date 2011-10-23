/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.gui.report;

import com.inspectime.application.client.ao.ReportTypeEnum;
import org.ujorm.gxt.client.CujoProperty;

/**
 * Menu definition of all reports
 * @author Ponec
 */
public class BaseReport {

    final ReportTypeEnum reportTypeEnum;
    final CujoProperty grpA;
    final CujoProperty grpB;

    public BaseReport(ReportTypeEnum reportTypeEnum, CujoProperty grpA) {
        this(reportTypeEnum, grpA, grpA);
    }

    public BaseReport(ReportTypeEnum reportTypeEnum, CujoProperty grpA, CujoProperty grpB) {
        this.reportTypeEnum = reportTypeEnum;
        this.grpA = grpA;
        this.grpB = grpB;
    }

    public CujoProperty getGrpA() {
        return grpA;
    }

    public CujoProperty getGrpB() {
        return grpB;
    }

    public ReportTypeEnum getReportTypeEnum() {
        return reportTypeEnum;
    }

    /** Is report type of overview ? */
    public boolean isOverview() {
        return grpB.equals(grpA);
    }

    /** Is used a user filter ? */
    public boolean isUserFilter() {
        switch (reportTypeEnum) {
            case WORK_EVENTS:
            case WORK_ATTENDANCE:
                return true;
            case WORK_OPEN_DAYS:
                return false;
            default:
                return false;
        }
    }

}
