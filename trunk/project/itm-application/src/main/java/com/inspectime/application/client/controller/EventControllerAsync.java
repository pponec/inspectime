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

public interface EventControllerAsync {

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.inspectime.application.client.controller.EventController
     */
    void lockEventDay(java.util.Date requiredDay, AsyncCallback<java.lang.String> callback);

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.inspectime.application.client.controller.EventController
     */
    void lockEventDay(java.util.Date requiredDay, java.lang.Long userId, AsyncCallback<java.lang.String> callback);

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.inspectime.application.client.controller.EventController
     */
    void getDbRows(org.ujorm.gxt.client.cquery.CQuery query, com.extjs.gxt.ui.client.data.PagingLoadConfig config, java.util.Date selectedDate, AsyncCallback<com.inspectime.application.client.ao.EventDay> callback);

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.inspectime.application.client.controller.EventController
     */
    void typeWorkaround(org.ujorm.gxt.client.tools.ClientSerializableEnvelope o, AsyncCallback<org.ujorm.gxt.client.tools.ClientSerializableEnvelope> callback);

    /**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static final class Util {

        private static EventControllerAsync instance;

        public static final EventControllerAsync getInstance() {
            if (instance == null) {
                instance = (EventControllerAsync) GWT.create(EventController.class);
                ServiceDefTarget target = (ServiceDefTarget) instance;
                target.setServiceEntryPoint(GWT.getModuleBaseURL() + "controller/EventController.rpc");
            }
            return instance;
        }

        private Util() {
            // Utility class should not be instanciated
        }
    }
}
