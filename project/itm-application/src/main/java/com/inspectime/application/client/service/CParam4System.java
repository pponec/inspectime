/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.application.client.service;

import com.inspectime.application.client.cbo.CSingleComParam;
import com.inspectime.service.def.ParamCompService;
import org.ujorm.gxt.client.CujoProperty;
import org.ujorm.gxt.client.CujoPropertyList;
import java.io.Serializable;
import java.util.HashMap;
import org.ujorm.gxt.client.AbstractCujo;

/**
 * Singleton - the Parameters of the Company
 * @author meatfly, Ponec
 * @see ParamCompService
 **/
public class CParam4System extends CAbstractParam implements Serializable {

    private static final CujoPropertyList pl = list(CParam4System.class);
    /** Debug mode */
    public static final CujoProperty<CParam4System, Boolean> debug = pl.newPropertyDef("debug", false);
    /** Http link to a page of the Inspectime Terms of Use */
    public static final CujoProperty<CParam4System, String> termsOfUseLink = pl.newPropertyDef("termsOfUseLink", "");
    /** Instance */
    private static CParam4System instance;
    /** Parameter descriptions */
    private HashMap<String, String> descriptions = new HashMap<String, String>();

    private CParam4System() {
        initMessages();
    }

    public static CParam4System getInstance() {
        if (instance==null) {
            instance = new CParam4System();
        }
        return instance;
    }

    @Override
    public CujoPropertyList readProperties() {
        return pl;
    }

    private void initMessages() {
        for (CujoProperty p : readProperties()) {
            setDescription(p, p.getLabel());
        }
    }

    // -------------- GET PARAMETERS --------------

    /** Returns a company name */
    public boolean isDebug() {

        final CujoProperty property = debug;

        AbstractCujo p = map.get(property.getName());
        String result = p instanceof CSingleComParam
                ? ((CSingleComParam)p).getTextValue()
                : property.getDefault().toString();
        return "true".equals(result);
    }

    /** Returns a pomodoro interval */
    public String getTermsOfUseLink() {

        CujoProperty property = termsOfUseLink;

        AbstractCujo p = map.get(property.getName());
        String result = p instanceof CSingleComParam
                ? ((CSingleComParam)p).getTextValue()
                : (String) property.getDefault();
        return result;
    }

}
