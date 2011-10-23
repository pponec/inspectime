/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.service.def;

import org.ujorm.gxt.client.ao.ValidationMessage;
import com.inspectime.commons.bo.SingleUsrParam;
import java.util.List;

/**
 * ParamOfCompany service
 * @author Ponec
 */
public interface SingleUsrParamService extends AbstractService<SingleUsrParam> {

    /** A parameter modify validation */
    public ValidationMessage validate(SingleUsrParam parameter, boolean create);

    /** Return parameters for the logged user */
    public List<SingleUsrParam> list();

}
