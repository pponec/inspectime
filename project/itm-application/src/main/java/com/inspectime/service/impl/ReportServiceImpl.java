/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.service.impl;

import com.inspectime.application.client.ao.DateRange;
import com.inspectime.application.client.ao.DateRangeEnum;
import com.inspectime.application.client.ao.ReportData;
import com.inspectime.application.client.ao.ReportRequest;
import com.inspectime.application.client.cbo.CEvent;
import com.inspectime.application.client.cbo.CReport;
import com.inspectime.application.client.gui.report.ReportChartItem;
import com.inspectime.application.server.core.ServerClassConfig;
import com.inspectime.commons.bo.AbstractBo;
import com.inspectime.commons.bo.Event;
import com.inspectime.commons.bo.Task;
import com.inspectime.commons.bo.enums.RoleEnum;
import com.inspectime.service.def.EventService;
import com.inspectime.service.def.ParamCompService;
import com.inspectime.service.def.ReportService;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.ujorm.UjoProperty;
import org.ujorm.UjoPropertyList;
import org.ujorm.criterion.Criterion;
import org.ujorm.criterion.Operator;
import org.ujorm.gxt.client.AbstractCujo;
import org.ujorm.gxt.client.CujoProperty;
import org.ujorm.gxt.client.CujoPropertyList;
import org.ujorm.gxt.server.UjoTranslator;
import org.ujorm.orm.OrmHandler;
import org.ujorm.orm.Query;

/**
 * Report Service Implementation
 * @author Ponec
 */
@Transactional
@org.springframework.stereotype.Service("reportService")
public class ReportServiceImpl extends AbstractServiceImpl<Event> implements ReportService {

    static final private Logger LOGGER = Logger.getLogger(ReportServiceImpl.class.getName());

    @Autowired
    private ParamCompService paramService;

    @Autowired
    private EventService eventService;

    @Autowired
    private OrmHandler ormHandler;

    @Override
    public Class<Event> getDefaultClass() {
        return Event.class;
    }

    /** Range selector. */
    @Override
    public DateRange getRangeRequest(DateRangeEnum dateRangeEnum, Date originalDay) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTimeInMillis(originalDay.getTime());
        c2.setTimeInMillis(originalDay.getTime());

        int firstDay = paramService.getValue(paramService.firstDayOfWeek, getApplContext().getUserCompany());

        switch(dateRangeEnum) {
            case PREVIOUS_DAY:
                c1.add(Calendar.DAY_OF_YEAR, -1);
                break;
            case PREVIOUS_WEEK:
                c1.add(Calendar.WEEK_OF_YEAR, -1);
                break;
            case PREVIOUS_MONTH:
                c1.add(Calendar.MONTH, -1);
                break;
            case PREVIOUS_YEAR:
                c1.add(Calendar.YEAR, -1);
                break;
        }

        switch(dateRangeEnum) {
            case PREVIOUS_DAY:
                c2.setTimeInMillis(c1.getTimeInMillis());
                break;
            case SELECTED_WEEK:
            case PREVIOUS_WEEK:
                int currDay = c2.get(Calendar.DAY_OF_WEEK); //  Note: Calendar.SUNDAY=1
                c1.add(Calendar.DAY_OF_YEAR, (firstDay-currDay-7) % 7);
                c2.setTimeInMillis(c1.getTimeInMillis());
                c2.add(Calendar.DAY_OF_YEAR, 6);
                break;
            case SELECTED_MONTH:
            case PREVIOUS_MONTH:
                currDay = c2.get(Calendar.DAY_OF_MONTH);
                c1.add(Calendar.DAY_OF_YEAR, 1-currDay);
                c2.setTimeInMillis(c1.getTimeInMillis());
                c2.add(Calendar.MONTH, 1);
                c2.add(Calendar.DAY_OF_YEAR, -1);
                break;
            case SELECTED_YEAR:
            case PREVIOUS_YEAR:
                currDay  = c2.get(Calendar.DAY_OF_YEAR);
                c1.add(Calendar.DAY_OF_YEAR, 1-currDay);
                c2.setTimeInMillis(c1.getTimeInMillis());
                c2.add(Calendar.YEAR, 1);
                c2.add(Calendar.DAY_OF_YEAR, -1);
                break;
        }

        return new DateRange(c1.getTime(), c2.getTime());
    }

    @Override
    public ReportData getReportData(ReportRequest reportRequest, Date dateFrom, Date dateTo) {
        UjoPropertyList properties = new Event().readProperties();
        CujoPropertyList cProperties = new CEvent().readProperties();

        UjoProperty[] orderBy = new UjoProperty[reportRequest.getEventProperties().length];
        UjoProperty[] orderBase = new UjoProperty[reportRequest.getEventProperties().length];
        CujoProperty[] cOrderBase = new CujoProperty[reportRequest.getEventProperties().length];

        for (int i = orderBy.length-1; i>=0; i--) {
            String propertyString = reportRequest.getEventProperties()[i];
            UjoProperty p = properties.findIndirect(propertyString, true);
            orderBy[i] = p;

            if (AbstractBo.class.isAssignableFrom(p.getType())) {
                orderBase[i] = p;
            } else {
                int j = propertyString.lastIndexOf('.');
                propertyString = propertyString.substring(0, j);
                UjoProperty pb = properties.findIndirect(propertyString, true);
                orderBase[i] = pb;
            }

            CujoProperty cpb = cProperties.findIndirect(propertyString, true);
            cOrderBase[i] = cpb;
        }

        final Criterion<Event> crn1, crn2, crn3, crn4, crn5, crn6, criterion;
        crn1 = Criterion.where(Event.day, Operator.GE, new java.sql.Date(dateFrom.getTime()));
        crn2 = Criterion.where(Event.day, Operator.LE, new java.sql.Date(dateTo.getTime()));
        crn3 = Criterion.where(Event.active, true);
        crn4 = Criterion.where(Event.period, Operator.GT, Event.period.getDefault()); // (period>0)
        crn5 = Criterion.where(Event.task.add(Task._privateState), false);
        crn6 = getApplContext().getUser().hasAnyRole(RoleEnum.MANAGER)
             ? Criterion.where(Event._compay, getApplContext().getUserCompany())
             : Criterion.where(Event.user, getApplContext().getUser())
             ;
        criterion = crn1.and(crn2).and(crn3).and(crn4).and(crn5).and(crn6);

        Query<Event> query = getSession().createQuery(Event.class);
        query.setCriterion(criterion);
        query.orderByMany(orderBy);

        List<Event> events = query.list();
        eventService.loadLazy(events);

        ReportData result = createReport(events, orderBase, cOrderBase, reportRequest);

        return result;
    }

    /** Create report. */
    private ReportData createReport(List<Event> events, UjoProperty[] orderBase, CujoProperty[] cOrderBase, ReportRequest reportRequest) {

        CReport lastReport = null;
        int totalPeriod = 0;

        List<CReport> reports = new ArrayList<CReport>(250);
        Map<Long, AbstractCujo> mapA = new HashMap<Long, AbstractCujo>();
        Map<Long, AbstractCujo> mapB = new HashMap<Long, AbstractCujo>();
        List<ReportChartItem> chartItems = new ArrayList<ReportChartItem>(180);

        UjoProperty propertyBaseA = orderBase.length>=0 ? orderBase[0] : null ;
        UjoProperty propertyBaseB = orderBase.length>=1 ? orderBase[1] : null ;
        CujoProperty cPropertyBaseA = cOrderBase.length>=0 ? cOrderBase[0] : null ;
        CujoProperty cPropertyBaseB = cOrderBase.length>=1 ? cOrderBase[1] : null ;

        UjoTranslator<AbstractCujo> translatorA = ServerClassConfig.getInstance(ormHandler).getTranslator(cPropertyBaseA.getType(), 1);
        UjoTranslator<AbstractCujo> translatorB = ServerClassConfig.getInstance(ormHandler).getTranslator(cPropertyBaseB.getType(), 1);

        for (Event event : events) {
            
            AbstractBo boA = (AbstractBo) propertyBaseA.getValue(event);
            Long idA = boA.getId();
            if (!mapA.containsKey(idA)) {
                AbstractCujo cujoA = translatorA.translateToClient(boA);
                mapA.put(idA, cujoA);                
            }
            AbstractBo boB = (AbstractBo) propertyBaseB.getValue(event);
            Long idB = boB.getId();
            if (!mapB.containsKey(idB)) {
                AbstractCujo cujoB = translatorB.translateToClient(boB);
                mapB.put(idB, cujoB);
            }

            if (isEventKeyEqualsLastReport(lastReport, event, propertyBaseA, propertyBaseB)) {
                lastReport.addTime(event.getPeriod());
                totalPeriod += event.getPeriod();
            } else {
                lastReport = null;
            }
            
            if (lastReport==null) {
                lastReport = new CReport();
                reports.add(lastReport);

                lastReport.setIdA(idA);
                //lastReport.setLevelA(null);
                lastReport.setLevelB(mapB.get(idB));

                lastReport.addTime(event.getPeriod());
                totalPeriod += event.getPeriod();
            }
        }

        calculatePercentsAndGroups(totalPeriod, reports, chartItems, reportRequest.isSummaryType());

        ReportData result = new ReportData();
        result.setEntityMap(mapA);
        result.setReports(reports);
        result.setTotalTimeMin(totalPeriod);
        result.setType(null);
        result.setChart(chartItems);

        return result;
    }


    /** Are event keys the sama like report ? */
    private boolean isEventKeyEqualsLastReport(CReport lastReport, Event event, UjoProperty propertyBaseA, UjoProperty propertyBaseB) {
        if (lastReport==null) {
            return false;
        }
        Long idA = ((AbstractBo) propertyBaseA.getValue(event)).getId();
        boolean result = idA.equals(lastReport.getIdA());
        if (result) {
            Long idB = ((AbstractBo) propertyBaseB.getValue(event)).getId();
            result = idB.equals(lastReport.getIdB());
        }
        return result;
    }

    /** Calculate percentages nad groups */
    private void calculatePercentsAndGroups(int totalTime, List<CReport> reports, List<ReportChartItem> chartItems, boolean summary) {
        Long lastId = Long.MIN_VALUE;
        ReportChartItem chartItem = null;
        int startGrpPointer = 0;
        int grpTime = 0;

        for (int i=0, m=reports.size(); i<m; ++i) {
            final CReport report = reports.get(i);
            final int timeMin = report.getTimeMin();

            if (true) {
                // Percentages:
                final float percent = 100f * timeMin / totalTime;
                report.setPercentTotal(percent);
                if (summary) {
                    report.setPercentGroup(percent);
                }
            }

            // Groups:
            Long id = report.getIdA();
            if (!id.equals(lastId)) {
                chartItem = new ReportChartItem(id);
                chartItems.add(chartItem);
                lastId = id;

                if (!summary) {
                    for (int j=startGrpPointer; j<i; ++j) {
                        final CReport r = reports.get(j);
                        final float percent2 = 100f * r.getTimeMin() / grpTime;
                        r.setPercentGroup(percent2);
                    }
                }
                grpTime = 0;
                startGrpPointer = i;
            }
            chartItem.addValue(timeMin);
            grpTime += timeMin;
        }

        // Calculate the last Group percentage:
        if (!summary) {
            int i = reports.size();
            for (int j=startGrpPointer; j<i; ++j) {
                final CReport r = reports.get(j);
                final float percent2 = 100f * r.getTimeMin() / grpTime;
                r.setPercentGroup(percent2);
            }
        }

    }
}