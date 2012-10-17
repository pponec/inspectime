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
import org.ujorm.core.KeyFactory;
import org.ujorm.extensions.UjoMiddle;

/**
 * Services for reading and writting user parameters
 * @author Ponec
 */
public interface ParamUserService extends UjoMiddle<ParamUserService> {

    /** Key factory */
    public static final KeyFactory $f = KeyFactory.Builder.get(ParamUserService.class);

    /** Pomodoro interval in [ms] */
    public static final Key<ParamUserService, Integer> pomodoroInterval = $f.newKey("PomodoroInterval", 20);
    /** Maximum number of hot event buttons. */
    public static final Key<ParamUserService, Integer> myEventButtonMaxCount = $f.newKey("HotEventButtonMaxCount", 16);

    /** Locale Text lcalization */
    public static final Key<ParamUserService,String> P_LANG = $f.newKey("Language", Locale.ENGLISH.getLanguage());
    /** Working Hours */
    //public static final Key<ParamUserService,Float> P_WORKING_HOURS = $f.newKey("WorkingHours", 8f);
    /** Decimal time format. */
    //public static final Key<ParamUserService,Boolean> P_DECIMAL_TIME_FORMAT = $f.newKey("DecimalTimeFormat", true);
    /** The Main selecton format. */
    //public static final Key<ParamUserService,String> P_DATE_MAIN_FORMAT = $f.newKey("DateMainFormat", "EE, yyyy/MM/dd'  %s: 'ww");
    /** The Export Date Selection. */
    //public static final Key<ParamUserService,String> P_DATE_REPORT_FORMAT = $f.newKey("DateReportFormat", P_DATE_MAIN_FORMAT.getDefault() );
    /** The Goto Date format. */
    //public static final Key<ParamUserService,String> P_DATE_GOTO_FORMAT = $f.newKey("DateGotoFormat", "yyyy/MM/dd");
    /** A Color of a private project. */
    public static final Key<ParamUserService,Color> P_COLOR_PRIVATE = $f.newKey("ColorOfPrivateProject", new Color(0x5DA158));
    /** A Color of finished project. */
    //public static final Key<ParamUserService,Color> P_COLOR_FINISHED_PROJ = $f.newKey("ColorOfFinishedProject", new Color(0xA9AC88));
    /** A Color of an editable area. */
    //public static final Key<ParamUserService,Color> P_COLOR_EDITABLE = $f.newKey("ColorOfEditableArea", new Color(0xFFFACD));
    /** Modify value of finished project or task. */
    //public static final Key<ParamUserService,Boolean> P_MODIFY_FINESHED_PROJ = $f.newKey("ModifyFinishedProject", false);
    /** Create a new Event on an EXIT action. */
    public static final Key<ParamUserService,Boolean> P_EXIT_EVENT_CREATE = $f.newKey("ExitEventCreating", true);
    /** Description of an EXIT action. */
    public static final Key<ParamUserService,String> P_EXIT_EVENT_DESCR = $f.newKey("ExitEventDescription", "EXIT");
    /** Create a new Event on an LOGIN action. */
    public static final Key<ParamUserService,Boolean> P_LOGIN_EVENT_CREATE = $f.newKey("LoginEventCreating", true);
    /** Description of an ENTER action. */
    public static final Key<ParamUserService,String> P_LOGIN_EVENT_DESCR = $f.newKey("LoginEventDescription", "LOGIN");
    
    /** Lock the Factory */
    public static final boolean $locked = $f.lockAndSize()>=0;
    
    
    /** Return the logged user. */
    public User getUser();

    /** Get a user parameter value. */
    public <T> T getValue(Key<ParamUserService,T> property, User company);

    /** Set a user parameter value. */
    public <T> void setValue(Key<ParamUserService,T> property, T value, User company);

}
