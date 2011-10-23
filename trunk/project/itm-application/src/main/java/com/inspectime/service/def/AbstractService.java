/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.service.def;

import com.inspectime.commons.WQuery;
import com.inspectime.commons.bo.AbstractBo;
import java.util.List;
import org.ujorm.core.UjoIterator;
import org.ujorm.criterion.Criterion;
import org.ujorm.implementation.orm.OrmTable;
import org.ujorm.orm.Query;

/**
 *
 * @author Hampl
 */
public interface AbstractService<BO extends AbstractBo> {

    abstract public Class<BO> getDefaultClass();

    public void save(BO bo);

    public void update(BO bo);

    public void saveOrUpdate(BO bo);

    public void delete(BO bo);

    public BO loadById(Object id);

    public UjoIterator<BO> load(Criterion<BO> criterion);

    /** Get required items */
    public <UJO extends OrmTable> UjoIterator<UJO> iterate(Query query);

    /** Get required items with a lazy loading of the first level properties */
    public <UJO extends OrmTable> List<UJO> list(WQuery query);

    /** Assert Server Context */
    public void assertServerContext();

}
