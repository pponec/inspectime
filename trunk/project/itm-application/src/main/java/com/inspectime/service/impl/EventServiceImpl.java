/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.service.impl;

import com.inspectime.application.client.ao.EventDay;
import com.inspectime.application.client.gui.liveEvent.LiveEventTable;
import com.inspectime.commons.WQuery;
import com.inspectime.commons.bo.Account;
import com.inspectime.commons.bo.Customer;
import com.inspectime.commons.bo.Event;
import com.inspectime.commons.bo.Product;
import com.inspectime.commons.bo.Project;
import com.inspectime.commons.bo.Task;
import com.inspectime.commons.bo.User;
import com.inspectime.commons.bo.item.TimeZone;
import com.inspectime.service.def.EventLockService;
import com.inspectime.service.def.EventService;
import com.inspectime.service.def.LiveEventService;
import com.inspectime.service.def.UserService;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.ujorm.criterion.Criterion;
import org.ujorm.gxt.client.ao.ValidationException;
import org.ujorm.implementation.orm.OrmTable;
import org.ujorm.orm.utility.OrmTools;

/**
 * Event Service impl
 * @author Pavel Ponec
 */
@Transactional
@org.springframework.stereotype.Service("eventService")
public class EventServiceImpl extends AbstractServiceImpl<Event> implements EventService {

    static final private Logger LOGGER = Logger.getLogger(EventServiceImpl.class.getName());

    @Autowired
    private LiveEventService liveEventService;

    @Autowired
    private UserService userService;

    @Autowired
    private EventLockService eventLockService;

    @Override
    public Class<Event> getDefaultClass() {
        return Event.class;
    }

    /** Assert that event is unlocked ? */
    private void assertUnlocked(Event event) {
        User user = event.get(Event.user);
        if (user==null) {
            user = getApplContext().getUser();
        }
        boolean locked = eventLockService.isLockedDay(event.get(Event.day), user);
        if (locked) {
            throw new IllegalStateException("Sorry, the Event is locked (" + event.get(Event.id) + ")");
        }
    }

    /** Assert that event is unlocked ? */
    private void validateForInsert(Event event) {

        Task task = getSession().load(Task.class, event.get(Event.task).getId());
        assertTrue(Boolean.TRUE.equals(task.get(Task.active)), "The assigned Task was removed");
        assertTrue(Boolean.FALSE.equals(task.get(Task.finished)), "The assigned Task was finished");

        Project project = task.get(Task.project);
        assertTrue(Boolean.TRUE.equals(project.get(Project.active)), "The assigned Project was removed");
        assertTrue(Boolean.FALSE.equals(project.get(Project.finished)), "The assigned Project was finished");

        Product product = project.get(Project.product);
        assertTrue(Boolean.TRUE.equals(product.get(Product.active)), "The assigned Product was removed");
        //assertExpr(Boolean.FALSE.equals(product.get(Product.finished)), "The assigned Product was finished");

        Customer customer = project.get(Project.customer);
        assertTrue(Boolean.TRUE.equals(customer.get(Customer.active)), "The assigned Customer was removed");

        Account account = task.get(Task.account);
        assertTrue(Boolean.TRUE.equals(account.get(Account.active)), "The assigned Account was removed");
        //assertExpr(Boolean.FALSE.equals(account.get(account.finished)), "The assigned Project was finished");

    }


    /** Assert the Expression */
    private void assertTrue(boolean assertion, String errorMsg) {
        if (!assertion) {
            throw new ValidationException(errorMsg);
        }
    }

    /** Metoda nedopočítává Event.period
     * @see #list(org.ujorm.orm.Query)
     */
    @Override
    public void save(Event bo) {
        
        if (bo.get(Event.user)==null) {
            bo.set(Event.user, getApplContext().getUser());
        }

        // Save UTC time:
        final TimeZone timeZone = new TimeZone(getApplContext().getClientTimeHoursOffset());
        bo.set(Event.timeZone, timeZone);
        bo.set(Event.utcDayTime, getUtcDateTime(bo));
        
        LOGGER.info("Saving new event: user="+bo.getUserId()+"("+bo.get(Event.user).getName()+"), locTime="+new Date()+", utcTime="+bo.get(Event.utcDayTime)+", timeZone="+bo.get(Event.timeZone)+", desc="+bo.get(Event.description));
        LOGGER.info(" - Client time offset="+getApplContext().getClientTimeOffset());
        LOGGER.info(" - Server time offset="+(getApplContext().getServerTimeOffset()/1000.0/60.0));

        assertUnlocked(bo);
        validateForInsert(bo);
        super.save(bo);
    }


    /** Metoda nedopočítává Event.period
     * @see #list(org.ujorm.orm.Query)
     */
    @Override
    public void update(Event bo) {
        // Modify the UTC time (?)

        User usr = bo.get(Event.user);
        if (usr!=null) {
            usr = userService.loadById(usr.getId());
            final TimeZone timeZone = usr.get(User.timeZone);
            bo.set(Event.utcDayTime, getUtcDateTime(bo));
        }

        assertUnlocked(bo);
        super.update(bo);
    }

    /** Returns UTC dateTime for the current user */
    private java.util.Date getUtcDateTime(Event bo) {
        Calendar calendar = GregorianCalendar.getInstance();

        calendar.setTime(bo.get(Event.day));
        calendar.add(Calendar.MINUTE, bo.get(Event.startTime).intValue());
        calendar.add(Calendar.MINUTE, - getApplContext().getClientTimeOffset());

        return calendar.getTime();
    }

    /** Return Event include calculated Event.period */
    @Override
    public <UJO extends OrmTable> List<UJO> list(WQuery query) {

        if (LiveEventTable.LIVE_CONTEXT.equals(query.getContext())) {
            final List<Event> result = liveEventService.list(query);
            loadLazy(result);
            return (List<UJO>) result;
        }

        query.getQuery().setSession(getSession());
        User user = getApplContext().getUser();
        Criterion crnNew  = Criterion.where(Event.user, user);
        query.addCriterion(crnNew);
        query.getQuery().orderBy(Event.startTime, Event.id);
        List<Event> result = (List) super.list(query);
        loadLazy(result);

        // Calculate periods:
        if (result.size()>0) {
            int lastItem = result.size()-1;
            short lastStart = result.get(lastItem).get(Event.startTime);
            for (int i = lastItem; i>=0; --i) {
                Event event = result.get(i);
                short currentStart = event.get(Event.startTime);
                short period = (short) (lastStart - currentStart);
                boolean change = period != event.get(Event.period).shortValue();
                event.set(Event.period, period);
                lastStart = currentStart;
                if (change) {
                    getSession().update(event);
                }
            }
        }

        return (List<UJO>) result;
    }

    /** Load Projects and Accounts */
    @Override
    public void loadLazy(List<Event> events) {
        List<Task> tasks = new ArrayList<Task>(events.size());
        for (Event event : events) {
            Task newTask = event.get(Event.task);
            int i;
            for (i=tasks.size()-1; i>=0 && tasks.get(i)!=newTask; --i) {}
            if (i<0) {
                tasks.add(newTask);
            }
        }
        OrmTools.loadLazyValuesAsBatch(tasks, Task.project);
        OrmTools.loadLazyValuesAsBatch(tasks, Task.account);
    }

    /** Get Properties of the Event Day */
    @Override
    public EventDay getEventDayProperties(Date selectedDate) {
        EventDay result = new EventDay();

        boolean locked = eventLockService.isLockedDay(new java.sql.Date(selectedDate.getTime()));
        result.setLocked(locked);

        return result;
    }

    /** Delete the row */
    @Override
    public void delete(Event bo) {
        assertUnlocked(bo);
        super.delete(bo);
    }

    /** Is the day empty of is closed for a logged user ? 
     * the NULL value means NO-EVENT, TRUE means closed, FALSE means opened working day
     */
    @Override
    public Boolean isClosedDay(java.util.Date day) {
        final Criterion<Event> crn1, crn2, crn3;

        crn1 = Criterion.where(Event.active, true);
        crn2 = Criterion.where(Event.user, getApplContext().getUser());
        crn3 = Criterion.where(Event.day, new java.sql.Date(day.getTime()));

        Event event = getSession()
                .createQuery(crn1.and(crn2).and(crn3))
                .orderBy( Event.startTime.descending()
                        , Event.id.descending())
                .setLimit(1)
                .uniqueResult()
                ;
        Boolean result = event==null ? null : event.isPrivate();
        return result;
    }

}
