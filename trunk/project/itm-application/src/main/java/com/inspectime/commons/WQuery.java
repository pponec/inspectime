/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */


package com.inspectime.commons;

import java.util.List;
import org.ujorm.UjoProperty;
import org.ujorm.criterion.Criterion;
import org.ujorm.gxt.client.cquery.CQuery;
import org.ujorm.orm.Query;

/**
 * The Inspectime extended Query
 * @author Ponec
 */
final public class WQuery {

    private final Query query;
    private final boolean loadRelations;
    private final String context;

    public WQuery(Query query, CQuery cquery) {
        this(query, cquery.getRelations()>0, cquery.getContext());
    }

    public WQuery(Query query, boolean loadRelations, String context) {
        this.query = query;
        this.loadRelations = loadRelations;
        this.context = context;
    }

    public String getContext() {
        return context;
    }

    public boolean isLoadRelations() {
        return loadRelations;
    }

    public Query getQuery() {
        return query;
    }

    /** Is an context */
    public boolean isContext() {
        return context!=null && context.length()>0;
    }

    /** Add new criterion */
    public void addCriterion(Criterion criterion) {
        query.addCriterion(criterion);
    }

    /** Returns true if order is the same like parameter. */
    public boolean isOrderBy(UjoProperty ... properties) {
        List<UjoProperty> myProperties = query.getOrderBy();

        if (properties.length!=myProperties.size()) {
            return false;
        }

        for (int i = properties.length-1; i>=0; --i) {
            final UjoProperty p1 = properties[i];
            final UjoProperty p2 = myProperties.get(i);
            if (!p1.toString().equals(p2.toString())) {
                return false;
            }
        }
        return true;
    }

    /** Set new OrderBy */
    public void setOrderBy(UjoProperty ... properties) {
        query.orderByMany(properties);
    }

}
