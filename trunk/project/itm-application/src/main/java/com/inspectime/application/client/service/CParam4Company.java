/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.service;

import com.inspectime.application.client.cbo.CSingleComParam;
import com.inspectime.service.def.ParamCompService;
import org.ujorm.gxt.client.CujoProperty;
import org.ujorm.gxt.client.CujoPropertyList;
import java.io.Serializable;
import java.util.HashMap;
import org.ujorm.gxt.client.AbstractCujo;

/**
 * Singleton - the Parameters of the Company
 * @author meatfly, Ponec
 * @see ParamCompService
 **/
public class CParam4Company extends CAbstractParam implements Serializable {

    private static final CujoPropertyList pl = list(CParam4Company.class);
    /** Company Name */
    public static final CujoProperty<CParam4Company, String> companyName = pl.newPropertyDef("CompanyName", "");
    /** Company Address */
    public static final CujoProperty<CParam4Company, String> companyAddress = pl.newPropertyDef("CompanyAddress", "");
    /** Is supported the Product? */
    public static final CujoProperty<CParam4Company, Boolean> productSupport = pl.newPropertyDef("ProductSupport", true);
    /** Is supported the Product? */
    public static final CujoProperty<CParam4Company, Integer> firstDayOfWeek = pl.newPropertyDef("FirstDayOfWeek", 1);
    /** Show Private Events in Event Report */
    public static final CujoProperty<CParam4Company, Boolean> reportShowsPrivateEvents = pl.newPropertyDef("ReportShowsPrivateEvents", false);
    /** */
    public static final CujoProperty<CParam4Company, String> jiraServerUrl = pl.newPropertyDef("JiraServerUrl", "");

    /** Instance */
    private static final CParam4Company instance = new CParam4Company();

    /** Parameter descriptions */
    private HashMap<String, String> descriptions = new HashMap<String, String>();

    private CParam4Company() {
        initMessages();
    }

    public static CParam4Company getInstance() {
        return instance;
    }

    @Override
    public CujoPropertyList readProperties() {
        return pl;
    }

    private void initMessages() {
        setDescription(companyName, "The name of your Company");
        setDescription(companyAddress, "Company address");
        setDescription(productSupport, "Is supported a Product based structure? (Note: the parameter is not implemented in the current version)");
        setDescription(firstDayOfWeek, "Set the first day of the week. Value 1 means Sunday, value 2 means Monday. The parameter is reasonable in a date selection on a Report panel");
        setDescription(reportShowsPrivateEvents, "Show private events in the Event Report");
    }


    // -------------- GET PARAMETERS --------------

    /** Returns a company name */
    public String getCompanyName() {

        CujoProperty property = companyName;

        AbstractCujo p = map.get(property.getName());
        String result = p instanceof CSingleComParam
                ? ((CSingleComParam)p).getTextValue()
                : (String) property.getDefault();
        return result;
    }

    /** Returns a pomodoro interval */
    public String getCompanyAddress() {

        CujoProperty property = companyAddress;

        AbstractCujo p = map.get(property.getName());
        String result = p instanceof CSingleComParam
                ? ((CSingleComParam)p).getTextValue()
                : (String) property.getDefault();
        return result;
    }

    /** Returns JIRA server url */
    public String getJiraServerUrl() {
        CujoProperty property = jiraServerUrl;

        AbstractCujo p = map.get(property.getName());
        String result = p instanceof CSingleComParam
                ? ((CSingleComParam)p).getTextValue()
                : (String) property.getDefault();
        return result;
    }

    /** Is the Product supported ? */
    public boolean isProductSupported() {
        CujoProperty property = productSupport;

        AbstractCujo par = map.get(property.getName());
        Boolean result = par instanceof CSingleComParam
                ? "true".equals(((CSingleComParam)par).getTextValue())
                : (Boolean) property.getDefault();
        return result;
    }

    /** Is the Product supported ? */
    public boolean isReportPrivateEvents() {
        CujoProperty property = reportShowsPrivateEvents;

        AbstractCujo par = map.get(property.getName());
        Boolean result = par instanceof CSingleComParam
                ? "true".equals(((CSingleComParam)par).getTextValue())
                : (Boolean) property.getDefault();
        return result;
    }

    
}
