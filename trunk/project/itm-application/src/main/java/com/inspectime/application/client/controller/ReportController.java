/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */
package com.inspectime.application.client.controller;

import com.google.gwt.user.client.rpc.RemoteService;
import com.inspectime.application.client.ao.DateRange;
import com.inspectime.application.client.ao.DateRangeEnum;
import com.inspectime.application.client.ao.ReportData;
import com.inspectime.application.client.ao.ReportRequest;
import com.inspectime.application.client.ao.WTime;
import java.util.Date;
import org.ujorm.gxt.client.CMessageException;
import org.ujorm.gxt.client.tools.ClientSerializableEnvelope;

/**
 * Service for ReportData
 * @author Ponec
 */
public interface ReportController extends RemoteService {

    /** Get a common reports */
    public ReportData getReportData(ReportRequest type, Date dateFrom, Date dateTo, Long userId) throws CMessageException;

    /** Get a Range request */
    public DateRange getRangeRequest(DateRangeEnum rangeEnum, Date currentDay) throws CMessageException;

    /* An workaround for the GXT serialization */
    public ClientSerializableEnvelope typeWorkaround(ClientSerializableEnvelope o) throws CMessageException;

    /* An workaround for the GXT serialization of WTime */
    public WTime typeWorkaround4WTime(WTime o);

}


