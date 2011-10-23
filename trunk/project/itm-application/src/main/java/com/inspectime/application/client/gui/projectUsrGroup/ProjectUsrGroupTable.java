/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */


package com.inspectime.application.client.gui.projectUsrGroup;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.inspectime.application.client.cbo.CProject;
import com.inspectime.application.client.cbo.CRelProjUsGroup;
import com.inspectime.application.client.cbo.CUserGroup;
import com.inspectime.application.client.clientTools.AbstractEditDialog;
import org.ujorm.gxt.client.cquery.CCriterion;
import org.ujorm.gxt.client.cquery.CQuery;
import org.ujorm.gxt.client.gui.MultiField;
import com.inspectime.application.client.clientTools.AbstractTable;
import org.ujorm.gxt.client.gui.TableListDialog;
import com.inspectime.application.client.gui.userGroup.UserGroupTable;
import org.ujorm.gxt.client.CujoProperty;

/**
 *
 * @author Ponec
 */
public class ProjectUsrGroupTable<CUJO extends CRelProjUsGroup> extends AbstractTable<CUJO> {

    public ProjectUsrGroupTable(CQuery<CUJO> query) {
        super(query);
    }

    public ProjectUsrGroupTable() {
    }

    protected boolean isRelationFromUsrgroup() {
        boolean result = getAdvancedCrn() != null &&
            getAdvancedCrn().getLeftNode() == CRelProjUsGroup.userGroup;
        return result;
    }

    @Override
    protected Button addButton(String label, AbstractImagePrototype icon, LayoutContainer c) {
        Button result = super.addButton(label, icon, c);
        if (result!=null && label==buttonCreate) {
            result.setText("Add");
        }
        if (result!=null && label==buttonDelete) {
            result.setText("Remove");
        }
        if (result!=null && label==buttonUpdate) {
            result.setVisible(false);
        }

        return result;
    }



    /** TODO: Create a new edit dialog. */
    //@Override
    @SuppressWarnings("unchecked")
    protected AbstractEditDialog<CUJO> createAbstractEditDialog_NEW(final CUJO selectedItem) {
        boolean newState = selectedItem==null;
        // if (clone) copy(selectedItem, cujo);
        CRelProjUsGroup cujo = newState ? new CRelProjUsGroup() : selectedItem ;

        UserGroupTable groupTable = new UserGroupTable();

        MultiField multiField = new MultiField();
        TableListDialog editDialog = new TableListDialog(groupTable, multiField);

        CCriterion<CUJO> advancedCrn = getAdvancedCrn();

        if (advancedCrn != null &&
            advancedCrn.getLeftNode() == CRelProjUsGroup.project) {
            //editDialog.setProject((CProject) advancedCrn.getRightNode());
        }
        if (advancedCrn != null &&
            advancedCrn.getLeftNode() == CRelProjUsGroup.userGroup) {
            //editDialog.setUserGroup((CUserGroup)advancedCrn.getRightNode());
        }

        return null;//editDialog;
    }

    /** Create a new edit dialog. */
    @Override
    protected ProjectUsrGroupEditDialog createDialogInstance() {
        return new ProjectUsrGroupEditDialog();
    }
    /** Create a new edit dialog. */
    @Override
    @SuppressWarnings("unchecked")
    protected AbstractEditDialog<CUJO> createTableEditDialog(final CUJO selectedItem, boolean newState, boolean clone) {
        ProjectUsrGroupEditDialog dialog = createDialogInstance();
        CRelProjUsGroup cujo = newState ? dialog.createItem() : selectedItem ;
        if (clone) copy(selectedItem, cujo);
        dialog.init(cujo, newState, getEditQuery(newState, selectedItem));

        CCriterion<CUJO> advancedCrn = getAdvancedCrn();

        if (advancedCrn != null &&
            advancedCrn.getLeftNode() == CRelProjUsGroup.project) {
            dialog.setProject((CProject) advancedCrn.getRightNode());
        }
        if (advancedCrn != null &&
            advancedCrn.getLeftNode() == CRelProjUsGroup.userGroup) {
            dialog.setUserGroup((CUserGroup)advancedCrn.getRightNode());
        }

        return dialog;
    }

    /** Specify a list of the Table columns */
    @Override
    protected CujoProperty[] createTableColumns() {
        return new CujoProperty[]
        {
        };
    }

    /** Create a default database query. */
    @Override
    public CQuery<? super CUJO> createDefaultQuery() {
        CQuery<CRelProjUsGroup> result = CQuery.newInstance(CRelProjUsGroup.class, createTableColumns());
        CCriterion<CRelProjUsGroup> crn = null;

        result.setCriterion(crn);
        if (isRelationFromUsrgroup()) {
            result.orderBy(CRelProjUsGroup.project.add(CProject.name));
        } else {
            result.orderBy(CRelProjUsGroup.userGroup.add(CUserGroup.name));
        }
        return result;
    }


}
