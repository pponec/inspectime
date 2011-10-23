/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.service.impl;

import com.inspectime.commons.WQuery;
import com.inspectime.commons.bo.Account;
import com.inspectime.service.def.AccountService;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.transaction.annotation.Transactional;
import org.ujorm.criterion.Criterion;
import org.ujorm.implementation.orm.OrmTable;

/**
 * Account Service Implementation
 * @author Ponec
 */
@Transactional
@org.springframework.stereotype.Service("accountService")
public class AccountServiceImpl extends AbstractServiceImpl<Account> implements AccountService {

    static final private Logger LOGGER = Logger.getLogger(AccountServiceImpl.class.getName());

    /** Is not the user deleted? */
    protected static final Criterion<Account> crnActive = Criterion.where(Account.active, true);

    @Override
    public Class<Account> getDefaultClass() {
        return Account.class;
    }


    private void setModified(Account bo, boolean created) {
        Date currentDate = new Date();

        if (created) {
            bo.set(Account.created, currentDate);
            if (bo.get(Account.createdBy)==null) {
                bo.set(Account.createdBy, getApplContext().getUser());
            }
            bo.set(Account.company, bo.get(Account.createdBy).getCompany());
        }
        bo.set(Account.modified, currentDate);
        if (bo.get(Account.modifiedBy)==null) {
            bo.set(Account.modifiedBy, getApplContext().getUser());
        }
    }

    @Override
    public <UJO extends OrmTable> List<UJO> list(WQuery query) {
        Criterion crn = Criterion.where(Account.company, getApplContext().getUserCompany());
        query.addCriterion(crn);
        return super.list(query);
    }

    @Override
    public void save(Account bo) {
        setModified(bo, true);
        super.save(bo);
    }

    @Override
    public void update(Account bo) {
        setModified(bo, false);
        super.update(bo);
    }

}

