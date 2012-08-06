/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.service.impl;

import com.inspectime.application.client.ao.ReportData;
import com.inspectime.application.client.ao.ReportRequest;
import com.inspectime.application.client.cbo.CEvent;
import com.inspectime.application.client.cbo.CProject;
import com.inspectime.application.client.cbo.CReport;
import com.inspectime.application.client.cbo.CTask;
import com.inspectime.application.client.gui.report.ReportChartItem;
import com.inspectime.application.server.core.ServerClassConfig;
import com.inspectime.commons.bo.AbstractBo;
import com.inspectime.commons.bo.Account;
import com.inspectime.commons.bo.Event;
import com.inspectime.commons.bo.Task;
import com.inspectime.commons.bo.User;
import com.inspectime.service.def.EventService;
import com.inspectime.service.def.ParamCompService;
import com.inspectime.service.def.ReportEventService;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.ujorm.Key;
import org.ujorm.criterion.Criterion;
import org.ujorm.criterion.Operator;
import org.ujorm.gxt.client.AbstractCujo;
import org.ujorm.gxt.server.UjoTranslator;
import org.ujorm.orm.OrmHandler;
import org.ujorm.orm.Query;

/**
 * Report Service Implementation
 * @author Ponec
 */
@Transactional
@org.springframework.stereotype.Service("reportEventService")
public class ReportEventServiceImpl extends AbstractReportEventServiceImpl implements ReportEventService {

    static final private Logger LOGGER = Logger.getLogger(ReportEventServiceImpl.class.getName());

    @Autowired
    protected ParamCompService paramService;

    @Autowired
    protected EventService eventService;

    @Autowired
    protected OrmHandler ormHandler;

    /** Report for the one selected user */
    @Override
    public ReportData getReportData(ReportRequest reportRequest, Date dateFrom, Date dateTo, Long userId) {

        User currentUser = getApplContext().getUser();
        if (userId==null) {
            userId = currentUser.getId();
        }
        Criterion<Event> criterion = createCriterion(userId, currentUser, dateFrom, dateTo);

        Query<Event> query = getSession().createQuery(Event.class);
        query.setCriterion(criterion);
        query.orderByMany(Event.day, Event.user.add(User.name), Event.startTime, Event.id);

        List<Event> events = query.list();
        eventService.loadLazy(events);

        ReportData result = createReport(events);

        return result;
    }

    /** Create Criterion */
    public Criterion<Event> createCriterion(Long userId, User currentUser, Date dateFrom, Date dateTo) {
        final Criterion<Event> crn1, crn2, crn3, crn4, crn5, crn6, crn7, criterion;

        crn1 = Criterion.where(Event.active, true);
        crn2 = Criterion.where(Event.user.add(User.id), userId);
        crn3 = Criterion.where(Event.user.add(User.company), currentUser.getCompany());
        crn4 = Criterion.where(Event.day, Operator.GE, new java.sql.Date(dateFrom.getTime()));
        crn5 = Criterion.where(Event.day, Operator.LE, new java.sql.Date(dateTo.getTime()));
        crn6 = Criterion.where(Event.period, Operator.GT, Event.period.getDefault()); // (Period>0)
        crn7 = Criterion.where(Event.task.add(Task.account).add(Account.privateState), false); // only Commercial events

        criterion = crn1.and(crn2).and(crn3).and(crn4).and(crn5).and(crn6);

        final boolean showPrivateEvents = paramService.get(paramService.reportShowsPrivateEvents);
        
        if (showPrivateEvents) {
            return criterion;
        } else {
            return criterion.and(crn7);
        }
    }

    protected ReportData createReport(List<Event> events) {

        List<CReport> reports = new ArrayList<CReport>(events.size());
        Map<Long, AbstractCujo> mapA = new HashMap<Long, AbstractCujo>();
        List<ReportChartItem> chartItems = new ArrayList<ReportChartItem>(180);
        int totalPeriod = 0;

        Key propertyBaseA = Event.task.add(Task.project);

        UjoTranslator<AbstractCujo> translatorA = ServerClassConfig.getInstance(ormHandler).getTranslator(CProject.class, 0);
        UjoTranslator<AbstractCujo> translatorB = ServerClassConfig.getInstance(ormHandler).getTranslator(CEvent.class, 0);
        UjoTranslator<AbstractCujo> translatorT = ServerClassConfig.getInstance(ormHandler).getTranslator(CTask.class, 0);

        // -----------------------------------

        for (Event event : events) {

            AbstractBo boA = (AbstractBo) propertyBaseA.getValue(event);
            Long idA = boA.getId();
            if (!mapA.containsKey(idA)) {
                AbstractCujo cujoA = translatorA.translateToClient(boA);
                mapA.put(idA, cujoA);
            }
            CEvent cEvent = (CEvent) translatorB.translateToClient(event);
            CTask  cTask  = (CTask)  translatorT.translateToClient(event.get(Event.task));
            cEvent.set(CEvent.task, cTask);

            CReport report = new CReport();
            reports.add(report);
            report.setIdA(idA);
            report.setLevelB(cEvent);
            totalPeriod = totalPeriod+event.getPeriod();
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