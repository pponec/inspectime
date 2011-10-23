/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.layout;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;

/**
 *
 * @author Hampl
 */
public class Layout extends LayoutContainer {

    public Layout() {
        super();
        final BorderLayout layout = new BorderLayout();
        Tabs t = new Tabs(this);
        add(t);
    }
}
