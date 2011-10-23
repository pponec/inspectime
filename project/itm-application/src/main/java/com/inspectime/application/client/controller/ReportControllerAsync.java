/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */
package com.inspectime.application.client.controller;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public interface ReportControllerAsync {

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.inspectime.application.client.controller.ReportController
     */
    void getReportData(com.inspectime.application.client.ao.ReportRequest type, java.util.Date dateFrom, java.util.Date dateTo, Long userId, AsyncCallback<com.inspectime.application.client.ao.ReportData> callback);

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.inspectime.application.client.controller.ReportController
     */
    void getRangeRequest(com.inspectime.application.client.ao.DateRangeEnum rangeEnum, java.util.Date currentDay, AsyncCallback<com.inspectime.application.client.ao.DateRange> callback);

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.inspectime.application.client.controller.ReportController
     */
    void typeWorkaround(org.ujorm.gxt.client.tools.ClientSerializableEnvelope o, AsyncCallback<org.ujorm.gxt.client.tools.ClientSerializableEnvelope> callback);

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.inspectime.application.client.controller.ReportController
     */
    void typeWorkaround4WTime(com.inspectime.application.client.ao.WTime o, AsyncCallback<com.inspectime.application.client.ao.WTime> callback);

    /**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static final class Util {

        private static ReportControllerAsync instance;

        public static final ReportControllerAsync getInstance() {
            if (instance == null) {
                instance = (ReportControllerAsync) GWT.create(ReportController.class);
                ServiceDefTarget target = (ServiceDefTarget) instance;
                target.setServiceEntryPoint(GWT.getModuleBaseURL() + "controller/ReportController.rpc");
            }
            return instance;
        }

        private Util() {
            // Utility class should not be instanciated
        }
    }
}
