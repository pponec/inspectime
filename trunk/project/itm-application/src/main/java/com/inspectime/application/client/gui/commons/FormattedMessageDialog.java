package com.inspectime.application.client.gui.commons;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.Element;

/**
 * @author Jiří Gazárek
 * @email gazarek@effectiva.cz
 */
public class FormattedMessageDialog extends Dialog {

    private String title;
    private String htmlMessage;
    private Runnable refreshCommand;

    public FormattedMessageDialog(String title, String htmlMessage, Runnable command) {
        this.title = title;
        this.htmlMessage = htmlMessage;
        this.refreshCommand = command;
    }

    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);
        setLayout(new FitLayout());

        setHeading(title);

        LayoutContainer container = new LayoutContainer();
        container.addText(htmlMessage);
        container.setScrollMode(Scroll.AUTO);

        add(container);
        getButtonById(Dialog.OK).setText("Close");
        getButtonById(Dialog.OK).addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                hide();
            }
        });
        addListener(Events.Hide, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                if (refreshCommand != null) {
                    refreshCommand.run();
                }
            }
        });
        setSize(800, 600);
    }
}
