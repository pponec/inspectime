/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.gui.registration;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.widget.form.CheckBox;

/**
 * Combo-box with HTTP link.
 * @author Pavel Ponec
 */
public class LinkCheckBox extends CheckBox {

    private boolean linkEnabled = false;

    /** Link */
    private String link;

    public LinkCheckBox(String link, boolean linkEnabled) {
        this.link = isValid(link) ? link : RegistrationDialog.DEFAULT_TERM_OF_USE_LINK ;
        this.linkEnabled = linkEnabled;
    }

    static private boolean isValid(CharSequence t) {
        return t!=null && t.length()>0;
    }

    /** GetBoxLabel */
    public El getBoxLabelEl() {
        return boxLabelEl;
    }

    @Override
    protected void onRender(com.google.gwt.user.client.Element target, int index) {
        super.onRender(target, index);

        if (linkEnabled) {
            final String html = "<a style='vertical-align: middle; text-decoration: underline;' target='_blank' href='"
                    +link + "'>Terms of use</a>"
                    ;
            El.fly(getElement()).createChild(html);
        }
    }

}
