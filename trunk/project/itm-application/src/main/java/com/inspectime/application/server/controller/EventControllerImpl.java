/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.server.controller;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.inspectime.application.client.ao.EventDay;
import com.inspectime.application.client.controller.EventController;
import com.inspectime.service.def.EventLockService;
import com.inspectime.service.def.EventService;
import java.util.Date;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.ujorm.gxt.client.CMessageException;
import org.ujorm.gxt.client.Cujo;
import org.ujorm.gxt.client.controller.TableController;
import org.ujorm.gxt.client.cquery.CQuery;
import org.ujorm.gxt.client.tools.ClientSerializableEnvelope;

/**
 * Implementation of the EventController
 * @author Ponec
 */
@org.springframework.stereotype.Controller("eventController")
public class EventControllerImpl extends RemoteServiceServlet implements EventController {

    final static private Logger LOGGER = Logger.getLogger(TableControllerImpl.class.getName());

    @Autowired
    private EventService eventService;

    @Autowired
    private EventLockService eventLockService;

    @Autowired
    private TableController tableController;

    @Override
    public String lockEventDay(Date requiredDay) throws CMessageException {
        eventService.assertServerContext();
        return eventLockService.lockEventDay(requiredDay);
    }

    @Override
    public String lockEventDay(Date requiredDay, Long userId) throws CMessageException {
        eventService.assertServerContext();
        return eventLockService.lockEventDay(requiredDay, userId);
    }

    @Override
    public EventDay getDbRows(CQuery cQuery, PagingLoadConfig config, Date selectedDate) throws CMessageException {
        eventService.assertServerContext();

        EventDay result = new EventDay();
        PagingLoadResult<Cujo> rows = tableController.getDbRows(cQuery, config);
        result.setEventsResult(rows);

        EventDay temp = eventService.getEventDayProperties(selectedDate);
        result.addProperties(temp);
        return result;
    }

    @Override
    public ClientSerializableEnvelope typeWorkaround(ClientSerializableEnvelope o) {
        return o;
    }
  
}
