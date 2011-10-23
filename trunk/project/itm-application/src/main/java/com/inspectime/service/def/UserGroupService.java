/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.service.def;

import com.inspectime.commons.bo.Company;
import com.inspectime.commons.bo.UserGroup;

/**
 * User group
 * @author Ponec
 */
public interface UserGroupService extends AbstractService<UserGroup> {

    /** Find default user Group of Company */
    public UserGroup findDefaultUserGroup(Company company);
}
