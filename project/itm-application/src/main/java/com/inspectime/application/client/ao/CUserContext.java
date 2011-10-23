/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.ao;

import com.inspectime.application.client.cbo.CSingleComParam;
import com.inspectime.application.client.cbo.CSingleUsrParam;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Client User context.
 * @author Ponec
 */
public class CUserContext implements Serializable {

    private List<CSingleComParam> systemParams;
    private List<CSingleComParam> companyParams;
    private List<CSingleUsrParam> userParams;
    private String userLogin;
    private String companyName;
    private String roles;
 // private CUser user; // Cause: StatusCodeException ...

    /** Logged user or NULL of no useer is logged. */
    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    /** Company Name */
    public String getCompanyName() {
        return companyName;
    }

    /** Company Name */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    /** Returns all system parameters */
    public List<CSingleComParam> getSystemParams() {
        if (systemParams == null) {
            systemParams = new ArrayList<CSingleComParam>();
        }
        return systemParams;
    }

    /** Returns all company parameters */
    public List<CSingleComParam> getCompanyParams() {
        if (companyParams == null) {
            companyParams = new ArrayList<CSingleComParam>();
        }
        return companyParams;
    }

    /** Returns all user parameters */
    public List<CSingleUsrParam> getUserParams() {
        if (userParams == null) {
            userParams = new ArrayList<CSingleUsrParam>();
        }
        return userParams;
    }
}
