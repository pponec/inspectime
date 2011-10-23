/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */
package com.inspectime.application.client.layout;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.google.gwt.user.client.Window;

public class Tabs extends LayoutContainer {

    private Layout layout;

    Tabs(Layout l) {
        layout = l;
        init();
    }

    private void init() {

        String txt = "ahoj";
        TabPanel folder = new TabPanel();
        folder.setWidth(Window.getClientWidth());
        folder.setAutoHeight(true);
        TabItem shortText = new TabItem("Short Text");
        shortText.addStyleName("pad-text");
        shortText.addText(txt);
        folder.add(shortText);
        TabItem longText = new TabItem("Long Text");
        longText.addStyleName("pad-text");
        longText.addText(txt + "<br>" + txt);
        folder.add(longText);

        add(folder);
    }
}
/*

 TabPanel panel = new TabPanel();
        panel.setPlain(true);
        panel.setSize(450, 250);
        TabItem normal = new TabItem("Normal");
        normal.addStyleName("pad-text");
        normal.addText("Just a plain old tab");
        panel.add(normal);
        TabItem ajax1 = new TabItem("Ajax Tab");
        ajax1.setScrollMode(Scroll.AUTO);
        ajax1.addStyleName("pad-text");
        ajax1.setAutoLoad(new RequestBuilder(RequestBuilder.GET, GWT.getHostPageBaseURL() + "data/ajax1.html"));
        panel.add(ajax1);
        TabItem eventTab = new TabItem("Event Tab");
        eventTab.addListener(Events.Select, new Listener<ComponentEvent>() {

            public void handleEvent(ComponentEvent be) {
                Window.alert("Event Tab Was Selected");
            }
        });
        eventTab.addStyleName("pad-text");
        eventTab.addText("I am tab 4's content. I also have an event listener attached.");
        panel.add(eventTab);
        TabItem disabled = new TabItem("Disabled");
        disabled.setEnabled(false);
        panel.add(disabled);
        
 */
