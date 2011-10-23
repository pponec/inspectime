/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */


package com.inspectime.application.client.clientTools;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import org.ujorm.gxt.client.Cujo;
import org.ujorm.gxt.client.tools.ColorGxt;

/**
 * Singleton Simple Color Renderer for columns type ColorGxt.
 * @see ColorGxt
 * @author Pavel Ponec
 */
public class ColorRenderer implements GridCellRenderer<Cujo> {

    private static ColorRenderer instance;

    private ColorRenderer() {
    }

    @Override
    public Object render(Cujo model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<Cujo> store, Grid<Cujo> grid) {
        ColorGxt colorGxt = (ColorGxt) model.get(property);
        String color = colorGxt!=null ? colorGxt.getBlackAlpha50() : "000000";
        return "<span style='color: #" + color + "'>" + color + "</span>";
    }


    /** Get Instance */
    public static ColorRenderer getInstance() {
        if (instance==null) {
            instance = new ColorRenderer();
        }
        return instance;
    }

}
