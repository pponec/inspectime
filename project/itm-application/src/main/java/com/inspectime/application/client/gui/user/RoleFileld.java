/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.gui.user;


import com.extjs.gxt.ui.client.widget.form.AdapterField;
import com.extjs.gxt.ui.client.widget.form.CheckBoxGroup;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.google.gwt.user.client.Element;
import com.inspectime.application.client.ao.CRoleEnum;

/** This component supoort max 9 roles. 
 * @see CRoleEnum
 */
public class RoleFileld extends AdapterField {

    /** CheckBox */
    final private CheckBoxGroup checkGroup;

    /** There is supported max 9 Roles */
    final private CRoleEnum[] roles;

    /** Default constructor. */
    public RoleFileld(CRoleEnum... roles) {
        super(new CheckBoxGroup());
        this.checkGroup = (CheckBoxGroup) super.getWidget();
        this.roles = roles;

        for (CRoleEnum label : roles) {
            CheckBox radio = new CheckBox();
            radio.setBoxLabel(label.getRoleName());
            radio.setValue(false);
            radio.setToolTip(getTooltip(label));
            checkGroup.add(radio);
        }
    }

    /** Create tooltip for the Combo */
    final public String getTooltip(CRoleEnum role) {
        switch (role) {
            case ADMIN: return "Administrate the users";
            case MANAGER: return "Show reports include the Live Event tab";
            case ROOT: return "Super manager";
            default: return null;
        }
    }

    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);
    }

    /** An alias for the setValue(object) */
    @Override
    public void setRawValue(String aValue) {
        setValue(aValue);
    }

    @Override
    public void setValue(Object aValue) {
        final String value = aValue.toString();
        for (int i=0; i<roles.length; ++i) {
            char ch = (char) ('0' + roles[i].ordinal());
            boolean found = value.indexOf(ch)>=0;
            CheckBox radio = (CheckBox) checkGroup.get(i);
            radio.setValue(found);
        }
    }

    @Override
    public String getValue() {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<roles.length; ++i) {
            CheckBox radio = (CheckBox) checkGroup.get(i);
            if (radio.getValue()) {
                if (sb.length()>0) {
                    sb.append(',');
                }
                char ch = (char) ('0' + roles[i].ordinal());
                sb.append(ch);
            }
        }
        return sb.toString();
    }





}
