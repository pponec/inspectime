/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.service.impl;

import com.inspectime.commons.bo.Event;

/**
 * Report Service Implementation
 * @author Ponec
 */
abstract public class AbstractReportEventServiceImpl extends AbstractServiceImpl<Event> {


    @Override
    public Class<Event> getDefaultClass() {
        return Event.class;
    }


}