/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.service.def;

import com.inspectime.commons.bo.Task;
import com.inspectime.commons.bo.User;

/**
 * User group
 * @author Ponec
 */
public interface TaskService extends AbstractService<Task> {

    /** Create default Project and Task for a new Company */
    public Task createDefaultTask(User user);

    /** Try to find the first private MyTask or first Company task by ID */
    public Task findFirstPrivateTask();

    /** Try to find the first commercial MyTask or first Company task by ID */
    public Task findFirstCommercialTask();

    /** Find first task */
    public Task findFirstTask(boolean privateTask);

}
