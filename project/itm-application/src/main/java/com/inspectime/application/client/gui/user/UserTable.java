/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.gui.user;

import com.google.gwt.user.client.Element;
import com.inspectime.application.client.cbo.CUser;
import com.extjs.gxt.ui.client.store.ListStore;
import org.ujorm.gxt.client.cquery.CCriterion;
import org.ujorm.gxt.client.cquery.CQuery;
import com.inspectime.application.client.clientTools.AbstractTable;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.inspectime.application.client.ao.CTimeZone;
import com.inspectime.application.client.ao.CRoleEnum;
import org.ujorm.gxt.client.CujoProperty;
import org.ujorm.gxt.client.cquery.COperator;


/**
 * User Table
 * @author Ponec
 */
public class UserTable<CUJO extends CUser> extends AbstractTable<CUJO> {

    /** Supported EDITABLE_ROLES */
    public static final CRoleEnum[] EDITABLE_ROLES = { /*CRoleEnum.USER,*/ CRoleEnum.MANAGER, CRoleEnum.ADMIN };

    public UserTable(CQuery<CUJO> query) {
        super(query);
    }

    public UserTable() {
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        grid.setAutoExpandColumn(getQuery().getColumnConfig(CUser.description).getId());
        grid.setAutoExpandMax(Integer.MAX_VALUE);
    }



    /** Create table renderer */
    private GridCellRenderer<CUser> createTimeZoneRenderer() {
        return new GridCellRenderer<CUser>() {
            @Override
            public Object render(CUser user, String property, ColumnData config, int rowIndex, int colIndex, ListStore<CUser> store, Grid<CUser> grid) {
                if (CUser.timeZone.getName().equals(property)) {
                    String tz = (String) user.get(property);
                    return new CTimeZone(tz).toString();
                } else {
                    throw new IllegalStateException("Unsupported property : "  + property);
                }
            }
        };
    }

    /** Create a new edit dialog. */
    @Override
    protected UserEditDialog<CUJO> createDialogInstance() {
        return new UserEditDialog();
    }

    /** Specify a list of the Table columns */
    @Override
    protected CujoProperty[] createTableColumns() {
        return new CujoProperty[]
        { CUser.email
        , CUser.login
        , CUser.name
        , CUser.pid
        , CUser.description
      //, CUser.userGroup.add(CUserGroup.name)
        , CUser.workFundStafPerWeek
        , CUser.timeZone
        , CUser.enabled
      //, CUser.accountNonExpired      // Document it first ! UserEditDialog.SPRING_CREDENTIALS_SUPPORTED
      //, CUser.credentialsNonExpired  // Document it first !
      //, CUser.accountNonLocked       // Document it first !
        , CUser.roles
        };
    }

    @Override
    protected CQuery<? super CUJO> createDefaultQuery() {
        CQuery<CUser> result = CQuery.newInstance(CUser.class, createTableColumns());
        result.addOrderBy(CUser.enabled.descending());
        result.addOrderBy(CUser.pid);
        result.addOrderBy(CUser.login);
        result.setCriterion(CUser.active.whereEq(true).and(CUser.login.where(COperator.NOT_EQ, "admin")));

        // GUI:
        result.getColumnConfig(CUser.workFundStafPerWeek).setWidth(50);
        result.getColumnConfig(CUser.enabled).setWidth(50);
        result.getColumnConfig(CUser.name).setWidth(200);
        result.getColumnConfig(CUser.email).setWidth(200);
        result.getColumnConfig(CUser.roles).setWidth(50);
        result.getColumnConfig(CUser.timeZone).setRenderer(createTimeZoneRenderer());
        result.getColumnConfig(CUser.roles).setRenderer(new RoleRenderer(EDITABLE_ROLES));
        result.getColumnConfig(CUser.login).setHidden(true);
 //     result.getColumnConfig(CUser.userGroup.add(CUserGroup.name)).setHeader("User group");
        if (UserEditDialog.SPRING_CREDENTIALS_SUPPORTED) {
            result.getColumnConfig(CUser.accountNonExpired).setWidth(30);
            result.getColumnConfig(CUser.accountNonLocked).setWidth(30);
            result.getColumnConfig(CUser.credentialsNonExpired).setWidth(30);
        }
        
        return result;
    }
}
