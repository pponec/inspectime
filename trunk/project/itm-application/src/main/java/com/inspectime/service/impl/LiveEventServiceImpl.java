/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.service.impl;

import java.util.Comparator;
import org.ujorm.core.UjoComparator;
import java.util.logging.Level;
import org.ujorm.orm.Session;
import com.inspectime.commons.WQuery;
import com.inspectime.commons.bo.Company;
import com.inspectime.commons.bo.Event;
import com.inspectime.commons.bo.User;
import com.inspectime.service.def.LiveEventService;
import com.inspectime.service.def.UserService;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.ujorm.criterion.Criterion;
import org.ujorm.implementation.orm.OrmTable;
import static org.ujorm.criterion.Operator.*;

/**
 * Event Service impl
 * @author Pavel Ponec
 */
@Transactional
@org.springframework.stereotype.Service("liveEventService")
public class LiveEventServiceImpl extends AbstractServiceImpl<Event> implements LiveEventService {

    static final private Logger LOGGER = Logger.getLogger(LiveEventServiceImpl.class.getName());
    /** Is not the user deleted? */
    protected static final Criterion<Event> crnActive = Criterion.where(Event.active, true);
    @Autowired
    private UserService userService;

    @Override
    public Class<Event> getDefaultClass() {
        return Event.class;
    }

    /** Metoda nedopočítává Event.period
     * @see #list(org.ujorm.orm.Query)
     */
    @Override
    public void save(Event bo) {
        throw new UnsupportedOperationException("Can't save the Live Event");
    }

    /** Metoda nedopočítává Event.period
     * @see #list(org.ujorm.orm.Query)
     */
    @Override
    public void update(Event bo) {
        throw new UnsupportedOperationException("Can't update the Live Event");
    }

    /** Return Event include calculated Event.period */
    @Override
    public <UJO extends OrmTable> List<UJO> list(WQuery query) {

        Company myCompany = getApplContext().getUserCompany();
        List<User> users = userService.getUserListForCompany(myCompany);
        List<Event> result = new ArrayList<Event>(users.size());

        Date utcCurrentTime = getCurrentTimeUTC();

        Criterion<Event> crnTime, crnActive, crnUser, criterion;
        crnTime = Criterion.where(Event.utcDayTime, LE, utcCurrentTime);
        crnActive = Criterion.where(Event.active, true);
        Session session = getSession();

        for (User user : users) {
            crnUser = Criterion.where(Event.user, user);
            criterion = crnUser.and(crnTime).and(crnActive);

            Event event = session.createQuery(criterion).setLimit(1).orderBy(Event.utcDayTime.descending()).uniqueResult();
            if (event!=null) {
                long period = (utcCurrentTime.getTime() - event.get(Event.utcDayTime).getTime()) / (1000 * 60) ;
                if (period < 0L) {
                    period = 0L;
                    LOGGER.log(Level.WARNING, "The period must not be negative: {0} for the Event: {1}", new Object[]{period, event});
                }
                if (period > Short.MAX_VALUE) {
                    period = Short.MAX_VALUE;
                }

                event.set(Event.period, (short) period);
                result.add(event);
            }
        }

        // Sorting:
        Comparator comp = UjoComparator.newInstance
                ( Locale.ENGLISH
                , Collator.PRIMARY
                , query.getQuery().getOrderAsArray());
        Collections.sort(result, comp);
        
        return (List<UJO>) result;
    }

    /** Get Current time on  Zone. */
    private Date getCurrentTimeUTC() {
        final TimeZone tz = TimeZone.getTimeZone("GMT");
        long ct = System.currentTimeMillis();
        int delta = TimeZone.getDefault().getOffset(ct) - tz.getOffset(ct);
        return new Date(ct - delta);
    }



};
