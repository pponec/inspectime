/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.service.impl;

import org.ujorm.gxt.client.ao.MessageSuffix;
import org.ujorm.gxt.client.tools.Tools;
import com.inspectime.application.server.core.SessionContext;
import com.inspectime.service.def.MessageService;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.transaction.annotation.Transactional;
import org.ujorm.UjoProperty;
import org.ujorm.orm.OrmHandler;
import org.ujorm.orm.metaModel.MetaRelation2Many;

/**
 * Scope session
 * @author Ponec
 */
@Transactional
@Scope("session")
@org.springframework.stereotype.Service(MessageServiceImpl.BEAN_NAME)
public class MessageServiceImpl implements MessageService {

    public static final String BEAN_NAME = "messageService";

    private static final Logger LOGGER = Logger.getLogger(MessageServiceImpl.class.getName());
    @Autowired
    private SessionContext sessionContext;
    @Autowired
    private OrmHandler ormHandler;
    private ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();

    public void setBaseName(String baseName) {
        messageSource.setBasename(baseName);
    }

    /** Set the session context */
    public void setSessionContext(SessionContext sessionContext) {
        this.sessionContext = sessionContext;
    }

    @Override
    public String get(String key) {
        final String result = getNullable(key, null, sessionContext.getUserLocale());
        return result != null ? result : key;
    }

    @Override
    public String get(String key, Object... args) {
        final String result = getNullable(key, args, sessionContext.getUserLocale());
        return result != null ? result : key;
    }

    @Override
    public String getNullable(String key, Object... args) {
        String result;
        try {
            result = messageSource.getMessage(key, args, sessionContext.getUserLocale());
            return result;
        } catch (NoSuchMessageException e) {
            result = null;
        }
        return result;
    }

    @Override
    public String getSideLabel(UjoProperty property) {
        return get(property, MessageSuffix.SIDE_LABEL);
    }

    @Override
    public String getColumnLabel(UjoProperty property) {
        return get(property, MessageSuffix.COLUMN);
    }

    @Override
    public String getDescription(UjoProperty property) {
        return get(property, MessageSuffix.DESCR);
    }

    @Override
    @SuppressWarnings("fallthrough")
    public String get(UjoProperty property, MessageSuffix suffix) {
        if (suffix == null) {
            suffix = MessageSuffix.SIDE_LABEL;

        }
        MetaRelation2Many column = ormHandler.findColumnModel(property);
        if (column == null) {
            return property.getName();
        }

        StringBuilder key = new StringBuilder(32);
        key.append("bo.");
        key.append(column.getTableClass().getSimpleName());
        key.append('.');
        key.append(property);

        switch (suffix) {
            case COLUMN:
            case DESCR:
                key.append('.');
                key.append(suffix.name());
            case SIDE_LABEL:
                break;
        }
        String result = getNullable(key.toString());

        if (result == null) {
            switch (suffix) {
                case DESCR:
                    result = "";
                    break;
                default:
                    result = Tools.getLabel(property.getName());
            }
        }
        return result;
    }
}
