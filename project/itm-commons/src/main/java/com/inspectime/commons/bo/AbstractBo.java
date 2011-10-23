/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.commons.bo;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.ujorm.UjoProperty;
import org.ujorm.implementation.orm.OrmTable;

/**
 * The abstract persistent class
 * @author Ponec
 */
public abstract class AbstractBo extends OrmTable<AbstractBo> implements Comparable<AbstractBo> {

    static final private Logger LOGGER = Logger.getLogger(AbstractBo.class.getName());

    /** A name of ID property */
    public static final String $ID = new String("id");
    /** A name of COMPANY property to a generic Criterion */
    public static final String $COMPANY = new String("company");
    /** A name of property with the "NOT-DELETED" reason. */
    public static final String $ACTIVE = new String("active");
    /** A graph color property name */
    public static final String $GRAPH_COLOR = new String("graphColor");

    /** Returns the Primary Key. */
    abstract public Long getId();

    @Override
    public void writeValue(final UjoProperty p, final Object value) {
        if ($ACTIVE==p.getName() && Boolean.FALSE.equals(value)) {
            String msg = "The property '" + p + "' have got the unsupported value: " + value;
            LOGGER.log(Level.WARNING, msg);
            // Auto-correction:
            super.writeValue(p, null);
        } else {
            super.writeValue(p, value);
        }
    }

    /** Returns a primary key value. */
    public Long getPrimaryKey() throws UnsupportedOperationException {
        for (UjoProperty p : readProperties()) {
            if ($ID == p.getName()) {
                return (Long) readValue(p);
            }
        }
        //throw new UnsupportedOperationException("Primary key was not found");
        return null; // A case of special PK
    }

    @Override
     public int compareTo(AbstractBo ujo) {
         final UjoProperty p = readProperties().get(0);
         final Object c1 = p.getValue(this);
         final Object c2 = p.getValue(ujo);

         if (c1 instanceof Comparable && c2 instanceof Comparable) {
             return ((Comparable)c1).compareTo(c2);
         } else {
             return String.valueOf(c1).compareTo(String.valueOf(c2));
         }
     }

}
