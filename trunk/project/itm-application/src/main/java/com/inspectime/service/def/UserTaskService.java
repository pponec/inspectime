/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */


package com.inspectime.service.def;

import com.inspectime.commons.bo.UserTask;

/**
 * User group
 * @author Ponec
 */
public interface UserTaskService extends AbstractService<UserTask> {

    /** Find the first private task */
    public UserTask findFirstTask(boolean privateTask);

    
}
