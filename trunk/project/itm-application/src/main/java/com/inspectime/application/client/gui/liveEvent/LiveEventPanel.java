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
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.KeyEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.WidgetListener;
import com.extjs.gxt.ui.client.util.DelayedTask;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.google.gwt.user.client.Event;
import com.inspectime.application.client.cbo.CEvent;
import com.inspectime.application.client.gui.event.EventEditDialog;
import java.util.Date;
import org.ujorm.gxt.client.cquery.CQuery;
import com.inspectime.application.client.cbo.CProject;
import com.inspectime.application.client.cbo.CUser;
import com.inspectime.application.client.gui.event.AbstractEventTable;
import com.inspectime.application.client.gui.event.EventTable;
import org.ujorm.gxt.client.CujoProperty;
import org.ujorm.gxt.client.commons.KeyCodes;
import org.ujorm.gxt.client.cquery.CCriterion;

/**
 * Live Event Table
 *
 * @author Ponec
 */
public class LiveEventPanel<CUJO extends CEvent> extends AbstractEventTable<CUJO> {

    /**
     * Value 0 means OFF, other values means an auto refresh interval in
     * minutes.
     */
    private static final int AUTO_REFRESH_INTERVAL = 1;
    private DelayedTask autoRefreshTask;
    private boolean reloadRequest = false;

    public LiveEventPanel(CQuery<CUJO> query) {
        super(query);
        embedded = true;
    }

    public LiveEventPanel() {
        embedded = true;
    }

    @Override
    protected void onRender(com.google.gwt.user.client.Element parent, int index) {
        super.onRender(parent, index);

        grid.setAutoExpandColumn(getQuery().getColumnConfig(CEvent.description).getId());
        grid.setAutoExpandMax(Integer.MAX_VALUE);

        autoRefreshTask = new DelayedTask(new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                if (AUTO_REFRESH_INTERVAL > 0) {
                    if (isVisible()) {
                        reloadTable();
                    } else {
                        reloadRequest = true;
                    }
                }
            }
        });


        grid.addListener(Events.OnKeyDown, new Listener<GridEvent>() {
            @Override
            public void handleEvent(GridEvent ge) {
                if (ge.getEvent().getKeyCode() == KeyCodes.F5) {
                    reloadTable();
                }
                ge.stopEvent();
            }
        });

        /*        

         BorderLayout borderLayout = new BorderLayout();

         ContentPanel evTable = new ContentPanel();
         evTable.setHeaderVisible(false);
         evTable.setLayout(borderLayout);
         evTable.add(grid, new BorderLayoutData(LayoutRegion.CENTER, 0, 0, 0));
        
         cpTable.setHeaderVisible(false);
         borderLayout = new BorderLayout();
         cpTable.setLayout(borderLayout);
         cpTable.add(evTable, new BorderLayoutData(LayoutRegion.CENTER, 0, 0, 0));
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
         * 
         */
    }

    /**
     * Specify a list of the Table columns
     */
    @Override
    protected CujoProperty[] createTableColumns() {
        return new CujoProperty[]{CEvent.user.add(CUser.name), CEvent.user.add(CUser.login), CEvent.project.add(CProject.name), CEvent._task_code, CEvent.day, CEvent.startTime_, CEvent.period_, CEvent.description
        };
    }

    /**
     * Returns a default database query.
     */
    @Override
    protected CQuery<? super CUJO> createDefaultQuery() {
        CQuery<CEvent> result = CQuery.newInstance(CEvent.class, createTableColumns());
        Date selectedDay = new Date();

        CCriterion<CEvent> crn1 = CEvent.active.whereEq(true);
        CCriterion<CEvent> crn2 = CCriterion.where((CujoProperty) CEvent.day, selectedDay); // Hack: http://code.google.com/p/google-web-toolkit/issues/detail?id=87
        result.setCriterion(crn1.and(crn2));
        result.setRelations(2);

        // GUI:
        result.getColumnConfig(CEvent.user.add(CUser.login)).setHidden(true);
        result.getColumnConfig(CEvent.day).setHidden(true);

        // Unsortable Columns:
        for (ColumnConfig cc : result.getCujoModel().getColumns()) {
            cc.setSortable(false);
        }

        result.getColumnConfig(CEvent.project.add(CProject.name)).setHeader("Project");
        result.getColumnConfig(CEvent._task_code).setHeader("Task");

        // Assign Renderer:
        GridCellRenderer<CEvent> renderer = EventTable.createRenderer();
        for (ColumnConfig cc : result.getCujoModel().getColumns()) {
            cc.setRenderer(renderer);
        }

        // Sortable Columns:
        for (ColumnConfig cc : result.getCujoModel().getColumns()) {
            cc.setSortable(true);
        }

        result.orderByMany(); // Reset sort

        result.setContext(LIVE_CONTEXT);
        return result;
    }

    @Override
    protected void createButtons(final LayoutContainer buttonContainer, final PagingToolBar toolBar) {
    }

    @Override
    protected EventEditDialog createDialogInstance() {
        return null;
    }

    @Override
    protected void onTableLoad(PagingLoadResult<CUJO> result) {
        super.onTableLoad(result);
        if (AUTO_REFRESH_INTERVAL > 0) {
            autoRefreshTask.delay(AUTO_REFRESH_INTERVAL * 60 * 1000);
        }
    }

    @Override
    public void reloadTable() {
        this.reloadRequest = false;
        super.reloadTable();
    }

    @Override
    protected Date getSelectedDay() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected String getTableTitle() {
        return "Team activity";
    }
}