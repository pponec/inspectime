/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.service.impl;

import com.inspectime.commons.WQuery;
import com.inspectime.commons.bo.Company;
import com.inspectime.commons.bo.Product;
import com.inspectime.commons.bo.User;
import com.inspectime.service.def.ProductService;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.transaction.annotation.Transactional;
import org.ujorm.criterion.Criterion;
import org.ujorm.implementation.orm.OrmTable;

/**
 *
 * @author Hampl
 */
@Transactional
@org.springframework.stereotype.Service("productService")
public class ProductServiceImpl extends AbstractServiceImpl<Product> implements ProductService {

    static final private Logger LOGGER = Logger.getLogger(ProductServiceImpl.class.getName());

    @Override
    public Class<Product> getDefaultClass() {
        return Product.class;
    }

    private void setModified(Product bo, boolean created) {
        Date currentDate = new Date();

        if (created) {
            bo.set(Product.created, currentDate);
            if (bo.get(Product.createdBy) == null) {
                bo.set(Product.createdBy, getApplContext().getUser());
            }
        }
        bo.set(Product.modified, currentDate);
        bo.set(Product.modifiedBy, getApplContext().getUser());

    }

    @Override
    public void save(Product bo) {
        setModified(bo, true);
        if (bo.get(Product.company) == null) {
            bo.set(Product.company, getApplContext().getUser().getCompany());
        }
        super.save(bo);
    }

    @Override
    public void update(Product bo) {
        setModified(bo, false);
        super.update(bo);
    }

    /** Find or create a first product of the current user company. */
    @Override
    public Product getFirstProduct() {
        return getFirstProduct(getApplContext().getUser());
    }

    /** Find or create a first product of the current user company. */
    @Override
    public Product getFirstProduct(User user) {
        Company company = user != null ? user.getCompany() : getApplContext().getUserCompany();
        Product firstProduct = getSession().createQuery(Product.class).setLimit(1).orderBy(Product.id).uniqueResult();
        if (firstProduct == null) {
            Date date = new Date();
            firstProduct = new Product();
            firstProduct.set(Product.company, company);
            firstProduct.set(Product.name, "-");
            firstProduct.set(Product.createdBy, user);
            firstProduct.set(Product.modifiedBy, user);
            firstProduct.set(Product.created, date);
            firstProduct.set(Product.modified, date);
            getSession().save(firstProduct);
        }
        return firstProduct;
    }

    @Override
    public <UJO extends OrmTable> List<UJO> list(WQuery query) {
        Criterion crn = Criterion.where(Product.company, getApplContext().getUserCompany());
        query.addCriterion(crn);
        return super.list(query);
    }

    /** Returns true if no product (include deleted) is found for the current company  */
    @Override
    public boolean isNoData() {
        Company company = super.getApplContext().getUserCompany();
        Criterion<Product> crn = Criterion.where(Product.company, company);
        Product product = getSession().createQuery(crn).setLimit(1).uniqueResult();
        return product == null;
    }
}
