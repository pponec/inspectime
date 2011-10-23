/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.service.impl;

import com.inspectime.commons.bo.SingleUsrParam;
import com.inspectime.commons.bo.User;
import com.inspectime.service.def.ParamUserService;
import com.inspectime.service.def.ApplContextService;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.ujorm.Ujo;
import org.ujorm.UjoProperty;
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
@org.springframework.stereotype.Service("appParamUserService")
public class ParamUserServiceImpl extends MapUjo implements ParamUserService, BeanFactoryAware {

    static final private Logger LOGGER = Logger.getLogger(ParamUserServiceImpl.class.getName());
    static final private UjoPropertyList propertyList = UjoManager.getInstance().readProperties(ParamUserService.class);
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
    protected String validateAndEncode(Object value, UjoProperty property) {
        String result;

//        // TODO:
//        boolean makeValidation = true;
//        if (makeValidation) {
//            if (AppParamCompService.par1==property) {
//                throw new IllegalArgumentException("error message");
//            }
//        }

        result = readUjoManager().encodeValue(value, false);
        return result;
    }

    /** Load parameter from DB */
    private SingleUsrParam loadParam(UjoProperty property) {
        return loadParam(property, getUser());
    }

    /** Load parameter from DB */
    private SingleUsrParam loadParam(UjoProperty property, User user) {

        Criterion<SingleUsrParam> crn1 = Criterion.where(SingleUsrParam.user, user);
        Criterion<SingleUsrParam> crn2 = Criterion.where(SingleUsrParam.key, property.getName());

        SingleUsrParam param = getSession().createQuery(crn1.and(crn2)).uniqueResult();

        return param;
    }

    /** Read default user parameter value */
    @Override
    public Object readValue(UjoProperty property) {
        return readValue(property, getUser());
    }

    /** Read a user parameter value */
    public Object readValue(UjoProperty property, User user) {
        try {
            SingleUsrParam param = loadParam(property, user);
            Object result = (param != null)
                    ? readUjoManager().decodeValue(property, param.getValue())
                    : property.getDefault();
            return result;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Can't read property: " + property, e);
            return property.getDefault();
        }
    }

    /** Write default user parameter value */
    @Override
    public void writeValue(UjoProperty property, Object value) {
        writeValue(property, value, getUser());
    }

    /** Write a user parameter value */
    public void writeValue(UjoProperty property, Object value, User user) {
        SingleUsrParam param = loadParam(property);
        if (param == null) {
            param = new SingleUsrParam();
            param.setKey(property.getName());
        }

        String textValue = validateAndEncode(value, property);
        param.setValue(textValue);
        param.set(SingleUsrParam.user, user);

        getSession().saveOrUpdate(param);
    }

    @Override
    public UjoPropertyList readProperties() {
        return propertyList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <UJO extends ParamUserService, VALUE> VALUE get(UjoProperty<UJO, VALUE> property) {
        return property.getValue((UJO) this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <UJO extends ParamUserService, VALUE> Ujo set(UjoProperty<UJO, VALUE> property, VALUE value) {
        property.setValue((UJO) this, value);
        return this;
    }

    @Override
    public String getText(UjoProperty property) {
        return readUjoManager().getText(this, property, null);
    }

    @Override
    public void setText(UjoProperty property, String value) {
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

    /** Get ApplContextService for the current user */
    protected ApplContextService getApplContext() {
        return (ApplContextService) beanFactory.getBean(ApplContextServiceImpl.BEAN_NAME);
    }

    /** Return user of the logged user. */
    @Override
    public User getUser() {

        User result;
        try {
            result = getApplContext().getUser();
        } catch (Throwable e) {
            result = new User();
            result.set(User.id, 1L);
        }

        return result;
    }

    /** Read a user parameter value */
    @Override
    public <T> T getValue(UjoProperty<ParamUserService, T> property, User company) {
        return (T) readValue(property, company);
    }

    /** Write a user parameter value */
    @Override
    public <T> void setValue(UjoProperty<ParamUserService, T> property, T value, User company) {
        writeValue(property, value, company);
    }

}
