/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.service.impl;

import com.inspectime.application.client.ao.CReportWorkDay;
import com.inspectime.commons.bo.Account;
import com.inspectime.commons.bo.Event;
import com.inspectime.commons.bo.Task;
import com.inspectime.commons.bo.User;
import com.inspectime.service.def.ReportOpenDaysService;
import java.util.Date;
import java.util.logging.Logger;
import org.springframework.transaction.annotation.Transactional;
import org.ujorm.criterion.Criterion;
import org.ujorm.criterion.Operator;

/**
 * Report Service Implementation
 * @author Ponec
 */
@Transactional
@org.springframework.stereotype.Service("reportOpenDaysService")
public class ReportOpenDaysServiceImpl extends ReportAttendanceServiceImpl implements ReportOpenDaysService {

    static final private Logger LOGGER = Logger.getLogger(ReportOpenDaysServiceImpl.class.getName());

    /** Create Criterion for all active users - include private events: */
    @Override
    protected Criterion<Event> createCriterion(Long userId, User currentUser, Date dateFrom, Date dateTo) {
        final Criterion<Event> crn1, crn2, crn3, crn4, crn5, crn6, crn7, criterion;

        crn1 = Criterion.where(Event.active, true);
        crn2 = Criterion.where(Event.user.add(User.active), true); // For all active users
        crn3 = Criterion.where(Event.user.add(User.company), currentUser.getCompany());
        crn4 = Criterion.where(Event.day, Operator.GE, new java.sql.Date(dateFrom.getTime()));
        crn5 = Criterion.where(Event.day, Operator.LE, new java.sql.Date(dateTo.getTime()));
        // Private=true OR Period>0 :
        crn6 = Criterion.where(Event.task.add(Task.account).add(Account.privateState), true); // Private events
        crn7 = Criterion.where(Event.period, Operator.GT, Event.period.getDefault()); // (Period>0)

        criterion = crn1.and(crn2).and(crn3).and(crn4).and(crn5).and( crn6.or(crn7) ) ;
        return criterion;
    }

    /** Print reports whitch is not closed */
    @Override
    protected boolean isAllowedToReport(CReportWorkDay reportWorkDay) {
        return ! CReportWorkDay.closed.getValue(reportWorkDay);
    }


}