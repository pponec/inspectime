/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.service.impl;

import com.inspectime.commons.bo.Company;
import com.inspectime.commons.bo.Customer;
import com.inspectime.service.def.CompanyService;
import java.util.logging.Logger;
import org.springframework.transaction.annotation.Transactional;
import org.ujorm.criterion.Criterion;

/**
 * Customer Service Implementation
 * @author Ponec
 */
@Transactional
@org.springframework.stereotype.Service("companyService")
public class CompanyServiceImpl extends AbstractServiceImpl<Company> implements CompanyService {

    static final private Logger LOGGER = Logger.getLogger(CompanyServiceImpl.class.getName());
    
    /** Is not the user deleted? */
    protected static final Criterion<Customer> crnActive = Criterion.where(Customer.active, true);

    @Override
    public Class<Company> getDefaultClass() {
        return Company.class;
    }

    @Override
    public int getNextTaskSequence() {
        Company company = getApplContext().getUserCompany();
        getSession().reload(company);
        int result = company.nextTaskCodeSeq();
        update(company);
        return result;
    }

}
