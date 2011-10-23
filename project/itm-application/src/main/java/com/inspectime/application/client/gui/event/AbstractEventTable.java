/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */


package com.inspectime.application.client.gui.event;

import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.inspectime.application.client.controller.EventControllerAsync;
import java.util.Date;
import org.ujorm.gxt.client.Cujo;
import com.inspectime.application.client.ao.EventDay;
import com.inspectime.application.client.clientTools.AbstractTable;
import org.ujorm.gxt.client.CMessageException;
import org.ujorm.gxt.client.cquery.CQuery;

/**
 * AbstractEventTable
 * @author Ponec
 */
abstract public class AbstractEventTable<CUJO extends Cujo> extends AbstractTable<CUJO> {

    /** Live Context */
    public static final String LIVE_CONTEXT = "LiveEventTable";

    /** CurrentEventDay */
    private EventDay eventDay = new EventDay();


    /** Constuctor to use a default query implemented by the method. */
    public AbstractEventTable() {
        super();
    }

    /** Constuctor to use a selected model of the table. */
    public AbstractEventTable(CQuery<CUJO> query) {
        super(query);
    }

    /** Create Loader */
    @Override
    protected PagingLoader<PagingLoadResult<ModelData>> createLodader() {
        final CQuery query = getQuery();
        if (LIVE_CONTEXT.equals(query.getContext())) {
            return super.createLodader();
        }

        RpcProxy<PagingLoadResult<Cujo>> proxy = new RpcProxy<PagingLoadResult<Cujo>>() {
            @Override
            public void load(final Object loadConfig, final AsyncCallback<PagingLoadResult<Cujo>> callback) {
                final AsyncCallback<EventDay> callback2 = new AsyncCallback<EventDay>() {

                    @Override
                    public void onFailure(final Throwable caught) {
                        if (CMessageException.isSessionTimeout(caught)) {
                            redirectToLogin();
                        }
                        callback.onFailure(caught);
                        GWT.log("Error data loading ", caught);
                    }

                    @Override
                    public void onSuccess(EventDay result) {
                        callback.onSuccess(result.getEventsResult());
                        eventDay.addProperties(result);
                        onTableLoad((PagingLoadResult<CUJO>) result.getEventsResult());
                    }
                };

                EventControllerAsync service = EventControllerAsync.Util.getInstance();
                service.getDbRows(getQuery(), (PagingLoadConfig) loadConfig, getSelectedDay(), callback2);
            }
        };

        // loader
        final PagingLoader<PagingLoadResult<ModelData>> loader = new BasePagingLoader<PagingLoadResult<ModelData>>(proxy);
        loader.setRemoteSort(true);
        return loader;
    }

    /** Returns selected day */
    abstract protected Date getSelectedDay();

    /** Returns an Event Day attributes, however the eventsResult will be empty allways */
    public EventDay getEventDay() {
        return eventDay;
    }

    /** Is not the dialog locked ? */
    @Override
    protected boolean isActionPanelEnabled() {
        return !eventDay.isLocked();
    }

}
