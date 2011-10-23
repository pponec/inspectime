/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.controller;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.google.gwt.user.client.rpc.RemoteService;
import com.inspectime.application.client.ao.EventDay;
import java.util.Date;
import org.ujorm.gxt.client.CMessageException;
import org.ujorm.gxt.client.cquery.CQuery;
import org.ujorm.gxt.client.tools.ClientSerializableEnvelope;

/**
 * Service for Events
 * @author Ponec
 */
public interface EventController extends RemoteService {

    public String lockEventDay(Date requiredDay) throws CMessageException;

    public String lockEventDay(Date requiredDay, Long userId) throws CMessageException;

    /** Returns rows of the table by the parameters. */
    public EventDay getDbRows(CQuery query, PagingLoadConfig config, Date selectedDate) throws CMessageException;

    /* An workaround for the GXT serialization */
    public ClientSerializableEnvelope typeWorkaround(ClientSerializableEnvelope o);
    
}


