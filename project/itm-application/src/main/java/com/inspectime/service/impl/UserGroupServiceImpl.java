/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.service.impl;

import com.inspectime.commons.WQuery;
import com.inspectime.commons.bo.Company;
import com.inspectime.commons.bo.UserGroup;
import com.inspectime.service.def.UserGroupService;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.transaction.annotation.Transactional;
import org.ujorm.criterion.Criterion;
import org.ujorm.implementation.orm.OrmTable;

/**
 *
 * @author Hampl
 */
@Transactional
@org.springframework.stereotype.Service("userGroupService")
public class UserGroupServiceImpl extends AbstractServiceImpl<UserGroup> implements UserGroupService {

    static final private Logger LOGGER = Logger.getLogger(UserGroupServiceImpl.class.getName());

    @Override
    public Class<UserGroup> getDefaultClass() {
        return UserGroup.class;
    }

    @Override
    public <UJO extends OrmTable> List<UJO> list(WQuery query) {
        // Criterion crn = Criterion.where(UserGroup.company, getApplContext().getUserCompany()); // TODO
        // query.addCriterion(crn);
        return super.list(query);
    }

    /** Find first UserGroup. Company attribute is not implemented yet. */
    @Override
    public UserGroup findDefaultUserGroup(Company company) {
        Criterion<UserGroup> crn = Criterion.where(UserGroup.active, true);
        UserGroup result = getSession().createQuery(crn).setLimit(1).orderBy(UserGroup.id).uniqueResult();
        return result;
    }


}

