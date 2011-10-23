/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.ao;

import java.io.Serializable;
import org.ujorm.gxt.client.CujoProperty;

/**
 * Report type Ponec
 * @author Pavel Ponec
 */
public class ReportRequest implements Serializable {

    /** Report enum */
    private ReportTypeEnum reportTypeEnum;
    /** Properties to the Event relation entity */
    private String[] eventProperties;

    /** Serialization constructor */
    public ReportRequest() {
    }

    public ReportRequest(ReportTypeEnum reportTypeEnum, CujoProperty ... eventProperties) {
        this.reportTypeEnum = reportTypeEnum;
        this.eventProperties = new String[eventProperties.length];

        for (int i = eventProperties.length-1; i>=0; i--) {
            this.eventProperties[i] = eventProperties[i].getName();
        }
    }

    public ReportTypeEnum getReportTypeEnum() {
        return reportTypeEnum;
    }

    public String[] getEventProperties() {
        return eventProperties;
    }

    /** Is the report request type of Summary ? */
    public boolean isSummaryType() {
        return eventProperties.length==2 && eventProperties[0].equals(eventProperties[1]);
    }

}
