/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.clientTools;

import com.extjs.gxt.ui.client.event.ColorPaletteEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.ColorPalette;
import com.extjs.gxt.ui.client.widget.form.TriggerField;
import com.extjs.gxt.ui.client.widget.menu.ColorMenu;
import com.google.gwt.user.client.Element;
import org.ujorm.gxt.client.tools.ColorGxt;

public class ColorField extends TriggerField<String> {

    /** The method SetValue generates a random collor if the value is null. */
    boolean randomSupported = true;

    public ColorField() {
        setEditable(false);
    }

    @Override
    protected void onTriggerClick(ComponentEvent ce) {
        super.onTriggerClick(ce);

        ColorMenu menu = new ColorMenu();
        menu.getColorPalette().addListener(Events.Select, new Listener<ColorPaletteEvent>() {

            @Override
            public void handleEvent(ColorPaletteEvent ce) {
                setValue(ce.getColor());
            }
        });
        menu.show(getElement(), "b");
    }

    @Override
    public void setValue(String value) {
        if (randomSupported && value==null) {
            value = getRandomColor();
        }
        
        super.setValue(value);

        if (isRendered()) {
            input.setStyleAttribute("backgroundColor", "#" + (value == null ? "FFFFFF" : new ColorGxt(value).getWhiteAlpha50()));
        }
    }

    @Override
    public void render(Element target, int index) {
        super.render(target, index);

        input.setStyleAttribute("backgroundColor", "#" + (value == null ? "DDDDDDDD" : new ColorGxt(value).getWhiteAlpha50()));
        input.setStyleAttribute("backgroundImage", "none");
    }

    public void setIntValue(int rgb) {
        StringBuilder sb = new StringBuilder(6);
        String hex = Integer.toHexString(rgb);
        for (int i = hex.length(); i != 6; ++i) {
            sb.append('0');
        }
        sb.append(hex);
        setValue(sb.toString());
    }

    public int getIntValue() {
        return value == null ? null : Integer.parseInt(value, 16);
    }


    /** The NULL value invokes a Random generator. */
    public void setValue(ColorGxt color) {
        setValue(color!=null ? color.toString() : null);
    }

    public ColorGxt getValueAsColor() {
        String result = getValue();
        return result!=null ? new ColorGxt(result) : null;
    }


    /** Returns random color */
    private String getRandomColor() {
        String[] colors = new ColorPalette().getColors();
        int i = com.google.gwt.user.client.Random.nextInt(colors.length);
        return colors[i];
    }

    /** The method SetValue generates a random collor if the value is null. */
    public boolean isRandomSupported() {
        return randomSupported;
    }

    /** The method SetValue generates a random collor if the value is null. */
    public void setRandomSupported(boolean randomSupported) {
        this.randomSupported = randomSupported;
    }


}
