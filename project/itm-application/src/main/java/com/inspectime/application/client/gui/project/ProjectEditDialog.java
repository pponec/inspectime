/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.gui.project;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import com.google.gwt.user.client.Element;
import com.inspectime.application.client.cbo.CCompany;
import com.inspectime.application.client.cbo.CProduct;
import com.inspectime.application.client.cbo.CProject;
import com.inspectime.application.client.clientTools.AbstractEditDialog;
import com.inspectime.application.client.gui.customer.CustomerTable;
import com.inspectime.application.client.gui.product.ProductTable;
import com.inspectime.application.client.service.CParam4Company;

/**
 * ProjectEditDialog
 * @author Ponec
 */
public class ProjectEditDialog<CUJO extends CProject> extends AbstractEditDialog<CUJO> {

    
    private CProduct product;
    private CCompany customer;

    @Override
    public CUJO createItem() {
        return (CUJO) new CProject();
    }

    /** Preselect product */
    public void setProduct(CProduct productFilter) {
        if (productFilter!=null) {
            this.product = productFilter;
            cujo.set(cujo.product, productFilter);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreateWidgets(Element parent, int pos) {
        //super.onRender(parent, pos);
        setClosable(true);
        setModal(true);
        setHeading(newState ? "New Project" : "Edit Project");
        setWidth(500);
        setHeight(350);
        setLayout(new FillLayout());

        panel = new FormPanel();
        panel.setHeaderVisible(false);
        panel.setFieldWidth(300);
        panel.setLabelWidth(120);
        panel.setLabelAlign(LabelAlign.RIGHT);
        panel.setButtonAlign(HorizontalAlignment.RIGHT);

        createWidget(CProject.name);
        createWidget(CProject.description);
        createWidgetRelation(CProject.product, new ProductTable());
        createWidgetRelation(CProject.customer, new CustomerTable());
        createWidget(CProject.completionDate);
        createWidget(CProject.finished);
        createWidget(CProject.graphColor);

        // Is the product Editable ?
        final boolean productEditable = CParam4Company.getInstance().isProductSupported();
        super.findWidget(CProject.product).setEnabled(productEditable);


        // if (product==null) createWidgetRelation(CProject.product, new ProductTable());

        // ------------------
        add(panel);

        final Button update = newOkButton(newState);
        panel.addButton(update);
        update.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                onSubmit();
            }
        });
        createQuitButton(panel);

    }

}
