/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.controller;

import com.google.gwt.user.client.rpc.RemoteService;
import org.ujorm.gxt.client.CMessageException;
import org.ujorm.gxt.client.tools.ClientSerializableEnvelope;

/**
 * Service for Events
 * @author Ponec
 */
public interface CompanyController extends RemoteService {

    public int getNextTaskSequence() throws CMessageException;

    /* An workaround for the GXT serialization */
    public ClientSerializableEnvelope typeWorkaround(ClientSerializableEnvelope o);
    
}


