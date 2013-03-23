/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.service.def;

import com.inspectime.commons.bo.Company;
import java.util.Calendar;
import org.ujorm.Key;
import org.ujorm.core.KeyFactory;
import org.ujorm.extensions.UjoMiddle;

/**
 * Services for reading and writting Company parameters
 * @author Ponec
 */
public interface ParamCompService extends UjoMiddle<ParamCompService> {

    /** Key factory */
    public static final KeyFactory $f = KeyFactory.Builder.get(ParamCompService.class);
    
    /** Company Name */
    public static final Key<ParamCompService, String> companyName = $f.newKey("CompanyName", "My Company");
    /** Company Address */
    public static final Key<ParamCompService, String> companyAddress = $f.newKey("CompanyAddress", "");
    /** Is supported the Product? */
    public static final Key<ParamCompService, Boolean> productSupport = $f.newKey("ProductSupport", true);
    /** The First Day of the Week Day. */
    public static final Key<ParamCompService, Integer> firstDayOfWeek = $f.newKey("FirstDayOfWeek", Calendar.getInstance().getFirstDayOfWeek());
    /** Show Private Events in Event Report */
    public static final Key<ParamCompService, Boolean> reportShowsPrivateEvents = $f.newKey("ReportShowsPrivateEvents", true);
    /** Show Private Events in Event Report */
    public static final Key<ParamCompService, String> jiraServerUrl = $f.newKey("JiraServerUrl", "");
    
    /** Lock the Factory */
    public static final boolean $locked = $f.lockAndSize()>=0;    


    /** Return company of the logged user. */
    public Company getUserCompany();

    /** Get a company parameter value. */
    public <T> T getValue(Key<ParamCompService,T> property, Company company);

    /** Set a company parameter value. */
    public <T> void setValue(Key<ParamCompService,T> property, T value, Company company);
}
