/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.service.impl;

import com.inspectime.commons.WQuery;
import com.inspectime.commons.bo.Product;
import com.inspectime.commons.bo.Project;
import com.inspectime.service.def.ProjectService;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.transaction.annotation.Transactional;
import org.ujorm.criterion.Criterion;
import org.ujorm.implementation.orm.OrmTable;

/**
 *
 * @author Hampl
 */
@Transactional
@org.springframework.stereotype.Service("projectService")
public class ProjectServiceImpl extends AbstractServiceImpl<Project> implements ProjectService {

    static final private Logger LOGGER = Logger.getLogger(ProjectServiceImpl.class.getName());

    @Override
    public Class<Project> getDefaultClass() {
        return Project.class;
    }

    private void setModified(Project bo, boolean created) {
        Date currentDate = new Date();

        if (created) {
            bo.set(Project.created, currentDate);
            if (bo.get(Project.createdBy)==null) {
                bo.set(Project.createdBy, getApplContext().getUser());
            }
            if (bo.get(Project.product)==null) {
                Product prod = getApplContext().getUser().getCompany().getFirstProduct();
                bo.set(Project.product, prod);
            }
        }
        bo.set(Project.modified, currentDate);
        if (bo.get(Project.modifiedBy)==null) {
            bo.set(Project.modifiedBy, getApplContext().getUser());
        }
    }

    @Override
    public void save(Project bo) {
        setModified(bo, true);
        if (bo.get(Project.product)==null) {
            bo.set(Project.product, getApplContext().getDefaultProduct());
        }
        super.save(bo);
    }

    @Override
    public void update(Project bo) {
        setModified(bo, false);
        super.update(bo);
    }

    @Override
    public <UJO extends OrmTable> List<UJO> list(WQuery query) {
        Criterion crn = Criterion.where(Project._company, getApplContext().getUserCompany());
        query.addCriterion(crn);
        return super.list(query);
    }

}
