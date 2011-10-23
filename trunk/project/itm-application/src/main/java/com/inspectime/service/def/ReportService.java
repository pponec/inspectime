/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.service.def;

import com.inspectime.application.client.ao.DateRange;
import com.inspectime.application.client.ao.DateRangeEnum;
import com.inspectime.application.client.ao.ReportData;
import com.inspectime.application.client.ao.ReportRequest;
import com.inspectime.commons.bo.Event;
import java.util.Date;

/**
 * Report Service interface
 * @author Ponec
 */
public interface ReportService extends AbstractService<Event> {
    
    /** Calculate RangeRequest */
    public DateRange getRangeRequest(DateRangeEnum dateRangeEnum, Date rangeEnum);

    /** Returns data for a report */
    public ReportData getReportData(ReportRequest type, Date dateFrom, Date dateTo);

    


}
