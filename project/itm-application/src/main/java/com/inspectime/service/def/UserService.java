/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inspectime.service.def;

import com.inspectime.application.client.ao.CUserContext;
import com.inspectime.application.client.cbo.CCompany;
import com.inspectime.application.client.cbo.CUser;
import com.inspectime.commons.bo.Company;
import java.util.Date;
import java.util.List;
import org.ujorm.gxt.client.ao.ValidationMessage;
import com.inspectime.commons.bo.User;
import com.inspectime.commons.bo.UserRole;
import com.inspectime.commons.bo.enums.RoleEnum;

/**
 *
 * @author Hampl
 */
public interface UserService extends AbstractService<User> {

    /** Database have no user. */
    public boolean isNoUser();

    public User loadByLogin(String userName);

    public void saveRole(UserRole role);

    /** Create roles from temporarry field. */
    public void createRoles(User user, boolean create);

    /** A user modify validation */
    public ValidationMessage validate(User user, boolean create);

    /** Return loged user bo (null if not loged)*/
    public User loadLogedUser();

    /** Returns Authentication Name */
    public String getAuthenticatonName();
    
    /** return true if  user have role, false if not*/
    //TODO : lepsi nazev ? refactor me
    public boolean userContainsRole(User user,RoleEnum role);

    /** return true if loged user have role, false if not*/
    //TODO : lepsi nazev ? refactor me
    public boolean loggedUserContainsRole(RoleEnum role);

    /** Returns User Context include all parameters */
    public CUserContext getUserContext();

    /** Encode passwordHash */
    public String encodePassword(String rawPass);

    /** Returns USers for selected Company */
    public List<User> getUserListForCompany(Company myCompany);

    /** Is a registation login free? */
    public boolean isLoginFree(String login);

    /** Register a new user include company */
    public String registerUser(CUser newUser, CCompany company);

    /** Has the logged user confirmed an Agreement with term of use? */
    public boolean isUserAgreemnt();

    /** Send a confirmation with an User Agreement. */
    public boolean sendUserAgreemnt();

    /** Logout the User and create the last event of the day */
    public boolean logout(Date localDate);

    /** Create first Event of the Day. */
    public boolean createFirstDayEvent(Date day);

    /** Get ApplContextService for the current user */
    public ApplContextService getApplContext();

}
