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
import org.ujorm.implementation.orm.OrmTable;
import org.ujorm.orm.Query;

/**
 * Common service
 * @author Ponec
 */
public interface CommonService  {

    public void save(AbstractBo bo);

    public void update(AbstractBo bo);

    public void saveOrUpdate(AbstractBo bo);

    public void delete(AbstractBo bo);

    public AbstractBo loadById(Class type, Object id);

    /** Get all rows with no optimization */
    public <UJO extends OrmTable> UjoIterator<UJO> iterate(Query cquery);

    /** Get all rows with an optimization lazy loading */
    public <UJO extends OrmTable> List<UJO> list(WQuery cquery);

    /** Returns a default session */
    public org.ujorm.orm.Session getSession();


   
}
