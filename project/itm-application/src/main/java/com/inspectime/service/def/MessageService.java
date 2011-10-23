/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */


package com.inspectime.service.def;

import org.ujorm.gxt.client.ao.MessageSuffix;
import org.ujorm.UjoProperty;

/**
 * Message Service in scope session
 * @author Ponec
 */
public interface MessageService {

    /** Returns a transl */
    public String get(String key);
    public String get(String key, Object... args);
    public String get(UjoProperty property, MessageSuffix suffix);
    public String getNullable(String key, Object... args);

    /** Returns a localized side label */
    public String getSideLabel(UjoProperty property);
    /** Returns a localized table column label */
    public String getColumnLabel(UjoProperty property);
    /** Returns localized descrition or tooltip */
    public String getDescription(UjoProperty property);

}
