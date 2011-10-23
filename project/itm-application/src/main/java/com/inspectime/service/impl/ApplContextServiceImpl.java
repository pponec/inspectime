/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.service.impl;

import com.inspectime.commons.bo.Company;
import com.inspectime.commons.bo.Product;
import com.inspectime.commons.bo.User;
import com.inspectime.commons.bo.enums.RoleEnum;
import com.inspectime.service.def.ApplContextService;
import com.inspectime.service.def.UserService;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.transaction.annotation.Transactional;
import org.ujorm.orm.support.UjoSessionFactory;

/**
 * User application context
 * @author Pavel Ponec
 */
@Transactional
@Scope("session")
@org.springframework.stereotype.Service(ApplContextServiceImpl.BEAN_NAME)
public class ApplContextServiceImpl implements ApplContextService {

    public static final String BEAN_NAME = "applContextService";

    static final private Logger LOGGER = Logger.getLogger(ApplContextServiceImpl.class.getName());

    @Autowired
    protected UjoSessionFactory ujoSessionFactory;

    @Autowired
    private UserService userService;

    private long timeStamp = System.currentTimeMillis();
    private long timeInterval = 10000; // 10 [sec]
    private User user;
    private short clientTimeOffset = 0;


    private void setTimeStamp() {
        timeStamp = System.currentTimeMillis() + timeInterval;
    }

    private boolean isTimeOut() {
        return System.currentTimeMillis() > timeStamp;
    }

    @Override
    public void resetCache() {
        timeStamp = System.currentTimeMillis() - 1L;
    }

    /** Return a loged user bo (null if not loged). */
    @Override
    public synchronized User getUser() {
        if (user==null || isTimeOut()) {
            user = userService.loadLogedUser();
            setTimeStamp();
        }

        // Assign a default session:
        if (user!=null) {
            user.writeSession(ujoSessionFactory.getDefaultSession());
        }

        return user;
    }

    @Override
    public boolean isLogged() {
        String login = userService.getAuthenticatonName();
        return login!=null && login.length()>0 && !UserServiceImpl.PROHIBITED_LOGIN.equals(login);
    }

    /** Returns logged users company */
    @Override
    public Company getUserCompany() {
        User user = getUser();
        return user !=null 
             ? user.getCompany()
             : null
             ;
    }

    /** Returns thr first Product */
    @Override
    public Product getDefaultProduct() {
        Company company = getUserCompany();
        return company !=null
             ? company.getFirstProduct()
             : null
             ;
    }

    /** Test for required permission for a logged user. */
    @Override
    public boolean hasPermission(RoleEnum... roleEnum) {
        final boolean result = getUser().hasAnyRole(roleEnum);
        return result;
    }

    /** The Client Time Offset [Minutes]  */
    @Override
    public short getClientTimeOffset() {
        return clientTimeOffset;
    }

    /** The Client Time Offset [Minutes]  */
    @Override
    public short getClientTimeHoursOffset() {
        final double SIXTY_MINUTES = 60.0;
        return (short) Math.round(clientTimeOffset / SIXTY_MINUTES);
    }


    /** The Client Time Offset  */
    @Override
    public void setClientTimeOffset(Date clientDate) {
        clientTimeOffset = (short) Math.round((clientDate.getTime() - (System.currentTimeMillis() - getServerTimeOffset())) / 1000.0 / 60);
    }

    /** Get Current time on  Zone. */
    private int getServerTimeOffset() {
        final TimeZone tz = TimeZone.getTimeZone("GMT");
        final long ct = System.currentTimeMillis();
        final int result = TimeZone.getDefault().getOffset(ct) - tz.getOffset(ct);
        return result;
    }

}
