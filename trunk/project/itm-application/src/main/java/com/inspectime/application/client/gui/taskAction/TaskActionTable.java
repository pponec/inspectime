/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.gui.taskAction;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.inspectime.application.client.cbo.CTaskAction;
import org.ujorm.gxt.client.CujoProperty;
import org.ujorm.gxt.client.cquery.CQuery;
import com.inspectime.application.client.clientTools.AbstractTable;

/**
 *
 * @author Ponec
 */
public class TaskActionTable<CUJO extends CTaskAction> extends AbstractTable<CUJO> {

//    private CNode node;
//    private CScript script;

    public TaskActionTable(CQuery<CUJO> query) {
        super(query);
    }

    public TaskActionTable() {
    }



    @Override
    protected Button addButton(String label, AbstractImagePrototype icon, LayoutContainer c) {
        Button result = super.addButton(label, icon, c);
        if (result!=null && label==buttonCreate) {
            result.setText("Add");
        }
        if (result!=null && label==buttonDelete) {
            result.setText("Remove");
        }
        if (result!=null && label==buttonUpdate) {
            result.setVisible(false);
        }

        return result;
    }



    /** Create a new edit dialog. */
    @Override
    protected TaskActionEditDialog<CUJO> createDialogInstance() {
        return new TaskActionEditDialog();
    }

    /** Specify a list of the Table columns */
    @Override
    protected CujoProperty[] createTableColumns() {
        return new CujoProperty[]
        {
        };
    }

    /** Create a default database query. */
    @Override
    public CQuery<? super CUJO> createDefaultQuery() {
        CQuery<CTaskAction> result = CQuery.newInstance(CTaskAction.class);
        return result;
    }

}
