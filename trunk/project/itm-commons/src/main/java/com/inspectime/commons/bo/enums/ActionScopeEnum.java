/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.commons.bo.enums;

import java.io.Serializable;
import org.ujorm.extensions.ValueExportable;

/**
 * Task Action Scope
 * @author Ponec
 */
public enum ActionScopeEnum implements ValueExportable, Serializable {

    PRIVATE("PV"),
    PROJECT("PJ"),
    PUBLIC( "PL"),
    ;

   private final String key;

    private ActionScopeEnum(String key) {
        this.key = key;
    }

    @Override
    public String exportToString() {
        return key;
    }
}
