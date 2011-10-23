/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.server.controller;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.inspectime.application.client.controller.CompanyController;
import com.inspectime.service.def.CompanyService;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.ujorm.gxt.client.CMessageException;
import org.ujorm.gxt.client.tools.ClientSerializableEnvelope;

/**
 * Implementation of the EventController
 * @author Ponec
 */
@org.springframework.stereotype.Controller("companyController")
public class CompanyControllerImpl extends RemoteServiceServlet implements CompanyController {

    final static private Logger LOGGER = Logger.getLogger(TableControllerImpl.class.getName());

    @Autowired
    private CompanyService companyService;

    @Override
    public ClientSerializableEnvelope typeWorkaround(ClientSerializableEnvelope o) {
        return o;
    }

    @Override
    public int getNextTaskSequence() throws CMessageException {
        companyService.assertServerContext();
        return companyService.getNextTaskSequence();
    }
  
}
