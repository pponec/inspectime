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
import org.ujorm.extensions.Property;
import org.ujorm.extensions.UjoMiddle;

/**
 * Services for reading and writting Company parameters
 * @author Ponec
 */
public interface ParamCompService extends UjoMiddle<ParamCompService> {

    /** Company Name */
    public static final Property<ParamCompService, String> companyName = Property.newInstance("CompanyName", "My Company");
    /** Company Address */
    public static final Property<ParamCompService, String> companyAddress = Property.newInstance("CompanyAddress", "");
    /** Is supported the Product? */
    public static final Property<ParamCompService, Boolean> productSupport = Property.newInstance("ProductSupport", true);
    /** The First Day of the Week Day. */
    public static final Property<ParamCompService,Integer> firstDayOfWeek = Property.newInstance("FirstDayOfWeek", Calendar.getInstance().getFirstDayOfWeek());
    /** Show Private Events in Event Report */
    public static final Property<ParamCompService,Boolean> reportShowsPrivateEvents = Property.newInstance("ReportShowsPrivateEvents", true);


    /** Return company of the logged user. */
    public Company getUserCompany();

    /** Get a company parameter value. */
    public <T> T getValue(Key<ParamCompService,T> property, Company company);

    /** Set a company parameter value. */
    public <T> void setValue(Key<ParamCompService,T> property, T value, Company company);
}
