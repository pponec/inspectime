/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.service.def;

import com.inspectime.commons.bo.User;

/**
 *
 * @author Hampl
 */
public interface InitDataService extends AbstractService<User> {

    /** Create demo data for the first user of a company. If no-empty project list is found than do nothing. */
    public void createDemoData();

}
