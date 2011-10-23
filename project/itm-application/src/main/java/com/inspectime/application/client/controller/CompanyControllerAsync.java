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

public interface CompanyControllerAsync {

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.inspectime.application.client.controller.CompanyController
     */
    void getNextTaskSequence(AsyncCallback<java.lang.Integer> callback);

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.inspectime.application.client.controller.CompanyController
     */
    void typeWorkaround(org.ujorm.gxt.client.tools.ClientSerializableEnvelope o, AsyncCallback<org.ujorm.gxt.client.tools.ClientSerializableEnvelope> callback);

    /**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static final class Util {

        private static CompanyControllerAsync instance;

        public static final CompanyControllerAsync getInstance() {
            if (instance == null) {
                instance = (CompanyControllerAsync) GWT.create(CompanyController.class);
                ServiceDefTarget target = (ServiceDefTarget) instance;
                target.setServiceEntryPoint(GWT.getModuleBaseURL() + "controller/CompanyController.rpc");
            }
            return instance;
        }

        private Util() {
            // Utility class should not be instanciated
        }
    }
}
