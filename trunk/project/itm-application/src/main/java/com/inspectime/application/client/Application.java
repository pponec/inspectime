/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */
package com.inspectime.application.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.inspectime.application.client.gui.UIManager;
import com.inspectime.application.client.gui.commons.Icons;
import com.inspectime.application.client.gui.registration.LoginDialog;
import java.util.Date;
import org.ujorm.gxt.client.ClientCallback;
import org.ujorm.gxt.client.ClientClassConfig;
import org.ujorm.gxt.client.controller.TableControllerAsync;

public class Application implements EntryPoint {

    public static final Icons ICONS = GWT.create(Icons.class);

    @Override
    public void onModuleLoad() {

        ClientClassConfig ccc = ClientClassConfig.getInstance();

        TableControllerAsync service = TableControllerAsync.Util.getInstance();

        // HACK: nastavení času na serveru
        service.initClientTime(new Date(), new ClientCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                // nothing to do
            }
        });

        ccc.startUp(service, new ClientCallback(UIManager.getInstance()) {
            @Override
            public void onSuccess(Object result) {

                // Load The User Context:
                LoginDialog.loadServerUserContext(new Runnable() {
                    @Override
                    public void run() {
                        UIManager.getInstance().startup();
                    }
                });
            }
        });

        // com.inspectime.application.client.gui.UIManager.getInstance().startup(); // TODO: Use an 'Event Bus' to fight spaghetti.
    }
}
