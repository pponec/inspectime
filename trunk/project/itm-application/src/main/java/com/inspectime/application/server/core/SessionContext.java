/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */


package com.inspectime.application.server.core;

import com.inspectime.commons.bo.User;
import java.util.Locale;

/**
 * Context of logged user in scpoe session
 * @author Ponec
 */
public class SessionContext {

    private User loggedUser;
    private Locale userLocale = Locale.ENGLISH;

    /** Is user logged? */
    public boolean isLogged() {
        return loggedUser!=null;
    }

    /** Return user language */
    public String getUserLanguage() {
        return userLocale.getLanguage();
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    public Locale getUserLocale() {
        return userLocale;
    }

    public void setUserLocale(Locale userLocale) {
        this.userLocale = userLocale;
    }


}
