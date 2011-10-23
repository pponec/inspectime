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
import java.util.Date;

public interface UserControllerAsync
{

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.inspectime.application.client.controller.UserController
     */
    void getUserContext( AsyncCallback<com.inspectime.application.client.ao.CUserContext> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.inspectime.application.client.controller.UserController
     */
    void isLoginFree(java.lang.String login, AsyncCallback<java.lang.Boolean> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.inspectime.application.client.controller.UserController
     */
    void registerUser( com.inspectime.application.client.cbo.CUser newUser, com.inspectime.application.client.cbo.CCompany company, AsyncCallback<java.lang.String> callback );

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.inspectime.application.client.controller.UserController
     */
    void isUserAgreemnt(Date day, AsyncCallback<java.lang.Boolean> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.inspectime.application.client.controller.UserController
     */
    void sendUserAgreemnt( AsyncCallback<java.lang.Boolean> callback );

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.inspectime.application.client.controller.UserController
     */
    void logout(java.util.Date localDate, AsyncCallback<java.lang.Boolean> callback );

    /**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static class Util 
    { 
        private static UserControllerAsync instance;

        public static UserControllerAsync getInstance()
        {
            if ( instance == null )
            {
                instance = (UserControllerAsync) GWT.create( UserController.class );
                ServiceDefTarget target = (ServiceDefTarget) instance;
                target.setServiceEntryPoint( GWT.getModuleBaseURL() + "controller/UserController.rpc" );
            }
            return instance;
        }
    }
}
