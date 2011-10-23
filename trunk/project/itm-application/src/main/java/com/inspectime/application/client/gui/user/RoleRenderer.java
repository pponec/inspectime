/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.gui.user;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.inspectime.application.client.ao.CRoleEnum;
import org.ujorm.gxt.client.Cujo;
import org.ujorm.gxt.client.tools.ColorGxt;

/**
 * Render a role names
 * @see ColorGxt
 * @author Pavel Ponec
 */
public class RoleRenderer implements GridCellRenderer<Cujo> {

    final private StringBuilder out = new StringBuilder(4);
    final String roleString;
    final String indexString;

    public RoleRenderer(CRoleEnum... roles) {
        StringBuilder rs = new StringBuilder();
        StringBuilder is = new StringBuilder();

        for (CRoleEnum roleEnum : roles) {
            rs.append(roleEnum.name().charAt(0));
            is.append(roleEnum.ordinal());
        }

        roleString = rs.toString();
        indexString = is.toString();
    }

    @Override
    public Object render(Cujo model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<Cujo> store, Grid<Cujo> grid) {
        String roles = model.get(property).toString();
        out.setLength(0);
        for (int i = 0, max=roles.length(); i < max; ++i) {
            int j = indexString.indexOf(roles.charAt(i));
            if (j>=0) {
                if (out.length()>0) {
                    out.append(',');
                }
                out.append(roleString.charAt(j));
            }
        }
        return out.toString();
    }
}
