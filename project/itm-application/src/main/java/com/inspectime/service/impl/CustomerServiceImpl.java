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
import com.inspectime.commons.bo.Customer;
import com.inspectime.commons.bo.User;
import com.inspectime.service.def.CustomerService;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.transaction.annotation.Transactional;
import org.ujorm.criterion.Criterion;
import org.ujorm.implementation.orm.OrmTable;

/**
 * Customer Service Implementation
 * @author Ponec
 */
@Transactional
@org.springframework.stereotype.Service("customerService")
public class CustomerServiceImpl extends AbstractServiceImpl<Customer> implements CustomerService {

    static final private Logger LOGGER = Logger.getLogger(CustomerServiceImpl.class.getName());

    @Override
    public Class<Customer> getDefaultClass() {
        return Customer.class;
    }

    private void setModified(Customer bo, boolean created) {
        Date currentDate = new Date();

        if (created) {
            bo.set(Customer.created, currentDate);
            if (bo.get(Customer.createdBy) == null) {
                bo.set(Customer.createdBy, getApplContext().getUser());
            }
            if (bo.get(Customer.company) == null) {
                Company company = getApplContext().getUser().getCompany();
                bo.set(Customer.company, company);
            }
        }
        bo.set(Customer.modified, currentDate);
        if (bo.get(Customer.modifiedBy) == null) {
            bo.set(Customer.modifiedBy, getApplContext().getUser());
        }

    }

    @Override
    public void save(Customer bo) {
        setModified(bo, true);
        if (bo.get(Customer.company) == null) {
            bo.set(Customer.company, getApplContext().getUserCompany());
        }
        super.save(bo);
    }

    @Override
    public void update(Customer bo) {
        setModified(bo, false);
        super.update(bo);
    }

    @Override
    public <UJO extends OrmTable> List<UJO> list(WQuery query) {
        Criterion crn = Criterion.where(Customer.company, getApplContext().getUserCompany());
        query.addCriterion(crn);
        return super.list(query);
    }

    /** Find or create a first Customer of the current user company. */
    @Override
    public Customer getFirstCustomer(User user) {
        Company company = user != null ? user.getCompany() : getApplContext().getUserCompany();
        Customer firstCustomer = getSession().createQuery(Customer.class).setLimit(1).orderBy(Customer.id).uniqueResult();
        if (firstCustomer == null) {
            Date date = new Date();
            firstCustomer = new Customer();
            firstCustomer.set(Customer.company, company);
            firstCustomer.set(Customer.name, "-");
            firstCustomer.set(Customer.createdBy, user);
            firstCustomer.set(Customer.modifiedBy, user);
            firstCustomer.set(Customer.created, date);
            firstCustomer.set(Customer.modified, date);
            getSession().save(firstCustomer);
        }
        return firstCustomer;
    }
}
