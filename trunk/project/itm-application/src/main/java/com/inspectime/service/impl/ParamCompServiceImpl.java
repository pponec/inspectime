/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.service.impl;

import com.inspectime.commons.bo.Company;
import com.inspectime.commons.bo.SingleComParam;
import com.inspectime.service.def.ParamCompService;
import com.inspectime.service.def.ApplContextService;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.ujorm.Ujo;
import org.ujorm.Key;
import org.ujorm.KeyList;
import org.ujorm.UjoPropertyList;
import org.ujorm.core.UjoManager;
import org.ujorm.criterion.Criterion;
import org.ujorm.implementation.map.MapUjo;
import org.ujorm.orm.Session;
import org.ujorm.orm.support.UjoSessionFactory;

/**
 * Services for reading and writting parameters
 * @author Ponec
 */
@Transactional
@org.springframework.stereotype.Service("appParamService")
public class ParamCompServiceImpl extends MapUjo implements ParamCompService, BeanFactoryAware {

    static final private Logger LOGGER = Logger.getLogger(ParamCompServiceImpl.class.getName());
    static final private UjoPropertyList propertyList = UjoManager.getInstance().readProperties(ParamCompService.class);
    @Autowired
    private UjoSessionFactory ujoSessionFactory;
    /** Spring bean factory */
    protected BeanFactory beanFactory;
    //temporary property do not use it
    private String v;



    public Session getSession() {
        return ujoSessionFactory.getDefaultSession();
    }

    /** Make validation and encode an object into String */
    protected String validateAndEncode(Object value, Key property) {
        String result;

//        // TODO:
//        boolean makeValidation = true;
//        if (makeValidation) {
//            if (ParamCompService.par1==property) {
//                throw new IllegalArgumentException("error message");
//            }
//        }

        result = readUjoManager().encodeValue(value, false);
        return result;
    }

    /** Load parameter from DB */
    private SingleComParam loadParam(Key property) {
        return loadParam(property, getUserCompany());
    }

    /** Load parameter from DB */
    private SingleComParam loadParam(Key property, Company company) {

        Criterion<SingleComParam> crn1 = Criterion.where(SingleComParam.company, company);
        Criterion<SingleComParam> crn2 = Criterion.where(SingleComParam.key, property.getName());

        SingleComParam param = getSession().createQuery(crn1.and(crn2)).uniqueResult();

        return param;
    }

    @Override
    public Object readValue(Key property) {
        return readValue(property, getUserCompany());
    }

    public Object readValue(Key property, Company company) {
        try {
            SingleComParam param = loadParam(property, company);
            Object result = (param != null)
                    ? readUjoManager().decodeValue(property, param.getValue())
                    : property.getDefault();
            return result;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Can't read property: " + property, e);
            return property.getDefault();
        }
    }

    @Override
    public void writeValue(Key property, Object value) {
        writeValue(property, value, getUserCompany());
    }

    public void writeValue(Key property, Object value, Company company) {
        SingleComParam param = loadParam(property);
        if (param == null) {
            param = new SingleComParam();
            param.setKey(property.getName());
        }

        String textValue = validateAndEncode(value, property);
        param.setValue(textValue);
        param.set(SingleComParam.company, company);

        getSession().saveOrUpdate(param);
    }

    @Override
    public KeyList readKeys() {
        return propertyList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <UJO extends ParamCompService, VALUE> VALUE get(Key<UJO, VALUE> property) {
        return property.getValue((UJO) this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <UJO extends ParamCompService, VALUE> Ujo set(Key<UJO, VALUE> property, VALUE value) {
        property.setValue((UJO) this, value);
        return this;
    }

    @Override
    public String getText(Key property) {
        return readUjoManager().getText(this, property, null);
    }

    @Override
    public void setText(Key property, String value) {
        readUjoManager().setText(this, property, value, null, null);
    }

    public void setAppVersion(String _version) {
        //set version string from spring
        v = _version;
    }

   /**
     * Set the beanFactory.
     * @see http://static.springsource.org/spring/docs/2.0.x/reference/beans.html#beans-factory-method-injection
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    /** Get ApplContextService for the current company */
    protected ApplContextService getApplContext() {
        return (ApplContextService) beanFactory.getBean(ApplContextServiceImpl.BEAN_NAME);
    }

    /** Return company of the logged company. */
    @Override
    public Company getUserCompany() {

        Company result;
        try {
            result = getApplContext().getUserCompany();
        } catch (Throwable e) {
            result = new Company();
            result.set(Company.id, 1L);
        }

        return result;
    }

    /** Get parameter value. */
    @Override
    public <T> T getValue(Key<ParamCompService,T> property, Company company) {
        return (T) readValue(property, company);
    }

    /** Set parameter value. */
    @Override
    public <T> void setValue(Key<ParamCompService,T> property, T value, Company company) {
        writeValue(property, value, company);
    }


}
