/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.service.impl;

import com.inspectime.commons.WQuery;
import java.util.Map;
import org.ujorm.Key;
import com.inspectime.commons.bo.SingleComParam;
import org.ujorm.gxt.client.ao.ValidationMessage;
import com.inspectime.service.def.ParamCompService;
import com.inspectime.service.def.SingleComParamService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.ujorm.core.UjoManager;
import org.ujorm.criterion.Criterion;
import org.ujorm.swing.SingleUjoTabModel;
import org.ujorm.swing.UjoPropertyRow;
import org.ujorm.implementation.orm.OrmTable;

/**
 *
 * @author Hampl, Ponec
 */
@Transactional
@org.springframework.stereotype.Service(SingleComParamServiceImpl.BEAN_NAME)
public class SingleComParamServiceImpl extends AbstractServiceImpl<SingleComParam> implements SingleComParamService {

    /** Bean Name */
    public static final String BEAN_NAME = "singleComParamService";

    static final private Logger LOGGER = Logger.getLogger(SingleComParamServiceImpl.class.getName());
    @Autowired
    private ParamCompService appParamService;
    /** UjoManager */
    private UjoManager ujoManager = UjoManager.getInstance();

    @Override
    public Class<SingleComParam> getDefaultClass() {
        return SingleComParam.class;
    }

    @Override
    public <UJO extends OrmTable> List<UJO> list(WQuery query) {
        List<SingleComParam> result = new ArrayList<SingleComParam>(appParamService.readProperties().size());

        Criterion<SingleComParam> crn = Criterion.where(SingleComParam.company, this.appParamService.getUserCompany());
        Map<String, SingleComParam> map = new HashMap<String, SingleComParam>();

        for (SingleComParam parameter : getSession().createQuery(crn)) {
            map.put(parameter.getKey(), parameter);
        }


        SingleUjoTabModel model = new SingleUjoTabModel(appParamService, appParamService.readProperties().get(0));
        int i = 0;
        for (UjoPropertyRow param : model) {
            SingleComParam dbPar = map.get(param.get(UjoPropertyRow.P_NAME));
            SingleComParam par = new SingleComParam();
            result.add(par);
            //
            par.set(SingleComParam.defaultValue, param.getText(UjoPropertyRow.P_DEFAULT));
            par.set(SingleComParam.value, dbPar!=null ? dbPar.getValue() : param.getText(UjoPropertyRow.P_DEFAULT));
            par.set(SingleComParam.index, ++i /*param.get(UjoPropertyRow.P_INDEX)*/);
            par.set(SingleComParam.key, param.get(UjoPropertyRow.P_NAME));
            par.set(SingleComParam.type, param.get(UjoPropertyRow.P_TYPENAME));
        }

        return (List<UJO>) result;
    }


    @Override
    public void save(SingleComParam bo) {
        update(bo);
    }

    @Override
    public void update(SingleComParam bo) {
        Key p = appParamService.readProperties().find(bo.getKey(), true);
        appParamService.setText(p, bo.getValue());
    }

    @Override
    public ValidationMessage validate(SingleComParam parameter, boolean create) {
        // throw new UnsupportedOperationException("Not supported yet.");
        return null;
    }

    @Override
    public List<SingleComParam> list() {
        return list(null);
    }
}

