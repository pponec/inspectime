/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.gui.registration;

import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Random;

/**
 * BotField
 * @author Pavel Ponec
 */
public class BotField extends TextField<String> implements Validator {

    String result = "";

    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);

        boolean multi = Random.nextBoolean();
        boolean posit = multi ? true : Random.nextBoolean();
        int o1 = 1 + Random.nextInt(multi ? 10 : 20);
        int o2 = (multi ? 1 : 11) + Random.nextInt(10);

        if (o2 > o1) {
            int t = o1;
            o1 = o2;
            o2 = t;
        }

        String label = "Antispam (" + o1 + (multi ? " * " : posit ? " + " : " - ") + name(o2) + ")";
        setFieldLabel(label);
        setAllowBlank(false);

        result = multi
               ? String.valueOf(o1 * o2)
               : String.valueOf(o1 + (posit ? o2 : -o2));
        setValidator(this);
    }

    /** Get name of number */
    private String name(int i) {
        if (Random.nextInt(100)<10) {
           return String.valueOf(i);
        } else switch(i) {
           case 1: return "one";
           case 2: return "two";
           case 3: return "three";
           case 4: return "four";
           case 5: return "five";
           case 6: return "six";
           case 7: return "seven";
           case 8: return "eight";
           case 9: return "nine";
           case 10: return "ten";
           case 15: return "fifteen";
           case 20: return "twenty";
           default: return String.valueOf(i);
       }
    }

    @Override
    public String validate(Field<?> field, String value) {
        if (result.equals(value)) {
            return null;
        } else {
            return "Wrong value";
        }
    }
}
