/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.service.impl;

import com.inspectime.commons.WQuery;
import com.inspectime.commons.bo.EventLock;
import com.inspectime.commons.bo.User;
import com.inspectime.commons.bo.enums.RoleEnum;
import com.inspectime.service.def.CommonService;
import com.inspectime.service.def.EventLockService;
import com.inspectime.service.def.UserService;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.ujorm.criterion.Criterion;
import org.ujorm.criterion.Operator;
import org.ujorm.implementation.orm.OrmTable;

/**
 * Event Lock Service Impl
 * @author Ponec
 */
@Transactional
@org.springframework.stereotype.Service("eventLockService")
public class EventLockServiceImpl extends AbstractServiceImpl<EventLock> implements EventLockService {

    static final private Logger LOGGER = Logger.getLogger(EventLockServiceImpl.class.getName());

    @Autowired
    private CommonService commonService;

    /** Default LockDay */
    static final java.sql.Date FIRST_DATE = User.DEFAULT_LOCK_DATE;

    @Autowired
    private UserService userService;

    @Override
    public Class<EventLock> getDefaultClass() {
        return EventLock.class;
    }



    /** Modify an Event Lock by a day to CREATE/DELETE*/
    private void setModified(EventLock bo, boolean created) {
        Date currentDate = new Date();

        if (bo.get(EventLock.user)==null) {
            bo.set(EventLock.user, getApplContext().getUser());
        }

        bo.set(EventLock.modified, currentDate);
        if (bo.get(EventLock.modifiedBy)==null) {
            bo.set(EventLock.modifiedBy, getApplContext().getUser());
        }
    }

    @Override
    public <UJO extends OrmTable> List<UJO> list(WQuery query) {
        boolean admin = getApplContext().hasPermission(RoleEnum.ADMIN);

        Criterion crn = admin
            ? Criterion.where(EventLock.user.add(User.company), getApplContext().getUserCompany())
            : Criterion.where(EventLock.user, getApplContext().getUser())
            ;
        query.addCriterion(crn);
        return super.list(query);
    }

    @Override
    public void save(EventLock bo) {
        setModified(bo, true);
        super.save(bo);
        modifyUserLock(bo);
    }

    @Override
    public void update(EventLock bo) {
        setModified(bo, false);
        super.update(bo);
        modifyUserLock(bo);
    }

    @Override
    public void delete(EventLock bo) {
        super.delete(bo);
        modifyUserLock(bo);
    }

    /** Modify the last User Lock */
    private void modifyUserLock(EventLock bo) {
        User user = bo.get(EventLock.user);
        java.sql.Date lockDate = findLastLockedDay(user);
        user.set(User.lockDate, lockDate);
        commonService.update(user);
        getApplContext().resetCache();
    }

    /** Lock required day */
    @Override
    public String lockEventDay(Date requiredDay) {
        return lockEventDay(requiredDay, getApplContext().getUser().getId());
    }

    /** Lock required day */
    @Override
    public String lockEventDay(Date requiredDay, Long userId) {

        try {
            User user = userService.loadById(userId);

            EventLock eventLock = new EventLock().init();
            eventLock.set(EventLock.user, user);
            eventLock.set(EventLock.lockDate, new java.sql.Date(requiredDay.getTime()));
            save(eventLock);

            return "";
        } catch (Throwable e) {
            String msg = "Can't lock the date " + requiredDay + " for the user no. " + userId;
            LOGGER.log(Level.SEVERE, msg, e);
            return msg;
        }
    }

    /** Returns a last Locked day for selected user */
    @Override
    public java.sql.Date findLastLockedDay(User user) {
        Criterion<EventLock> crn1, crn2, criterion;

        crn1 = Criterion.where(EventLock.user, user);
        crn2 = Criterion.where(EventLock.active, true);
        criterion = crn1.and(crn2);

        EventLock result = getSession().createQuery(criterion).orderBy(EventLock.lockDate.descending()).setLimit(1).uniqueResult();

        return result!=null ? result.get(EventLock.lockDate) : FIRST_DATE;
    }

    /** Returns a last Locked day for selected user */
    @Override
    public boolean isLockedDay(java.sql.Date currentDay) {
        User user = getApplContext().getUser();
        return isLockedDay(currentDay, user);
    }
    
    /** Returns a last Locked day for selected user */
    @Override
    public boolean isLockedDay(java.sql.Date currentDay, User user) {
        final boolean optimization = true;

        if (optimization) {
            return currentDay.compareTo(user.get(User.lockDate)) <= 0;
        } else {
            Criterion<EventLock> crn1, crn2, crn3, criterion;

            crn1 = Criterion.where(EventLock.user, user);
            crn2 = Criterion.where(EventLock.active, true);
            crn3 = Criterion.where(EventLock.lockDate, Operator.GE, currentDay);
            criterion = crn1.and(crn2).and(crn3);

            EventLock result = getSession().createQuery(criterion).setLimit(1).uniqueResult();
            return result != null;
        }
    }



}

