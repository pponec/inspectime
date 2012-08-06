/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.service.def;

import com.inspectime.commons.bo.User;
import java.awt.Color;
import java.util.Locale;
import org.ujorm.Key;
import org.ujorm.extensions.Property;
import org.ujorm.extensions.UjoMiddle;

/**
 * Services for reading and writting user parameters
 * @author Ponec
 */
public interface ParamUserService extends UjoMiddle<ParamUserService> {

    /** Pomodoro interval in [ms] */
    public static final Property<ParamUserService, Integer> pomodoroInterval = Property.newInstance("PomodoroInterval", 20);
    /** Maximum number of hot event buttons. */
    public static final Property<ParamUserService, Integer> myEventButtonMaxCount = Property.newInstance("HotEventButtonMaxCount", 16);

    /** Locale Text lcalization */
    public static final Property<ParamUserService,String> P_LANG = Property.newInstance("Language", Locale.ENGLISH.getLanguage());
    /** Working Hours */
    //public static final Property<ParamUserService,Float> P_WORKING_HOURS = Property.newInstance("WorkingHours", 8f);
    /** Decimal time format. */
    //public static final Property<ParamUserService,Boolean> P_DECIMAL_TIME_FORMAT = Property.newInstance("DecimalTimeFormat", true);
    /** The Main selecton format. */
    //public static final Property<ParamUserService,String> P_DATE_MAIN_FORMAT = Property.newInstance("DateMainFormat", "EE, yyyy/MM/dd'  %s: 'ww");
    /** The Export Date Selection. */
    //public static final Property<ParamUserService,String> P_DATE_REPORT_FORMAT = Property.newInstance("DateReportFormat", P_DATE_MAIN_FORMAT.getDefault() );
    /** The Goto Date format. */
    //public static final Property<ParamUserService,String> P_DATE_GOTO_FORMAT = Property.newInstance("DateGotoFormat", "yyyy/MM/dd");
    /** A Color of a private project. */
    public static final Property<ParamUserService,Color> P_COLOR_PRIVATE = Property.newInstance("ColorOfPrivateProject", new Color(0x5DA158));
    /** A Color of finished project. */
    //public static final Property<ParamUserService,Color> P_COLOR_FINISHED_PROJ = Property.newInstance("ColorOfFinishedProject", new Color(0xA9AC88));
    /** A Color of an editable area. */
    //public static final Property<ParamUserService,Color> P_COLOR_EDITABLE = Property.newInstance("ColorOfEditableArea", new Color(0xFFFACD));
    /** Modify value of finished project or task. */
    //public static final Property<ParamUserService,Boolean> P_MODIFY_FINESHED_PROJ = Property.newInstance("ModifyFinishedProject", false);
    /** Create a new Event on an EXIT action. */
    public static final Property<ParamUserService,Boolean> P_EXIT_EVENT_CREATE = Property.newInstance("ExitEventCreating", true);
    /** Description of an EXIT action. */
    public static final Property<ParamUserService,String> P_EXIT_EVENT_DESCR = Property.newInstance("ExitEventDescription", "EXIT");
    /** Create a new Event on an LOGIN action. */
    public static final Property<ParamUserService,Boolean> P_LOGIN_EVENT_CREATE = Property.newInstance("LoginEventCreating", true);
    /** Description of an ENTER action. */
    public static final Property<ParamUserService,String> P_LOGIN_EVENT_DESCR = Property.newInstance("LoginEventDescription", "LOGIN");

    /** Return the logged user. */
    public User getUser();

    /** Get a user parameter value. */
    public <T> T getValue(Key<ParamUserService,T> property, User company);

    /** Set a user parameter value. */
    public <T> void setValue(Key<ParamUserService,T> property, T value, User company);

}
