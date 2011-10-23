/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.service;

import com.inspectime.application.client.cbo.CSingleUsrParam;
import com.inspectime.service.def.ParamUserService;
import org.ujorm.gxt.client.CujoProperty;
import org.ujorm.gxt.client.CujoPropertyList;
import java.io.Serializable;
import java.util.EnumSet;

/**
 * Singleton - the Parameters of the User
 * @author meatfly, Ponec
 * @see ParamUserService
 **/
public class CParam4User extends CAbstractParam implements Serializable {

    private static final CujoPropertyList pl = list(CParam4User.class);
    /** Ponodoro Interval [min] */
    public static final CujoProperty<CParam4User, Integer> pomodoroInterval = pl.newPropertyDef("PomodoroInterval", 20);
    /** Maximum number of the Hot Event Buttons */
    public static final CujoProperty<CParam4User, Integer> hotEventButtonMaxCount = pl.newPropertyDef("HotEventButtonMaxCount", 16);

    /** Working Hours */
    //public static final CujoProperty<CParam4User,Float> P_WORKING_HOURS = pl.newPropertyDef("WorkingHours", 8f);
    /** The First Day of the Week Day. */
    //public static final CujoProperty<CParam4User,Integer> P_FIRST_DAY_OF_WEEK = pl.newProperty("FirstDayOfWeek", Integer.class);
    /** Decimal time format. */
    //public static final CujoProperty<CParam4User,Boolean> P_DECIMAL_TIME_FORMAT = pl.newPropertyDef("DecimalTimeFormat", true);
    /** The Main selecton format. */
    //public static final CujoProperty<CParam4User,String> P_DATE_MAIN_FORMAT = pl.newPropertyDef("DateMainFormat", "EE, yyyy/MM/dd'  %s: 'ww");
    /** The Export Date Selection. */
    //public static final CujoProperty<CParam4User,String> P_DATE_REPORT_FORMAT = pl.newPropertyDef("DateReportFormat", P_DATE_MAIN_FORMAT.getDefault() );
    /** The Goto Date format. */
    //public static final CujoProperty<CParam4User,String> P_DATE_GOTO_FORMAT = pl.newPropertyDef("DateGotoFormat", "yyyy/MM/dd");
    /** A Color of a private project. */
    public static final CujoProperty<CParam4User,String> P_COLOR_PRIVATE = pl.newPropertyDef("ColorOfPrivateProject", "5DA158");
    /** A Color of finished project. */
    //public static final CujoProperty<CParam4User,String> P_COLOR_FINISHED_PROJ = pl.newPropertyDef("ColorOfFinishedProject", "A9AC88");
    /** A Color of an editable area. */
    //public static final CujoProperty<CParam4User,String> P_COLOR_EDITABLE = pl.newPropertyDef("ColorOfEditableArea", "FFFACD");
    /** Modify value of finished project or task. */
    //public static final CujoProperty<CParam4User,Boolean> P_MODIFY_FINESHED_PROJ = pl.newPropertyDef("ModifyFinishedProject", false);
    /** Create a new Event on an EXIT action. */
    public static final CujoProperty<CParam4User,Boolean> P_EXIT_EVENT_CREATE = pl.newPropertyDef("ExitEventCreating", true);
    /** Description of an EXIT action. */
    public static final CujoProperty<CParam4User,String> P_EXIT_EVENT_DESCR = pl.newPropertyDef("ExitEventDescription", "EXIT");
    /** Create a new Event on a LOGIN action. */
    public static final CujoProperty<CParam4User,Boolean> P_LOGIN_EVENT_CREATE = pl.newPropertyDef("LoginEventCreating", true);
    /** Description of an ENTER action. */
    public static final CujoProperty<CParam4User,String> P_LOGIN_EVENT_DESCR = pl.newPropertyDef("LoginEventDescription", "LOGIN");
    /** LocaleText lcalization */
    public static final CujoProperty<CParam4User,String> P_LOCALE = pl.newPropertyDef("Language", "en");



    /** Instance */
    private static final CParam4User instance = new CParam4User();

    /** User Login */
    private String login;
    /** User Company name */
    private String company;
    /** User Roles */
    private EnumSet roles;

    private CParam4User() {
        initMessages();
    }

    public static CParam4User getInstance() {
        return instance;
    }

    @Override
    public CujoPropertyList readProperties() {
        return pl;
    }

    private void initMessages() {
        setDescription(CParam4User.pomodoroInterval, "Pomodoro interval [ms], for more information see: http://www.pomodorotechnique.com");
        setDescription(CParam4User.hotEventButtonMaxCount, "Maximum number of the Favourite buttons. Call a new login to change the button count.");
        setDescription(CParam4User.P_COLOR_PRIVATE, "Color of the PRIVATE tasks / event");
        setDescription(CParam4User.P_EXIT_EVENT_CREATE, "Create a new Event on Exit using smart algorithm");
        setDescription(CParam4User.P_EXIT_EVENT_DESCR, "Default descripton to an auto Event on the Logout action");
        setDescription(CParam4User.P_LOGIN_EVENT_CREATE, "Create a new Login on Exit using smart algorithm");
        setDescription(CParam4User.P_LOGIN_EVENT_DESCR, "Default descripton to an auto Event on the Login action");
        setDescription(CParam4User.P_LOCALE, "Language (only the ENGLISH is supported now");
    }

    // -------------- GET PARAMETERS --------------

    /** Returns a pomodoro interval */
    public int getPomodoroInterval() {

        CujoProperty property = pomodoroInterval;

        CSingleUsrParam p = (CSingleUsrParam) map.get(property.getName());
        int result = p != null
                ? Integer.parseInt(p.getTextValue())
                : (Integer) property.getDefault();
        return result;
    }

    /** Returns a pomodoro interval */
    public int getHotEventButtonMaxCount() {

        CujoProperty property = hotEventButtonMaxCount;

        CSingleUsrParam p = (CSingleUsrParam) map.get(property.getName());
        int result = p != null
                ? Integer.parseInt(p.getTextValue())
                : (Integer) property.getDefault();
        return result;
    }

    /** Get Login */
    public String getLogin() {
        return login;
    }

    /** SetLogin */
    public void setLogin(String login) {
        this.login = login;
    }

    /** User Company name */
    public String getCompany() {
        return company;
    }

    /** User Company name */
    public void setCompany(String company) {
        this.company = company;
    }

    /** User Roles */
    public EnumSet getRoles() {
        return roles;
    }

    /** User Roles */
    public void setRoles(EnumSet roles) {
        this.roles = roles;
    }
}
