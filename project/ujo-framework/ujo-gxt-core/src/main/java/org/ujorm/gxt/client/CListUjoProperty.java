/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2010-2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@ujorm.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package org.ujorm.gxt.client;

import java.util.List;

/**
 * A property list metadata of Unified Data Object.
 * @author Pavel Ponec
 */
public interface CListUjoProperty<UJO extends Cujo, ITEM> extends CujoProperty<UJO,List<ITEM>> {

    /** Returns a class of the property. */
    public Class<ITEM> getItemType();

    /** Returns a count of Items. If the property is null, method returns 0. */
    public int getItemCount(UJO ujo);

    /** Returns true if the item type is a type or subtype of the parameter class. */
    public boolean isItemTypeOf(Class type);

    /**
     * Returns a value of property. The result is the same, like Ujo#readValue(ListUjoPropertyCommon).
     */
    public ITEM getItem(UJO ujo, int index);

    /**
     * Return a not null List. If original list value is empty, the new List is created.
     * @see #getItem(Ujo, int)
     */
    public List<ITEM> getList(UJO ujo);

    /** Set a property item value.
     * @return the element previously at the specified position.
     */
    public ITEM setItem(UJO ujo, int index, ITEM value);

    /** Add an Item Value. If List is null, the method create an instance of List (for exact behaviour see an implementation).
     * @return true (as per the general contract of Collection.add).
     */
    public boolean addItem(UJO ujo, ITEM value);

    /** Removes the first occurrence in this list of the specified element.
     * @return true if this list is not null and contains the specified element, otherwise returns false.
     * @since 0.81
     */
    public boolean removeItem(UJO ujo, ITEM value);

    /** Indicates whether a list of items is null or empty. */
    public boolean isDefault(UJO ujo);

}
