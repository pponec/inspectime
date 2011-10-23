/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */


package com.inspectime.application.server.core;

import com.inspectime.application.client.cbo.*;
import com.inspectime.commons.bo.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.ujorm.gxt.client.ClientClassConfig;
import org.ujorm.gxt.client.Cujo;
import org.ujorm.gxt.server.AbstractServerClassConfig;
import org.ujorm.gxt.server.UjoTranslator;
import org.ujorm.orm.OrmHandler;
import org.ujorm.orm.OrmUjo;

/**
 * Inicialization the CUJO objects + mapping to BO.
 * Scope is an Singleton.
 * @author Pavel Ponec
 */
public class ServerClassConfig extends AbstractServerClassConfig {

    @Autowired
    protected OrmHandler handler;
    private static ServerClassConfig serverConfig;
    final private Map<String, Class<? extends OrmUjo>> classMap;

    /** Initializa all BOs */
    public ServerClassConfig() {
        ClientClassConfig.getInstance();
        classMap = new HashMap<String, Class<? extends OrmUjo>>();
        //
        init(new CUser(), User.class);
        init(new CUserRole(), UserRole.class);
        init(new CUserGroup(), UserGroup.class);
        init(new CCompany(), Company.class);
        init(new CProduct(), Product.class);
        init(new CProject(), Project.class);
        init(new CCustomer(), Customer.class);
        init(new CRelProjUsGroup(), RelProjectUsGroup.class);
        init(new CSingleComParam(), SingleComParam.class);
        init(new CSingleUsrParam(), SingleUsrParam.class);
        init(new CTaskAction(), TaskAction.class);
        init(new CEvent(), Event.class);
        init(new CUserTask(), UserTask.class);
        init(new CRelease(), Release.class);
        init(new CTask(), Task.class);
        init(new CAccount(), Account.class);
        init(new CEventLock(), EventLock.class);
        
        // Use it for more client classes ...
    }

    private void init(Cujo cujo, Class<? extends OrmUjo> type) {
        classMap.put(cujo.getClass().getName(), type);
    }
    
    /** Convert CType to UjoClass */
    @Override
    public Class<? extends OrmUjo> getServerClass(String ctype) throws IllegalStateException {
        Class<? extends OrmUjo> result = getServerClassOrNull(ctype);
        if (getServerClassOrNull(ctype)==null) {
            throw new IllegalStateException("No server class was found for: " + ctype);
        }
        return result;
    }

    @Override
     public Class<? extends OrmUjo> getServerClassOrNull(String ctype) throws IllegalStateException {
        Class<? extends OrmUjo> result = classMap.get(ctype);

        return result;
    }

    /** Convert CType to UjoClass */
    @Override
    public OrmUjo newServerObject(String ctype) throws IllegalStateException {
        try {
            return getServerClass(ctype).newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("No server instance was created for: " + ctype, e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public UjoTranslator getTranslator(Class ctype, int relations) {
        try {
            Class serverClass = getServerClass(ctype.getName());
            UjoTranslator result = new UjoTranslator(ctype, serverClass, relations, this);
            return result;
        } catch (Exception e) {
            throw new IllegalStateException("New translator excepton", e);
        }
    }

    /** Return all client object type instances */
    @Override
    public List<Cujo> getClientObjectList() throws IllegalStateException {
        List<Cujo> result = new ArrayList<Cujo>();
        String _cujoType = null;
        try {
            for (String cujoType : classMap.keySet()) {
                _cujoType = cujoType;
                Class c = Class.forName(cujoType);
                Cujo cujo = (Cujo) c.newInstance();
                result.add(cujo);
            }

        } catch (Throwable e) {
            throw new IllegalStateException("Can't create an instance for type: " + _cujoType, e);
        }

        return result;
    }

    /** Returns the OrmHandler */
    @Override
    public OrmHandler getHandler() {
        return handler;
    }

    /** Get the Singleton instance */
    public static ServerClassConfig getInstance(OrmHandler handler) {
        if (serverConfig==null) {
            serverConfig = new ServerClassConfig();
            serverConfig.handler = handler;
        }
        return serverConfig;
    }

}
