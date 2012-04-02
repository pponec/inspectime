/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */


package com.inspectime.application.client.gui.company;

import com.inspectime.application.client.cbo.CCompany;
import com.inspectime.application.client.clientTools.AbstractEditDialog;
import com.inspectime.application.client.clientTools.AbstractTable;
import org.ujorm.gxt.client.CujoProperty;
import org.ujorm.gxt.client.cquery.CCriterion;
import org.ujorm.gxt.client.cquery.CQuery;

/**
 *
 * @author Ponec
 */
public class CompanyTable<CUJO extends CCompany> extends AbstractTable<CUJO> {

    public CompanyTable(CQuery<CUJO> query) {
        super(query);
    }

    public CompanyTable() {
    }

    /** Create a new edit dialog. */
    @Override
    @SuppressWarnings("unchecked")
    protected AbstractEditDialog<CUJO> createDialogInstance() {
        return new CompanyEditDialog();
    }

    /** Specify a list of the Table columns */
    @Override
    protected CujoProperty[] createTableColumns() {
        return new CujoProperty[]
        {
        };
    }

    @Override
    protected CQuery<? super CUJO> createDefaultQuery() {
        CQuery<CCompany> result = CQuery.newInstance(CCompany.class, createTableColumns());
        //
        result.addOrderBy(CCompany.id); // Default orderBy
        result.setCriterion(CCompany.active.whereEq(true));
        result.getColumnConfig(CCompany.active).setHidden(true);
        return result;
    }


}
