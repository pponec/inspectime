/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.server.callback;

import com.inspectime.application.client.cbo.CUser;
import org.ujorm.gxt.server.UjoTranslatorCallback;
import com.inspectime.commons.bo.User;
import org.ujorm.Ujo;

/**
 * Translator User callback.
 * @author Pavel Ponec
 */
public class UserCallback<CUJO extends CUser> implements UjoTranslatorCallback<CUJO> {

    @Override
    public void copy(Ujo ujo, CUJO cuser) {
        final User user = (User) ujo;
        cuser.set(CUser.roles, user.getUserRoleAsString());
    }
}
