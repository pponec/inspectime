/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.clientTools;

import com.inspectime.application.client.gui.UIManager;
import org.ujorm.gxt.client.CLoginRedirectable;
import org.ujorm.gxt.client.Cujo;
import org.ujorm.gxt.client.cquery.CQuery;
import org.ujorm.gxt.client.gui.TablePanel;

/**
 * AbstractTable
 * @author Pavel Ponec
 */
abstract public class AbstractTable<CUJO extends Cujo> extends TablePanel<CUJO> {

    public AbstractTable(CQuery<CUJO> query) {
        super(query);
    }

    public AbstractTable() {
        super.pageSize = 100;
    }

    /** No table title */
    @Override
    protected String getTableTitle() {
        if (isSelectMode()) {
            return null;
        } else {
            return null;
        }
    }

    @Override
    public void redirectToLogin() {
        UIManager.getInstance().redirectToLogin();
    }

    /** Get object redirectable to login */
    protected CLoginRedirectable redir() {
        return this;
    }

}
