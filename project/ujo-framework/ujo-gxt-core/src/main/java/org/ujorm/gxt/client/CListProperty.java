/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2010-2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@ujorm.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package org.ujorm.gxt.client;

import java.util.ArrayList;
import java.util.List;

/**
 * The main implementation of the interface ListUjoProperty.
 * @author Pavel Ponec
 */
public class CListProperty<UJO extends Cujo, ITEM>
    extends CProperty<UJO,List<ITEM>>
    implements CListUjoProperty<UJO,ITEM> {

    /** Class of the list item. */
    final private Class<ITEM> itemType;

    /** Protected constructor */
    protected CListProperty(String name, Class<ITEM> itemType, int index) {
        super(name, ArrayList.class, null, index);
        this.itemType = itemType;
    }


    /** Returns a count of Items. If a property value is null, method returns 0. */
    @Override
    public int getItemCount(final UJO ujo) {
        List<ITEM> list = getValue(ujo);
        return list!=null ? list.size() : 0 ;
    }

    /** Return a Class of the Item. */
    @Override
    public Class<ITEM> getItemType() {
        return itemType;
    }

    /** Returns true if the item type is a type or subtype of the parameter class. */
    @SuppressWarnings("unchecked")
    @Override
    public boolean isItemTypeOf(final Class aType) {
        boolean result = this.itemType.equals(aType);
        if (result) {
            return result;
        }
        else if(Number.class.equals(aType)) {
            result = Short.class.equals(itemType) ||
                Integer.class.equals(itemType) ||
                Long.class.equals(itemType) ||
                Float.class.equals(itemType) ||
                Double.class.equals(itemType) //
                // BigInt.class.equals(type)
                // BigDecimal.class.equals(type)
                ;
        }
        else if (CEnum.class.equals(aType)) {
            result = this.getClass().equals(CPropertyEnum.class);
        }
        else if (java.util.Date.class.equals(aType)) {
            result = java.sql.Date.class.equals(itemType);
        }
        return result;
    }

    /**
     * Returns a value of property. The result is the same, like Ujo#getValue(UjoPropertyList) .get(index) .
     */
    @Override
    public ITEM getItem(final UJO ujo, final int index) {
        return getValue(ujo).get(index);
    }

    /** Set a property item value. The action is the same, like Ujo#getValue(UjoPropertyList) .set(indexm, value).
     * @return the element previously at the specified position.
     */
    @Override
    public ITEM setItem(final UJO ujo, final int index, final ITEM value) {
        return getValue(ujo).set(index, value);
    }

    /** Add Item Value. If List is null, the method creates an instance.
     * @return true (as per the general contract of Collection.add).
     */
    @Override
    public boolean addItem(final UJO ujo, final ITEM value) {
        List<ITEM> result = getList(ujo);
        return result.add(value);
    }

    /** Removes the first occurrence in this list of the specified element.
     * @return true if this list is not null and contains the specified element, otherwise returns false.
     * @since 0.81
     */
    @Override
    public boolean removeItem(UJO ujo, ITEM value) {
        List<ITEM> list = getValue(ujo);
        return list!=null && list.remove(value);
    }

    /**
     * Returns a not null List. If original list value is null, then a new List is created by a property type.
     * If the property type is an interface then the ArrayList instance is used.
     * @see #getItem(Ujo,int)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<ITEM> getList(final UJO ujo) {
        List<ITEM> result = getValue(ujo);
        if (result==null) {
            result = new ArrayList();
            setValue(ujo, result);
        }
        return result;
    }

    /** Sort a list by its properties. */
    @SuppressWarnings("unchecked")
    public void sort(UJO ujo, CujoProperty ... properties) {
        throw new IllegalArgumentException("The method sort is not supported");
    }


    /** Indicates whether a list of items is null or empty. */
    @Override
    public boolean isDefault(UJO ujo) {
        List<ITEM> list = getValue(ujo);
        return list==null || list.isEmpty();
    }

    // --------- STATIC METHODS -------------------

    /** A ListUjoProperty Factory
     * Method assigns a next property index.
     * @hidden
     */
    @SuppressWarnings("unchecked")
    public static <UJO extends Cujo, ITEM> CListProperty<UJO,ITEM> newListProperty
    ( final String name
    , final Class<ITEM> itemType
    , final int index
    ) {
        return new CListProperty<UJO,ITEM>(name, (Class) List.class, index);
    }
    
}
