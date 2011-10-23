/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.service;

import com.inspectime.application.client.cbo.CSingleComParam;
import org.ujorm.gxt.client.AbstractCujo;
import org.ujorm.gxt.client.CujoProperty;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Singleton - the Abstract Parameters of the Company
 * @author Ponec
 **/
abstract public class CAbstractParam extends AbstractCujo implements Serializable {

    /** Local Static Data Store */
    protected HashMap<String, AbstractCujo> map = new HashMap<String, AbstractCujo>();

    /** Parameter descriptions */
    protected HashMap<String, String> descriptions = new HashMap<String, String>();


    @Override
    public <X> X get(String property) {
        throw new UnsupportedOperationException("Use an implemented getter instead of.");
    }

    @Override
    public <X> X get(String property, X valueWhenNull) {
        throw new UnsupportedOperationException("Use an implemented getter instead of.");
    }

    protected void setDescription(CujoProperty<? extends CAbstractParam, ?> property, String description) {
        descriptions.put(property.getName(), description);
    }

    /** Get Description by key */
    public String getDescription(String key) {
        return descriptions.get(key);
    }

    public AbstractCujo getParameter(CujoProperty property) {
        return map.get(property.getName());
    }

    public void setParameter(CujoProperty property, CSingleComParam param) {
        map.put(property.getName(), param);
    }

    /** Bulk save or modify a parameter list */
    public void saveAllParameters(List<? extends AbstractCujo> params) {
        for (AbstractCujo param : params) {
            String parameterKey = param.get(CSingleComParam.key.getName());
            map.put(parameterKey, param);
        }
    }

    /** Returns true if parametes are initializad. */
    public boolean isEmpty() {
        return map.isEmpty();
    }

}
