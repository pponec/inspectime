/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.service.impl;

import com.inspectime.application.client.ao.ReportData;
import com.inspectime.application.client.ao.CReportWorkDay;
import com.inspectime.application.client.ao.ReportRequest;
import com.inspectime.commons.TaskTime;
import com.inspectime.application.client.ao.WTime;
import com.inspectime.application.client.cbo.CReport;
import com.inspectime.application.client.gui.report.ReportChartItem;
import com.inspectime.commons.bo.Account;
import com.inspectime.commons.bo.Event;
import com.inspectime.commons.bo.Task;
import com.inspectime.commons.bo.User;
import com.inspectime.service.def.EventService;
import com.inspectime.service.def.ParamCompService;
import com.inspectime.service.def.ReportAttendanceService;
import java.util.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.ujorm.criterion.Criterion;
import org.ujorm.criterion.Operator;
import org.ujorm.gxt.client.AbstractCujo;
import org.ujorm.orm.OrmHandler;
import org.ujorm.orm.Query;

/**
 * Report Service Implementation
 * @author Ponec
 */
@Transactional
@org.springframework.stereotype.Service("reportAttendanceService")
public class ReportAttendanceServiceImpl extends AbstractReportEventServiceImpl implements ReportAttendanceService {

    static final private Logger LOGGER = Logger.getLogger(ReportAttendanceServiceImpl.class.getName());

    @Autowired
    protected ParamCompService paramService;

    @Autowired
    protected EventService eventService;

    @Autowired
    protected OrmHandler ormHandler;

    /** Report for the one selected user */
    @Override
    public ReportData getReportData(ReportRequest reportRequest, java.util.Date dateFrom, java.util.Date dateTo, Long userId) {

        User currentUser = getApplContext().getUser();
        if (userId==null) {
            userId = currentUser.getId();
        }
        Criterion<Event> criterion = createCriterion(userId, currentUser, dateFrom, dateTo);

        Query<Event> query = getSession().createQuery(Event.class);
        query.setCriterion(criterion);
        query.orderByMany(Event.day, Event.user.add(User.name), Event.startTime);

        List<Event> events = query.list();
        eventService.loadLazy(events);

        ReportData result = createReport(events);

        return result;
    }


    /** Create Criterion - include private events: */
    protected Criterion<Event> createCriterion(Long userId, User currentUser, Date dateFrom, Date dateTo) {
        final Criterion<Event> crn1, crn2, crn3, crn4, crn5, crn6, crn7, criterion;

        crn1 = Criterion.where(Event.active, true);
        crn2 = Criterion.where(Event.user.add(User.id), userId);
        crn3 = Criterion.where(Event.user.add(User.company), currentUser.getCompany());
        crn4 = Criterion.where(Event.day, Operator.GE, new java.sql.Date(dateFrom.getTime()));
        crn5 = Criterion.where(Event.day, Operator.LE, new java.sql.Date(dateTo.getTime()));
        // Private=true OR Period>0 :
        crn6 = Criterion.where(Event.task.add(Task.account).add(Account.privateState), true); // Private events
        crn7 = Criterion.where(Event.period, Operator.GT, Event.period.getDefault()); // (Period>0)

        criterion = crn1.and(crn2).and(crn3).and(crn4).and(crn5).and( crn6.or(crn7) ) ;
        return criterion;
    }

    /** The last Event of the day */
    private void makeLastEvent
            ( final CReportWorkDay reportWorkDay
            , final Map<Long, TaskTime> tasks
            , final int dayWorkTime
            , final List<CReport> reports) {
        reportWorkDay.setDayWorkTime(dayWorkTime);

        // Create task names:
        TaskTime[] taskArray = tasks.values().toArray(new TaskTime[tasks.size()]);
        Arrays.sort(taskArray);
        StringBuilder taskNames = new StringBuilder();
        for (TaskTime taskTime : taskArray) {
            taskTime.exportTo(taskNames);
        }
        reportWorkDay.set(CReportWorkDay.taskNames, taskNames.toString());
        tasks.clear();

        // Add this item to reports:
        if (isAllowedToReport(reportWorkDay)) {
            CReport report = new CReport();
            report.setLevelB(reportWorkDay);
            reports.add(report);
        }
    }

    /** Is allowd the report work day to reprot ? */
    protected boolean isAllowedToReport(CReportWorkDay reportWorkDay) {
        return true;
    }

    /** Create the report */
    protected ReportData createReport(List<Event> events) {

        List<CReport> reports = new ArrayList<CReport>(events.size());
        Map<Long, AbstractCujo> mapA = new HashMap<Long, AbstractCujo>();
        List<ReportChartItem> chartItems = new ArrayList<ReportChartItem>(180);
        int totalPeriod = 0;

        // -----------------------------------
        
        final java.sql.Date UNDEFINED_DAY = new java.sql.Date(0);
        java.sql.Date lastDay = UNDEFINED_DAY;
        long lastUserId = Long.MIN_VALUE;
        CReportWorkDay reportWorkDay = null;
        int dayWorkTime = 0;
        Map<Long,TaskTime> tasks = new HashMap<Long,TaskTime>();

        for (Event event : events) {
            if (lastUserId != event.getUserId().longValue()) {
                lastUserId = event.getUserId().longValue();
                lastDay = UNDEFINED_DAY;
            }
            if(!lastDay.equals(Event.day.of(event))) {
                lastDay = Event.day.of(event);
              
                if (reportWorkDay!=null) {
                    makeLastEvent(reportWorkDay, tasks, dayWorkTime, reports);
                    totalPeriod = totalPeriod + dayWorkTime;
                    dayWorkTime = 0;
                }
                // Create new work day:
                reportWorkDay = new CReportWorkDay();
                reportWorkDay.set(CReportWorkDay.user, event.get(Event.user.add(User.name)));
                reportWorkDay.set(CReportWorkDay.day, event.get(Event.day));
                reportWorkDay.set(CReportWorkDay.arrival, new WTime(event.getStartTime()));
            }

            // Last Event:
            reportWorkDay.set(CReportWorkDay.departure, new WTime(event.getEndTime()));
            reportWorkDay.set(CReportWorkDay.closed, event.isPrivate());

            if (!event.isPrivate()) {
                // Add the work time:
                dayWorkTime += event.getPeriod();

                // Task list:
                Task task = event.get(Event.task);
                TaskTime tt = tasks.get(event.getId());
                if (tt==null) {
                    tasks.put(event.getId(), tt = new TaskTime(task));
                }
                tt.addTime(event);
            }
        }

        if (reportWorkDay!=null) {
            makeLastEvent(reportWorkDay, tasks, dayWorkTime, reports);
        }

        // -----------------------------------

        ReportData result = new ReportData();
        result.setEntityMap(mapA);
        result.setReports(reports);
        result.setTotalTimeMin(totalPeriod);
        result.setType(null);
        result.setChart(chartItems);

        return result;
    }

}