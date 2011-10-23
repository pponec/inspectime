/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.ao;

import com.extjs.gxt.ui.client.widget.Label;

/**
 * Standard Styled Label
 * @author Pavel Ponec
 */
public class StandardLabel extends Label {

    public StandardLabel(String text, String styleName) {
        super(text);
        setStyleName(styleName);
    }

    public StandardLabel(String text) {
        this(text, "standardLabel");
    }



}
