/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.service.impl;

import com.inspectime.commons.WQuery;
import com.inspectime.commons.bo.AbstractBo;
import com.inspectime.commons.bo.Release;
import com.inspectime.service.def.ReleaseService;
import java.util.Date;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.ujorm.Ujo;
import org.ujorm.UjoProperty;
import org.ujorm.core.UjoIterator;
import org.ujorm.criterion.Criterion;
import org.ujorm.criterion.Operator;
import org.ujorm.implementation.orm.OrmTable;
import org.ujorm.implementation.orm.RelationToMany;

/**
 *
 * @author Ponec
 */
@Transactional
@org.springframework.stereotype.Service("releaseService")
public class ReleaseServiceImpl extends AbstractServiceImpl<Release> implements ReleaseService {

    @Override
    public Class<Release> getDefaultClass() {
        return Release.class;
    }

    @Override
    public UjoIterator<Release> loadReleasesForWFStart() {
        Date now = new Date();

        Criterion<Release> c1 = Criterion.where(Release.wfStarted, false);
        Criterion<Release> c2 = Criterion.where(Release.releaseDate, Operator.LT, now);

        UjoIterator<Release> result = getSession().createQuery(c1.and(c2)).iterator();
        return result;

    }

    private void copyProperties(Ujo oldB, Ujo newB) {
        for (UjoProperty property : oldB.readProperties()) {
            if (!property.isTypeOf(RelationToMany.class)) {
                if (!AbstractBo.$ID.equals(property.getName())) {

                    for (UjoProperty proprty2 : newB.readProperties()) {
                        if (property.getName().equals(proprty2.getName())) {
                            newB.writeValue(proprty2, oldB.readValue(property));
                            break;
                        }
                    }
                }
            }
        }

    }

    @Override
    public <UJO extends OrmTable> List<UJO> list(WQuery query) {
        Criterion crn = Criterion.where(Release._company, getApplContext().getUserCompany());
        query.addCriterion(crn);
        return super.list(query);
    }


}
