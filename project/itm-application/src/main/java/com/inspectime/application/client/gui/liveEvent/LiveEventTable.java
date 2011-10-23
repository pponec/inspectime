/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.gui.liveEvent;

import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.DelayedTask;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.inspectime.application.client.cbo.CEvent;
import com.inspectime.application.client.gui.event.EventEditDialog;
import org.ujorm.gxt.client.cquery.CQuery;
import com.inspectime.application.client.Application;
import com.inspectime.application.client.cbo.CProject;
import com.inspectime.application.client.cbo.CUser;
import com.inspectime.application.client.gui.event.ChartItem;
import com.inspectime.application.client.gui.event.EventTable;
import org.ujorm.gxt.client.CujoProperty;
import org.ujorm.gxt.client.tools.ColorGxt;

/**
 * Live Event Table
 * @author Ponec
 */
public class LiveEventTable<CUJO extends CEvent> extends EventTable<CUJO> {

    /** Value 0 means OFF, other values means an auto refresh interval in minutes. */
    private static final int AUTO_REFRESH_INTERVAL = 1;
    private DelayedTask autoRefreshTask;
    private boolean reloadRequest = false;

    /** The reload button */
    private String buttonReload = "Reload";
    
    public LiveEventTable(CQuery<CUJO> query) {
        super(query);
    }

    public LiveEventTable() {
    }

    @Override
    protected void onRender(com.google.gwt.user.client.Element parent, int index) {
        super.onRender(parent, index);

        autoRefreshTask = new DelayedTask(new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                if (AUTO_REFRESH_INTERVAL>0) {
                    if (isVisible()) {
                        reloadTable();
                    } else {
                        reloadRequest = true;
                    }
                }
            }
        });
    }

    /** Specify a list of the Table columns */
    @Override
    protected CujoProperty[] createTableColumns() {
        return new CujoProperty[]
          { CEvent.user.add(CUser.name)
          , CEvent.user.add(CUser.login)
          , CEvent.project.add(CProject.name)
          , CEvent._task_code
          , CEvent.day
          , CEvent.startTime_
          , CEvent.period_
          , CEvent.description
        };
    }

    /** Returns a default database query. */
    @Override
    protected CQuery<? super CUJO> createDefaultQuery() {
        CQuery<CEvent> result = (CQuery<CEvent>) super.createDefaultQuery();

        // Sortable Columns:
        for (ColumnConfig cc : result.getCujoModel().getColumns()) {
            cc.setSortable(true);
        }
        
        result.orderByMany(); // Reset sort
        result.getColumnConfig(CEvent.user.add(CUser.login)).setHidden(false);
        result.getColumnConfig(CEvent.day).setHidden(false);

        result.setContext(LIVE_CONTEXT);
        return result;
    }

    @Override
    protected void createButtons(final LayoutContainer buttonContainer, final PagingToolBar toolBar) {        
        Button btnReload = addButton(buttonReload, Application.ICONS.reloadData(), buttonContainer);

        // Button Events:
        if (btnReload != null) {
            btnReload.addSelectionListener(new SelectionListener<ButtonEvent>() {
                @Override
                public void componentSelected(ButtonEvent be) {
                    reloadTable();
                    //toolBar.refresh();
                }
            });
        }
    }

    /** Is the toolbar enabled */
    @Override
    protected boolean isToolBarEnabled() {
        return false;
    }
    
    @Override
    protected EventEditDialog createDialogInstance() {
        return null;
    }

    /** Create Chart Item */
    @Override
    protected ChartItem createChartItem(final Long id, final String name, final ColorGxt color) {
        final boolean calcByTime = false;
        return new ChartItem(id, name, color, calcByTime);
    }

    @Override
    protected String getProjectsChartTitle() {
        return "Users by Project";
    }
    @Override
    protected String getAccountsChartTitle() {
        return "Users by Account";
    }
    @Override
    protected String getCustomersChartTitle() {
        return "Users by Customer";
    }

    @Override
    protected void onTableLoad(PagingLoadResult<CUJO> result) {
        super.onTableLoad(result);
        if (AUTO_REFRESH_INTERVAL>0) {
            autoRefreshTask.delay(AUTO_REFRESH_INTERVAL*60*1000);
        }
    }

    /** Start auto refresh task. */
    public void onSelectLiveEventTable() {
        if (autoRefreshTask != null && reloadRequest) {
            reloadTable();
        }
    }

    @Override
    public void reloadTable() {
        this.reloadRequest = false;
        super.reloadTable();
    }






}
