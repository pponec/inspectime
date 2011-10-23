/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.server.controller;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.inspectime.application.client.ao.DateRange;
import com.inspectime.application.client.ao.DateRangeEnum;
import com.inspectime.application.client.ao.ReportData;
import com.inspectime.application.client.ao.ReportRequest;
import com.inspectime.application.client.ao.WTime;
import com.inspectime.application.client.controller.ReportController;
import com.inspectime.service.def.EventService;
import com.inspectime.service.def.ReportAttendanceService;
import com.inspectime.service.def.ReportEventService;
import com.inspectime.service.def.ReportOpenDaysService;
import com.inspectime.service.def.ReportService;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.ujorm.gxt.client.CMessageException;
import org.ujorm.gxt.client.tools.ClientSerializableEnvelope;

/**
 * Implementation of the EventController
 * @author Ponec
 */
@org.springframework.stereotype.Controller("reportController")
public class ReportControllerImpl extends RemoteServiceServlet implements ReportController {

    final static private Logger LOGGER = Logger.getLogger(ReportControllerImpl.class.getName());

    @Autowired
    private ReportService reportService;
    @Autowired
    private ReportEventService reportEventService;
    @Autowired
    @Qualifier("reportAttendanceService")
    private ReportAttendanceService reportAttendanceService;
    @Autowired
    @Qualifier("reportOpenDaysService")
    private ReportOpenDaysService reportOpenDaysService;
    @Autowired
    private EventService eventService;


    @Override
    public DateRange getRangeRequest(DateRangeEnum rangeEnum, Date originalDay) throws CMessageException {
        eventService.assertServerContext();
        return reportService.getRangeRequest(rangeEnum, originalDay);
    }

    @Override
    public ReportData getReportData(ReportRequest type, Date dateFrom, Date dateTo, Long userId) throws CMessageException {
        eventService.assertServerContext();
        try {
            switch (type.getReportTypeEnum()) {
                case WORK_EVENTS:
                    return reportEventService.getReportData(type, dateFrom, dateTo, userId);
                case WORK_ATTENDANCE:
                    return reportAttendanceService.getReportData(type, dateFrom, dateTo, userId);
                case WORK_OPEN_DAYS:
                    return reportOpenDaysService.getReportData(type, dateFrom, dateTo, userId);
                default:
                    return reportService.getReportData(type, dateFrom, dateTo);
            }

        } catch (Throwable e) {
            LOGGER.log(Level.WARNING, "Report Error", e);
            throw new IllegalStateException("", e);
        }
    }

    @Override
    public WTime typeWorkaround4WTime(WTime o) {
        return o;
    }

    @Override
    public ClientSerializableEnvelope typeWorkaround(ClientSerializableEnvelope o) throws CMessageException {
        return o;
    }




}
