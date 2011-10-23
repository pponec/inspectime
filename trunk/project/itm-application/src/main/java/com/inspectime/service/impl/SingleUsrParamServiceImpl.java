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
import org.ujorm.UjoProperty;
import org.ujorm.gxt.client.ao.ValidationMessage;
import com.inspectime.commons.bo.SingleUsrParam;
import com.inspectime.service.def.ParamUserService;
import com.inspectime.service.def.SingleUsrParamService;
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
@org.springframework.stereotype.Service("singleUsrParamService")
public class SingleUsrParamServiceImpl extends AbstractServiceImpl<SingleUsrParam> implements SingleUsrParamService {

    static final private Logger LOGGER = Logger.getLogger(SingleUsrParamServiceImpl.class.getName());
    @Autowired
    private ParamUserService appParamService;
    /** UjoManager */
    private UjoManager ujoManager = UjoManager.getInstance();

    @Override
    public Class<SingleUsrParam> getDefaultClass() {
        return SingleUsrParam.class;
    }

    /** Return parameters for the logged user
     * @param query The parameters is ignored
     * @param loadRelations The parameter is ignored
     * @return
     */
    @Override
    public <UJO extends OrmTable> List<UJO> list(final WQuery query) {
        List<SingleUsrParam> result = new ArrayList<SingleUsrParam>(appParamService.readProperties().size());

        Criterion<SingleUsrParam> crn = Criterion.where(SingleUsrParam.user, this.appParamService.getUser());
        Map<String, SingleUsrParam> map = new HashMap<String, SingleUsrParam>();

        for (SingleUsrParam parameter : getSession().createQuery(crn)) {
            map.put(parameter.getKey(), parameter);
        }


        SingleUjoTabModel model = new SingleUjoTabModel(appParamService, appParamService.readProperties().get(0));
        int i = 0;
        for (UjoPropertyRow param : model) {
            SingleUsrParam dbPar = map.get(param.get(UjoPropertyRow.P_NAME));
            SingleUsrParam par = new SingleUsrParam();
            result.add(par);
            //
            par.set(SingleUsrParam.defaultValue, param.getText(UjoPropertyRow.P_DEFAULT));
            par.set(SingleUsrParam.value, dbPar!=null ? dbPar.getValue() : param.getText(UjoPropertyRow.P_DEFAULT));
            par.set(SingleUsrParam.index, ++i /*param.get(UjoPropertyRow.P_INDEX)*/);
            par.set(SingleUsrParam.key, param.get(UjoPropertyRow.P_NAME));
            par.set(SingleUsrParam.type, param.get(UjoPropertyRow.P_TYPENAME));
        }

        return (List<UJO>) result;
    }


    @Override
    public void save(SingleUsrParam bo) {
        update(bo);
    }

    @Override
    public void update(SingleUsrParam bo) {
        UjoProperty p = appParamService.readProperties().find(bo.getKey(), true);
        appParamService.setText(p, bo.getValue());
    }

    @Override
    public ValidationMessage validate(SingleUsrParam parameter, boolean create) {
        // throw new UnsupportedOperationException("Not supported yet.");
        return null;
    }

    @Override
    public List<SingleUsrParam> list() {
        return list(null);
    }

}

