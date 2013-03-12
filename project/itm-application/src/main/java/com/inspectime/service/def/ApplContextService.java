/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.service.def;

import com.inspectime.commons.bo.Company;
import com.inspectime.commons.bo.Product;
import com.inspectime.commons.bo.User;
import com.inspectime.commons.bo.enums.RoleEnum;
import java.util.Date;

/**
 * User application context
 * @author Ponec
 */
public interface ApplContextService  {

    /** Return loged user bo (null if not loged)*/
    public User getUser() ;

    /** Is user logged */
    public boolean isLogged();

    /** Returns logged users company */
    public Company getUserCompany();

    /** Returns the first Product */
    public Product getDefaultProduct();

    /** Reset sessio cache */
    public void resetCache();

    /** Test to a roles */
    public boolean hasPermission(RoleEnum ... roleEnum);

    /** The Client Time Offset [Minutes]  */
    public short getClientTimeOffset();

    public int getServerTimeOffset();

    /** The Client Time Offset [Hours]  */
    public short getClientTimeHoursOffset();

    /** The Client Time Offset  */
    public void setClientTimeOffset(Date clientDate);

}
