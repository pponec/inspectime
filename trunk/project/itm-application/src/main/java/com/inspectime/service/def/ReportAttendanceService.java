/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.service.def;

import com.inspectime.application.client.ao.ReportData;
import com.inspectime.application.client.ao.ReportRequest;
import com.inspectime.commons.bo.Event;
import java.util.Date;

/**
 * Report Service interface
 * @author Ponec
 */
public interface ReportAttendanceService extends AbstractService<Event> {
    
    /** Returns data for a report */
    public ReportData getReportData(ReportRequest reportRequest, Date dateFrom, Date dateTo, Long userId);


}
