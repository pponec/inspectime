/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.service.impl;

import com.google.gwt.core.client.GWT;
import com.inspectime.application.client.ao.CUserContext;
import com.inspectime.application.client.cbo.CCompany;
import com.inspectime.application.client.cbo.CSingleComParam;
import com.inspectime.application.client.cbo.CSingleUsrParam;
import com.inspectime.application.client.cbo.CUser;
import com.inspectime.application.client.service.CParam4System;
import com.inspectime.commons.WQuery;
import com.inspectime.commons.bo.Company;
import com.inspectime.commons.bo.Event;
import com.inspectime.commons.bo.SingleComParam;
import com.inspectime.commons.bo.SingleUsrParam;
import com.inspectime.commons.bo.Task;
import java.util.Date;
import org.ujorm.gxt.client.ao.ValidationException;
import org.ujorm.gxt.client.ao.ValidationMessage;
import com.inspectime.commons.bo.User;
import com.inspectime.commons.bo.UserGroup;
import com.inspectime.commons.bo.UserRole;
import com.inspectime.commons.bo.User_TEST;
import com.inspectime.commons.bo.enums.RoleEnum;
import com.inspectime.service.def.ApplContextService;
import com.inspectime.service.def.ParamCompService;
import com.inspectime.service.def.CompanyService;
import com.inspectime.service.def.EventService;
import com.inspectime.service.def.ParamUserService;
import com.inspectime.service.def.SingleComParamService;
import com.inspectime.service.def.SingleUsrParamService;
import com.inspectime.service.def.TaskService;
import com.inspectime.service.def.UserGroupService;
import com.inspectime.service.def.UserService;
import com.inspectime.service.defPlain.ParamSystemService;
import java.util.EnumSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.encoding.ShaPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.ujorm.criterion.Criterion;
import static org.ujorm.criterion.Operator.*;
import org.ujorm.gxt.client.CujoProperty;
import org.ujorm.gxt.server.IServerClassConfig;
import org.ujorm.gxt.server.UjoTranslator;
import org.ujorm.implementation.orm.OrmTable;

/**
 *
 * @author Hampl
 */
@Transactional
@org.springframework.stereotype.Service("userService")
public class UserServiceImpl extends AbstractServiceImpl<User> implements UserService {

    /** TODO: remove it */
    public static final StringBuilder __$sb = new StringBuilder().append(User_TEST.ADMIN_LOGIN2); // TestCode

    /** Logger */
    static final private Logger LOGGER = Logger.getLogger(UserServiceImpl.class.getName());
    /** Is not the user deleted? */
    private static Criterion<User> crnActive = User.active.whereEq(true);
    /** Prohibited Login */
    public static final String PROHIBITED_LOGIN = "roleAnonymous";
    /** Array of the Prohibited Logins */
    private final String[] prohibitedLogins = {PROHIBITED_LOGIN};
    

    @Autowired
    private ParamSystemService sysParamService;
    @Autowired
    private IServerClassConfig serverClassConfig;
    @Autowired
    private SingleUsrParamService singleUsrParamService;
    @Autowired
    private SingleComParamService singleComParamService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private UserGroupService userGroupServiceImpl;
    @Autowired
    private ParamCompService paramCompService;
    @Autowired
    private ParamSystemService paramSystemService;
    @Autowired
    private ParamUserService paramUserService;
    @Autowired
    private EventService eventService;
    @Autowired
    private TaskService taskService;

    @Override
    public boolean isNoUser() {
        Criterion<User> allUsersCrn = Criterion.forAll(User.login);
        User user = getSession().createQuery(allUsersCrn).setLimit(1).uniqueResult();
        return user==null;
    }

    @Override
    public User loadByLogin(String login) throws NoSuchElementException {
        Criterion<User> loginCrn = Criterion.where(User.login, login); // equals to
        User user = getSession().createQuery(crnActive().and(loginCrn)).uniqueResult();
        return user;
    }

    @Override
    public void saveRole(UserRole role) {
        getSession().save(role);
    }

    @Override
    public Class<User> getDefaultClass() {
        return User.class;
    }

    /** A user modify validation */
    @Override
    public ValidationMessage validate(User user, boolean create) {

        //check prohibited logins
        for (String pLogin : prohibitedLogins) {
            if (user.getLogin().equalsIgnoreCase(pLogin)) {
                return new ValidationMessage(User.login, "Login is prohibited: " + user.getLogin());
            }
        }
        //check length
        if (user.getLogin().length() < 4) {
            return new ValidationMessage(User.login, "Login must have 4 characters at least.");
        }

        // check unique
        ValidationMessage result = null;
        Criterion<User> crnName = Criterion.where(User.login, user.getLogin());

        if (create) {
            boolean exists = getSession().createQuery(crnActive().and(crnName)).exists();
            if (exists) {
                result = new ValidationMessage(User.login, "Login is already used: " + user.getLogin());
            }
        } else {
            Criterion<User> crnExcludeId = Criterion.where(User.id, NOT_EQ, user.getId());
            boolean exists = getSession().createQuery(crnActive().and(crnName).and(crnExcludeId)).exists();
            if (exists) {
                result = new ValidationMessage(User.login, "Login is already used: " + user.getLogin());
            }
        }

        return result;
    }

    /** Create roles from temporarry field. */
    @Override
    public void createRoles(User user, boolean create) {
        Set<RoleEnum> reqRoles = getRequiredRoles(user.get(User._userTextRoles));

        if (reqRoles != null) {
            if (create) {
                for (RoleEnum role : reqRoles) {
                    UserRole userRole = new UserRole(user, role);
                    getSession().save(userRole);
                }
            } else {
                // Update:
                Criterion<UserRole> cr = Criterion.where(UserRole.user, user);
                List<UserRole> assignedRoles = getSession().createQuery(cr).iterator().toList();

                for (RoleEnum role : RoleEnum.values()) {
                    boolean required = reqRoles.contains(role);
                    boolean assigned = isRoleAssigned(role, assignedRoles);

                    if (required != assigned) {
                        if (required) {
                            UserRole userRole = new UserRole(user, role);
                            getSession().save(userRole);
                        } else {
                            for (UserRole userRole : assignedRoles) {
                                if (userRole.getRole() == role) {
                                    getSession().delete(userRole);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /** Create new Signle Parameter of Company. */
    private CSingleComParam createSingleComParam(CujoProperty p, String textValue) {
        CSingleComParam singleParam = new CSingleComParam();
        singleParam.set(CSingleComParam.key, p.getName());
        singleParam.set(CSingleComParam.index, p.getIndex());
        singleParam.set(CSingleComParam.value, textValue);
        singleParam.set(CSingleComParam.type, p.getClass().getSimpleName());
        return singleParam;
    }


    /** Returns required roles from string */
    private Set<RoleEnum> getRequiredRoles(String roles) {
        if (roles == null) {
            return null;
        }
        EnumSet<RoleEnum> result = EnumSet.noneOf(RoleEnum.class);
        StringTokenizer st = new StringTokenizer(roles, ",; ");
        //
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (token.length()>0) try {
                Integer i = Integer.parseInt(token);
                result.add(RoleEnum.values()[i]);
            } catch (Throwable e) {
                LOGGER.log(Level.WARNING, "Cant get user role: " + token + " for: " + roles);
            }
        }
        return result;
    }

    /** Má uživatel přiřazenou roli?  */
    private boolean isRoleAssigned(RoleEnum role, List<UserRole> userRoles) {
        for (UserRole userRole : userRoles) {
            if (userRole.getRole() == role) {
                return true;
            }
        }
        return false;
    }

    @Override
    public User loadLogedUser() {
        try {
            String login = getAuthenticatonName();
            User result = loadByLogin(login);
            return result;
        } catch (Throwable e) {
            GWT.log("No user context is available", e);
            return null;
        }
    }

    /** Returns Authentication Name (login) */
    @Override
    public String getAuthenticatonName() {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        return login;
    }

    @Override
    public boolean loggedUserContainsRole(RoleEnum role) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User u = loadByLogin(login);
        return userContainsRole(u, role);
    }

    /** Call the DB request */
    @Override
    public boolean userContainsRole(User user, RoleEnum role) {
        if (user == null) {
            return false;
        }
        Criterion<UserRole> crnUser = Criterion.where(UserRole.user, user);
        Criterion<UserRole> crnRole = Criterion.where(UserRole.role, role);

        boolean result = getSession().createQuery(crnUser.and(crnRole)).exists();
        return result;
    }

    @Override
    public void save(User bo) {
        if (bo.get(User.company) == null) {
            bo.set(User.company, getApplContext().getUserCompany());
        }

        // Make Hash:
        if (isValid(bo.getPasswordNative())) {
            bo.setPasswordHash(encodePassword(bo.getPasswordNative()));
        }

        if (bo.get(User.userGroup) == null) {
            // Dummy User Group:
            UserGroup dummyUserGroup = new UserGroup();
            dummyUserGroup.set(UserGroup.id, 1L);
            bo.set(User.userGroup, dummyUserGroup);
        }

        boolean create = true;
        ValidationMessage msg = validate(bo, create);
        if (msg != null) {
            throw new ValidationException(msg);
        }
        super.save(bo);
        createRoles((User) bo, create);
    }

    @Override
    public void update(User bo) {

        // Make Hash:
        if (isValid(bo.getPasswordNative())) {
            bo.setPasswordHash(encodePassword(bo.getPasswordNative()));
        }

        if (bo.get(User.userGroup) == null) {
            // Dummy User Group:
            UserGroup dummyUserGroup = new UserGroup();
            dummyUserGroup.set(UserGroup.id, 1L);
            bo.set(User.userGroup, dummyUserGroup);
        }

        boolean create = false;
        ValidationMessage msg = validate(bo, create);
        if (msg != null) {
            throw new ValidationException(msg);
        }
        super.update(bo);
        createRoles((User) bo, create);
    }

//    private void displayStatus(Object prefix) {
//        LOGGER.log(Level.WARNING, ">>> " + prefix + " "
//                + getUjoSessionFactory().getDeep() + " " // ladicí metoda není součástí api
//                + getUjoSessionFactory().getDefaultSession().isClosed() + " "
//                + getUjoSessionFactory().getDefaultSession().isRollbackOnly()
//                );
//    }

    @Override
    public <UJO extends OrmTable> List<UJO> list(WQuery query) {
        Criterion crn = Criterion.where(User.company, getApplContext().getUserCompany());
        query.addCriterion(crn);
        return super.list(query);
    }

    @Override
    public CUserContext getUserContext() {
        CUserContext result = new CUserContext();
        final int relations = 1;

        User user = loadLogedUser();
        if (user != null) {
            result.setUserLogin(user.getLogin());
            result.setCompanyName(paramCompService.get(ParamCompService.companyName));
            result.setRoles(user.getUserRoleAsString());

            // User Parameters
            final List<SingleUsrParam> usrParams = singleUsrParamService.list();
            UjoTranslator<CSingleUsrParam> translator2 = new UjoTranslator<CSingleUsrParam>(new CSingleUsrParam(), relations, serverClassConfig);
            result.getUserParams().addAll(translator2.translate(usrParams).list());

            // Company parameters:
            final List<SingleComParam> comParams = singleComParamService.list();
            UjoTranslator<CSingleComParam> translator3 = new UjoTranslator<CSingleComParam>(new CSingleComParam(), relations, serverClassConfig);
            result.getCompanyParams().addAll(translator3.translate(comParams).list());

            // System parameters:
            result.getSystemParams().add(createSingleComParam
                    ( CParam4System.debug, paramSystemService.getText
                    ( ParamSystemService.debug)));
            result.getSystemParams().add(createSingleComParam
                    ( CParam4System.termsOfUseLink, paramSystemService.getText
                    ( ParamSystemService.termsOfUseLink)));
            result.getSystemParams().add(createSingleComParam
                    ( CParam4System.termsOfUseLink
                    , getDefaultTimeZone()));
        }

        return result;
    }

    /** Rerutns a default time zone by logged user. */
    private String getDefaultTimeZone() {
        final User usr = getApplContext().getUser();
        return usr!=null ? usr.get(User.timeZone).exportToString() : "a" ;
    }

    /** Encode passwordHash */
    @Override
    public String encodePassword(String rawPass) {
        boolean hasEnabled = false; // TODO: how to compare client password to the database hash by Spring ?

        if (hasEnabled) {
            ShaPasswordEncoder passwordEncoder = new ShaPasswordEncoder(256);
            String salt = sysParamService.get(sysParamService.salt);
            return passwordEncoder.encodePassword(rawPass, salt);
        } else {
            return rawPass;
        }
    }

    @Override
    public List<User> getUserListForCompany(Company myCompany) {
        Criterion<User> crn1, crn2, criterion;
        crn1 = Criterion.where(User.company, myCompany);
        crn2 = Criterion.where(User.active, true);
        criterion = crn1.and(crn2);

        List<User> users = getSession().createQuery(criterion).orderBy(User.login).list();
        return users;
    }

    @Override
    public boolean isLoginFree(String login) {
        Criterion<User> crn1, crn2, criterion;
        crn1 = Criterion.where(User.login, login);
        crn2 = Criterion.where(User.active, true);
        criterion = crn1.and(crn2);

        User anyUsers = getSession().createQuery(criterion).setLimit(1).uniqueResult();
        return anyUsers==null;
    }

    @Override
    public String registerUser(CUser newUser, CCompany newCompany) {
        
        String errorMessage = "Login (email) is already used, you have to choose another one.";

        try {

            UjoTranslator<CUser> userTrans = new UjoTranslator(newUser, 1, serverClassConfig);
            User user = userTrans.translateToServer(newUser);
            //
            UjoTranslator<CCompany> compTrans = new UjoTranslator(newCompany, 1, serverClassConfig);
            Company company = compTrans.translateToServer(newCompany);

            if (!isLoginFree(user.getLogin())) {
                return errorMessage + ": " + newUser.getLogin();
            }
            if (company.getName()==null || "".equals(company.getName())) {
                company.set(Company.name, "My Company");
            }

            user.setCompany(company);
            user.set(User.id, null);
            user.set(User.userGroup, userGroupServiceImpl.findDefaultUserGroup(null));
            user.set(User.contractDate, new java.sql.Date(System.currentTimeMillis()));

            companyService.save(company);
            save(user);

            // Save company name to the parameter:
            paramCompService.setValue(ParamCompService.companyName, company.getName(), company);

            return "";

        } catch (Throwable e) {
            String msg;
            
            if (e instanceof ValidationException) {
                msg = ((ValidationException) e).getMessage();
                LOGGER.log(Level.WARNING, msg, e);
            } else {
                msg = errorMessage + ": " + newUser.getLogin();
                LOGGER.log(Level.SEVERE, msg, e);
            }
            return msg;
        }
    }

    @Override
    public boolean isUserAgreemnt() {
        assertServerContext();
        try {
            User user = getApplContext().getUser();
            setLoginTimeStamp(user, false);
            return user.get(User.contractDate)!=null;
        } catch (Throwable e) {
            LOGGER.log(Level.SEVERE, "The isUserAgreemnt bug", e);
            return false;
        }
    }

    /** Assign a timestamp for agreement with the Term of Use. */
    @Override
    public boolean sendUserAgreemnt() {
        try {
            User user = getApplContext().getUser();
            user.readChangedProperties(true);
            user.writeSession(getSession()); // Activate an Column Change Management.
            user.set(User.contractDate, new java.sql.Date(System.currentTimeMillis()));
            getSession().update(user);
            getApplContext().resetCache();
            return true;
        } catch (Throwable e) {
            LOGGER.log(Level.SEVERE, "The isUserAgreemnt bug", e);
            return false;
        }
    }

    /** Set stamp for a last user login */
    private void setLoginTimeStamp(User user, boolean reload) {
        try {
            if (reload) {
               user = getSession().load(User.class, user.getId());
            }
            user.set(User.lastLoginDate, new java.sql.Date(System.currentTimeMillis()));
            getSession().update(user);
            getApplContext().resetCache();
        } catch (Throwable e) {
            LOGGER.log(Level.SEVERE, "setLoginTimeStamp", e);
        }
    }

    /** Logout the User and create the last event of the day */
    @Override
    public boolean logout(Date localDate) {

        final boolean createExitEvent = ParamUserServiceImpl.P_EXIT_EVENT_CREATE.getValue(paramUserService);
        if (!createExitEvent) {
            return false;
        }

        assertServerContext();
        Boolean closedDay = eventService.isClosedDay(localDate);
        if (closedDay!=null && !closedDay) {
            Task privateTask = taskService.findFirstPrivateTask();
            if (privateTask==null) {
                return false;
            }
            Event privateEvent = new Event();
            privateEvent.setDayTime(localDate);
            privateEvent.set(Event.task, privateTask);
            privateEvent.set(Event.description, ParamUserServiceImpl.P_EXIT_EVENT_DESCR.getValue(paramUserService));
            privateEvent.set(Event.active, true);
            privateEvent.set(Event.user, getApplContext().getUser());
            privateEvent.set(Event.timeZone, getApplContext().getUser().get(User.timeZone));
            eventService.save(privateEvent);

            return true;
        } else {
            return false;
        }
    }

    /** Create first Event of the day. */
    @Override
    public boolean createFirstDayEvent(Date localDate) {

        assertServerContext();

        final boolean createLoginEvent = ParamUserServiceImpl.P_LOGIN_EVENT_CREATE.getValue(paramUserService);
        if (!createLoginEvent) {
            return false;
        }

        try {
            if (eventService.isClosedDay(localDate) == null) {
                Task commercialTask = taskService.findFirstCommercialTask();
                if (commercialTask == null) {
                    return false;
                }
                Event privateEvent = new Event();
                privateEvent.setDayTime(localDate);
                privateEvent.set(Event.task, commercialTask);
                privateEvent.set(Event.description, ParamUserServiceImpl.P_LOGIN_EVENT_DESCR.getValue(paramUserService));
                privateEvent.set(Event.active, true);
                privateEvent.set(Event.user, getApplContext().getUser());
                privateEvent.set(Event.timeZone, getApplContext().getUser().get(User.timeZone));
                eventService.save(privateEvent);
            }

        } catch (Throwable e) {
            LOGGER.log(Level.SEVERE, "createFirstEvent bug", e);
            return false;
        }
        return true;
    }

    /** Get ApplContextService for the current user */
    @Override
    public ApplContextService getApplContext() {
         return super.getApplContext();
    }
    
    /** Criterion for User.active = TRUE */
    private Criterion<User> crnActive() {
        if (crnActive==null) {
             crnActive = Criterion.where(User.active, true);
        }
        return crnActive;
    }
}
