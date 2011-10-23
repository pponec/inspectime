/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2010-2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@ujorm.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package org.ujorm.gxt.client;

import java.util.Comparator;

/**
 *
 * @author Roman Slavik
 */
public class CujoComparator implements Comparator<Cujo> {

    final CujoProperty[] properties;

    public CujoComparator(final CujoProperty ... properties) {
        this.properties = properties;
    }

    @Override
    public int compare(Cujo o1, Cujo o2) {
        for (CujoProperty property : properties) {

            Comparable c1 = (Comparable) o1.get(property);
            Comparable c2 = (Comparable) o2.get(property);
            
            if (c1==c2  ) { continue;  }
            if (c1==null) { return +1; }
            if (c2==null) { return -1; }

            int result = property.isAscending()
            ? c1.compareTo(c2)
            : c2.compareTo(c1)
            ;
            if (result!=0) { return result; }
        }
        return 0;
    }

    /** A String representation. */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(32);
        for (CujoProperty property : properties) {
            if (sb.length()>0) {
                sb.append(", ");
            }
            sb.append(property.getName());
            sb.append("[");
            sb.append(property.isDirect() ? "ASC" : "DESC");
            sb.append("]");
        }
        return sb.toString();
    }


}
