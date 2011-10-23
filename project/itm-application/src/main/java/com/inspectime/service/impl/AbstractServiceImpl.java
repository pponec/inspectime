/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.service.impl;

import com.inspectime.application.server.core.ServerClassConfig;
import com.inspectime.commons.WQuery;
import com.inspectime.commons.bo.AbstractBo;
import com.inspectime.service.def.ApplContextService;
import com.inspectime.service.def.CommonService;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.ujorm.UjoProperty;
import org.ujorm.core.UjoIterator;
import org.ujorm.criterion.Criterion;
import org.ujorm.implementation.orm.OrmTable;
import org.ujorm.orm.Query;
import org.ujorm.orm.Session;
import org.ujorm.orm.support.UjoSessionFactory;
import com.inspectime.commons.bo.Company;
import com.inspectime.service.def.AbstractService;
import java.util.List;
import org.ujorm.extensions.Property;
import org.ujorm.gxt.client.CMessageException;

/**
 * predek sluzeb
 * @author Hampl
 */
abstract public class AbstractServiceImpl<BO extends AbstractBo> implements AbstractService<BO>, BeanFactoryAware {

    private static final Logger LOGGER = Logger.getLogger(AbstractServiceImpl.class.getName());

    @Autowired
    protected CommonService commonService;
    @Autowired
    private UjoSessionFactory ujoSessionFactory;
    @Autowired
    private ServerClassConfig serverClassConfig;

    /** Spring bean factory */
    protected BeanFactory beanFactory;
    /** Ujo Company filter */
    protected UjoProperty<BO,Company> PROPERTY_COMPANY_UNDEFINED = Property.newInstance("UNDEFINED", Company.class);
    /** Ujo Company filter */
    protected UjoProperty<BO,Company> propertyCompany = PROPERTY_COMPANY_UNDEFINED;


    public UjoSessionFactory getUjoSessionFactory() {
        return ujoSessionFactory;
    }   

    /** Returns the default session */
    public Session getSession() {
        return getUjoSessionFactory().getDefaultSession();
    }

    /** Returns a default class for the company */
    @Override
    abstract public Class<BO> getDefaultClass();

    /** Returns a relation to a user company. */
    protected UjoProperty<BO,Company> getCompanyProperty() {
        if (propertyCompany==PROPERTY_COMPANY_UNDEFINED) {
            synchronized (this) {
                if (propertyCompany==PROPERTY_COMPANY_UNDEFINED) try {
                    BO bo = getDefaultClass().newInstance();
                    for (UjoProperty p : bo.readProperties()) {
                        if (AbstractBo.$COMPANY==p.getName()) {
                           propertyCompany = p;
                           return propertyCompany;
                        }
                        propertyCompany = null;
                    }
                } catch (Throwable ex) {
                    LOGGER.log(Level.SEVERE, "Can't get instance of the " +  getDefaultClass(), ex);
                    propertyCompany = null;
                }
            }
        }
        return propertyCompany;
    }

    /** Returns company Criterion */
    protected Criterion<BO> getCompanyCriterion() {
        UjoProperty<BO,Company> p = getCompanyProperty();
        if (p!=null) {
            Company company = getApplContext().getUserCompany();
            Criterion<BO> result = Criterion.where(p, company);
            return result;
        } else {
            return null;
        }
    }

    @Override
    public void save(BO bo) {
        commonService.save(bo);
    }

    @Override
    public void update(BO bo) {
        commonService.update(bo);
    }

    @Override
    public void saveOrUpdate(BO bo) {
        // commonService.saveOrUpdate(bo);

        boolean create = bo.readSession()==null;

        if (create) {
            save(bo);
        } else {
            update(bo);
        }
    }

    @Override
    public void delete(BO bo) {
        commonService.delete(bo);
    }

    @SuppressWarnings("unchecked")
    @Override
    public BO loadById(Object id) {
        return (BO) commonService.loadById(getDefaultClass(), id);
    }

    @Override
    public UjoIterator<BO> load(Criterion<BO> criterion) {
        return getSession().createQuery(getDefaultClass(), criterion).iterator();
    }

    /** Get All */
    @Override
    public <UJO extends OrmTable> UjoIterator<UJO> iterate(Query query) {
        return commonService.iterate(query);
    }

    /** Get All */
    @Override
    public <UJO extends OrmTable> List<UJO> list(WQuery query) {
        return commonService.list(query);
    }


   /**
     * Set the beanFactory.
     * @see http://static.springsource.org/spring/docs/2.0.x/reference/beans.html#beans-factory-method-injection
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    /** Get ApplContextService for the current user */
    protected ApplContextService getApplContext() {
         return (ApplContextService) beanFactory.getBean(ApplContextServiceImpl.BEAN_NAME);
    }

    /** Returns true if parameter is not NULL and no-empty. */
    protected boolean isValid(CharSequence text) {
        return text!=null && text.length()>0;
    }

    /** assertServerContext */
    @Override
    public void assertServerContext() throws CMessageException {

        boolean logged = false;
        try {
            logged = getApplContext().isLogged();
        } catch (Exception e) {
            logged = false;
        }

        if (!logged) {
            final String msg = "Login timeout";
            CMessageException exeption = new CMessageException(CMessageException.KEY_SESSION_TIMEOUT, msg);
            LOGGER.log(Level.WARNING, msg, exeption);
            throw exeption;
        }
    }

}
