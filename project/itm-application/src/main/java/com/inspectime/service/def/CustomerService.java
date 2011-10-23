/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.service.def;

import com.inspectime.commons.bo.Customer;
import com.inspectime.commons.bo.User;

/**
 * Customer Service 
 * @author Ponec
 */
public interface CustomerService extends AbstractService<Customer> {

    /** Find or create a first Customer of the current user company. */
    public Customer getFirstCustomer(User user);

}
