/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2010-2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@ujorm.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.service.impl;

import com.inspectime.commons.WQuery;
import org.ujorm.gxt.client.ao.ValidationException;
import org.ujorm.gxt.client.ao.ValidationMessage;
import com.inspectime.service.def.CommonService;
import com.inspectime.commons.bo.AbstractBo;
import com.inspectime.commons.bo.User;
import com.inspectime.service.def.UserService;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.ujorm.core.UjoIterator;
import org.ujorm.gxt.server.IServerClassConfig;
import org.ujorm.implementation.orm.OrmTable;
import org.ujorm.orm.Query;
import org.ujorm.orm.Session;
import org.ujorm.orm.utility.OrmTools;
import org.ujorm.spring.UjormTransactionManager;


/**
 *
 * @author ponec
 */
@Transactional
@org.springframework.stereotype.Service("commonService")
public class CommonServiceImpl implements CommonService {

    private static final Logger LOGGER = Logger.getLogger(CommonServiceImpl.class.getName());

    @Autowired
    protected UjormTransactionManager ujoSessionFactory;
    @Autowired
    protected UserService userService;
    @Autowired
    protected IServerClassConfig serverClassConfig;

    public UjormTransactionManager getUjoSessionFactory() {
        return ujoSessionFactory;
    }

    /** Returns a default session */
    @Override
    public Session getSession() {
        return getUjoSessionFactory().getLocalSession();
    }

    @Override
    public void save(AbstractBo bo) {
        getSession().save(bo);
    }

    @Override
    public void update(AbstractBo bo) {
        getSession().update(bo);
    }

    @Override
    public void saveOrUpdate(AbstractBo bo) {
        boolean create = bo.readSession()==null;
        ValidationMessage msg = null;

        // Validations:
        if (bo instanceof User) {
             msg = userService.validate((User)bo, create);
        }
        if (msg!=null) {
            // getSession().rollback();
            throw new ValidationException(msg);
        }

        // Save:
        getSession().saveOrUpdate(bo);

        // After save:
        if (bo instanceof User) {
             userService.createRoles((User) bo, create);
        }
    }

    @Override
    public void delete(AbstractBo bo) {
        getSession().delete(bo);
    }

    @Override
    @SuppressWarnings("unchecked")
    public AbstractBo loadById(Class type, Object id) {
        return (AbstractBo) getSession().load(type, id);
    }

    /** Get all items.
     * @param cfg provides the limit & offset or the objecty can be NULL.
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public <UJO extends OrmTable> UjoIterator<UJO> iterate(Query query) {

        try {
            query.setSession(getSession());
            UjoIterator<UJO> result = query.iterator();
            return result;
        } catch (Throwable e) {
            String msg = "No iterator";
            LOGGER.log(Level.SEVERE, msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    /** Get all items with optimization lazy loading
     * @param cfg provides the limit & offset or the objecty can be NULL.
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public <UJO extends OrmTable> List<UJO> list(WQuery query) {

        try {
            query.getQuery().setSession(getSession());

            if (query.isLoadRelations()) {
                List result = OrmTools.loadLazyValuesAsBatch(query.getQuery());
                return result;
            } else {
                List<UJO> result = query.getQuery().iterator().toList();
                return result;
            }
        } catch (Throwable e) {
            String msg = "No iterator";
            LOGGER.log(Level.SEVERE, msg, e);
            throw new RuntimeException(msg, e);
        }
    }

}


