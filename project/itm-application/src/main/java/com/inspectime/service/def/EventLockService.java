/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.service.def;

import com.inspectime.commons.bo.EventLock;
import com.inspectime.commons.bo.User;
import java.util.Date;

/**
 * User group
 * @author Ponec
 */
public interface EventLockService extends AbstractService<EventLock> {

    /** Lock required day */
    public String lockEventDay(Date requiredDay);

    /** Lock required day */
    public String lockEventDay(Date requiredDay, Long userId);

    /** Returns a last Locked day for selected user */
    public Date findLastLockedDay(User user) ;

    /** Returns a last Locked day for selected user */
    public boolean isLockedDay(java.sql.Date currentDay, User user);

    /** Returns a last Locked day for a current user */
    public boolean isLockedDay(java.sql.Date currentDay);


}
