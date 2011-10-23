/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */
package com.inspectime.application.client.controller;

import com.google.gwt.user.client.rpc.RemoteService;
import com.gwtincubator.security.exception.ApplicationSecurityException;
import com.inspectime.application.client.ao.CUserContext;
import com.inspectime.application.client.cbo.CCompany;
import com.inspectime.application.client.cbo.CUser;
import java.util.Date;
import org.ujorm.gxt.client.CMessageException;

/**
 * Service to get auser information
 * @author Hampl, Ponec
 */
//@RemoteServiceRelativePath("controller/")
public interface UserController extends RemoteService {

    //@org.springframework.security.annotation.Secured({com.inspectime.commons.bo.enums.RoleInterfaceEnum.BAN})
    CUserContext getUserContext() throws ApplicationSecurityException, CMessageException;

    /** Is login free to registration. */
    public boolean isLoginFree(String login) throws CMessageException;

    /** Registre new user to new company. */
    public String registerUser(CUser newUser, CCompany company) throws CMessageException;

    /** Has the logged user confirmed an Agreement with term of use? */
    public boolean isUserAgreemnt(Date day) throws CMessageException;

    /** Send a confirmation with an User Agreement. */
    public boolean sendUserAgreemnt() throws CMessageException;

    /** Logout the user */
    public boolean logout(Date localDate) throws CMessageException;
}


