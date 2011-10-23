/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.server.controller;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.gwtincubator.security.exception.ApplicationSecurityException;
import com.inspectime.application.client.ao.CUserContext;
import com.inspectime.application.client.cbo.CCompany;
import com.inspectime.application.client.cbo.CUser;
import com.inspectime.application.client.controller.UserController;
import com.inspectime.service.def.InitDataService;
import com.inspectime.service.def.UserService;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.ujorm.gxt.client.CMessageException;


/**
 * Implementation of the UserController
 * @author Ponec
 */
@org.springframework.stereotype.Controller("userController")
public class UserControllerImpl extends RemoteServiceServlet implements UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private InitDataService initDataService;

    @Override
    public CUserContext getUserContext() throws ApplicationSecurityException, CMessageException {
        final CUserContext result = userService.getUserContext();
        return result;
    }

    @Override
    public boolean isLoginFree(String login) throws CMessageException {
        final boolean result = userService.isLoginFree(login);
        return result;
    }

    @Override
    public String registerUser(CUser newUser, CCompany company) throws CMessageException {
        final String result = userService.registerUser(newUser, company);
        return result;
    }

    @Override
    public boolean isUserAgreemnt(Date clientDay) throws CMessageException {
        userService.getApplContext().setClientTimeOffset(clientDay);
        boolean result = userService.isUserAgreemnt();
        userService.createFirstDayEvent(clientDay);
        initDataService.createDemoData();
        return result;
    }

    @Override
    public boolean sendUserAgreemnt() throws CMessageException {
        return userService.sendUserAgreemnt();
    }

    /** Logout the user */
    @Override
    public boolean logout(Date localDate) throws CMessageException {
        return userService.logout(localDate);
    }

  
}
