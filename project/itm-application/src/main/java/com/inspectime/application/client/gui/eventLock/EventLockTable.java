/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */


package com.inspectime.application.client.gui.eventLock;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.inspectime.application.client.Application;
import com.inspectime.application.client.ao.CRoleEnum;
import com.inspectime.application.client.cbo.CEventLock;
import com.inspectime.application.client.cbo.CUser;
import com.inspectime.application.client.clientTools.AbstractEditDialog;
import com.inspectime.application.client.service.CParam4User;
import java.util.List;
import org.ujorm.gxt.client.CujoProperty;
import org.ujorm.gxt.client.cquery.CCriterion;
import org.ujorm.gxt.client.cquery.CQuery;
import com.inspectime.application.client.clientTools.AbstractTable;

/**
 * Event Lock
 * @author Ponec
 */
public class EventLockTable<CUJO extends CEventLock> extends AbstractTable<CUJO> {

    /** The reload button */
    private String buttonReload = "Reload";
    private boolean adminRole = false;

    public EventLockTable(CQuery<CUJO> query) {
        super(query);
    }

    public EventLockTable() {
    }

    @Override
    protected void onRender(Element parent, int index) {
        adminRole = CParam4User.getInstance().getRoles().contains(CRoleEnum.ADMIN);
        super.onRender(parent, index);
        grid.setAutoExpandMax(Integer.MAX_VALUE);
        grid.setAutoExpandColumn(getQuery().getColumnConfig(CEventLock.modifiedBy.add(CUser.name)).getId());

    }

    /** Create a new edit dialog. */
    @Override
    protected AbstractEditDialog<CUJO> createDialogInstance() {
        return new EventLockEditDialog();
    }

    /** Specify a list of the Table columns */
    @Override
    protected CujoProperty[] createTableColumns() {
        return new CujoProperty[]
          { CEventLock.lockDate
          , CEventLock.user.add(CUser.name)
          , CEventLock.modified
          , CEventLock.active
          , CEventLock.modifiedBy.add(CUser.name)
        };
    }

    /** Returns a default database query. */
    @Override
    protected CQuery<? super CUJO> createDefaultQuery() {
        CQuery<CEventLock> result = CQuery.newInstance(CEventLock.class, createTableColumns());

        CCriterion<CEventLock> crn = adminRole ? null : CEventLock.active.whereEq(true);
        result.setCriterion(crn);
        result.orderBy
                ( CEventLock.lockDate.descending()
                , CEventLock.user.add(CUser.name)
                , CEventLock.id.descending()
                );

        // Properties:
        result.getColumnConfig(CEventLock.user.add(CUser.name)).setHeader("User");
        result.getColumnConfig(CEventLock.modifiedBy.add(CUser.name)).setHeader("Modified By");
        result.getColumnConfig(CEventLock.active).setHidden(false);
        result.getColumnConfig(CEventLock.active).setRenderer(new GridCellRenderer() {
            @Override
            public Object render(ModelData model, String property, ColumnData config, int rowIndex, int colIndex, ListStore store, Grid grid) {
                Object value = model.get(property);
                return Boolean.TRUE.equals(value) ? "locked" : "x" ;
            }
        });
        

        return result;
    }

    @Override
    protected void createButtons(final LayoutContainer buttonContainer, final PagingToolBar toolBar) {
        Button btnReload = addButton(buttonReload, Application.ICONS.reloadData(), buttonContainer);
        Button buttonD = addButton(buttonDelete="Unlock", Application.ICONS.unlock(), buttonContainer);

        // Button Reload:
        if (btnReload != null) {
            btnReload.addSelectionListener(new SelectionListener<ButtonEvent>() {
                @Override
                public void componentSelected(ButtonEvent be) {
                    reloadTable();
                    //toolBar.refresh();
                }
            });
        }

        // DELETE button action:
        if (buttonD != null) {
            buttonD.setToolTip("[F8], [DEL]");
            buttonD.addSelectionListener(new SelectionListener<ButtonEvent>() {
                @Override
                public void componentSelected(ButtonEvent ce) {
                    deleteItem(grid.getSelectionModel().getSelectedItems(), toolBar);
                }
            });
        }
    }

    @Override
    protected Button addButton(String label, AbstractImagePrototype icon, LayoutContainer c) {
        if (label.equals(buttonDelete) && !adminRole) {
            return null;
        } else {
            return super.addButton(label, icon, c);
        }
    }

    @Override
    protected void deleteItem(final List<CUJO> selectedItems, final PagingToolBar toolBar) {
        if (adminRole) {
            super.deleteItem(selectedItems, toolBar);
        } else {
            return;
        }
    }

    /** Public Reload table */
    @Override
    public void reloadTable() {
        super.reloadTable();
    }

}
