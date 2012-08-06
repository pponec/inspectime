/*
 *  Copyright 2007-2010 Pavel Ponec
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */   
   
package org.ujorm.gxt.client;

import java.util.Collection;
import org.ujorm.Key;
import org.ujorm.gxt.client.cquery.CCriterion;
import org.ujorm.gxt.client.cquery.COperator;

public interface CCriterionProvider<UJO extends Cujo, VALUE> {

    /**
     * Create a new CCriterion where this property value is related to a parameter value along the {@link COperator}.
     * @param COperator COperator
     * <ul>
     * <li>VALUE - the parameter value</li>
     * <li>Key - reference to a related entity</li>
     * <li>List&lt;TYPE&gt; - list of values (TODO - this type is planned in the future)</li>
     * </ul>
     * @return A new CCriterion
     */
    public CCriterion<UJO> where
        ( COperator COperator
        , VALUE value
        );

    /**
     * Create a new CCriterion where this property is related to the value along the parameter {@link COperator}.
     * @param property Key
     * @param COperator COperator
     * <ul>
     * <li>VALUE - the parameter value</li>
     * <li>Key - reference to a related entity</li>
     * <li>List&lt;TYPE&gt; - list of values (TODO - this type is planned in the future)</li>
     * </ul>
     * @return A new CCriterion
     */
    public CCriterion<UJO> where
        ( COperator COperator
        , CujoProperty<?,VALUE> value
        );

    /**
     * Create a new CCriterion where this property equals the parameter value.
     * @param property Key
     * <ul>
     * <li>TYPE - parameter value</li>
     * <li>List&lt;TYPE&gt; - list of values</li>
     * <li>Key - reference to a related entity</li>
     * </ul>
     * @return A the new immutable CCriterion
     */
    public CCriterion<UJO> whereEq(VALUE value);

    /**
     * Create a new CCriterion where this property value equals the parameter value.
     * @param property Key can be type a direct of indirect (for a relation) property
     * @return A the new immutable CCriterion
     */
    public CCriterion<UJO> whereEq(CujoProperty<UJO,VALUE> property);

    /**
     * Create new CCriterion where this property value is in the one of parameter values.
     * @param property A direct or indeirect Ujo property
     * @param list A collection of the values. The collection argument can be the EMPTY, the CCriterion result will be FALSE in this case.
     * @return A the new immutable CCriterion.
     */
    public CCriterion<UJO> whereIn
        ( Collection<VALUE> list
        );

    /**
     * Create new CCriterion where this property value is not in any of parameter values.
     * @param property A direct or indeirect Ujo property
     * @param list A collection of the values. The collection argument can be the EMPTY, the CCriterion result will be TRUE in this case.
     * @return A the new immutable CCriterion.
     */
    public CCriterion<UJO> whereNotIn
        ( Collection<VALUE> list
        );

    /**
     * Create new CCriterion where this property value is in the one of parameter values.
     * @param property A reference to a related entity
     * @param list A collection of the values. The collection argument can be the EMPTY, the CCriterion result will be FALSE in this case.
     * @return A the new immutable CCriterion
     */
    public CCriterion<UJO> whereIn
        ( VALUE... list
        );

    /**
     * Create new CCriterion where this property value is not in any of parameter values.
     * @param property A property direct or indeirect Ujo property
     * @param list A collection of the values. The collection argument can be the EMPTY, the CCriterion result will be TRUE in this case.
     * @return A the new immutable CCriterion.
     */
    public CCriterion<UJO> whereNotIn
        ( VALUE... list
        );

    /** Create a new CCriterion where this property value is not equals the value
     * @see org.ujorm.CCriterion.COperator#NOT_EQ */
    public CCriterion<UJO> whereNeq(VALUE value);

    /** Create a new CCriterion where this property is great then the value
     * @see org.ujorm.CCriterion.COperator#GT */
    public CCriterion<UJO> whereGt(VALUE value);

    /** Create a new CCriterion where this property is great or equals the value
     * @see org.ujorm.CCriterion.COperator#GE */
    public CCriterion<UJO> whereGe(VALUE value);

    /** Create a new CCriterion where this property is less then the value
     * @see org.ujorm.CCriterion.COperator#LT */
    public CCriterion<UJO> whereLt(VALUE value);

    /** Create a new CCriterion where this property is less or equals than the value
     * @see org.ujorm.CCriterion.COperator#LE */
    public CCriterion<UJO> whereLe(VALUE value);

    /**
     * Create a new CCriterion where this property is {@code null}.
     * The method is a shortcut to the next full expression:
     * <pre class="pre">
     * Criterin.where(Order.NOTE_PROPERTY, COperator.EQ, (String) null) </pre>
     * for the String property type in this case.
     * @param property Key
     * @see #whereNotNull(org.ujorm.Key)
     * @see COperator#EQ
     */
    public CCriterion<UJO> whereNull();

    /**
     * Create a new CCriterion where this property is not {@code null}.
     * The method is a shortcut to the next full expression:
     * <pre class="pre">
     * Criterin.where(Order.NOTE_PROPERTY, COperator.EQ, (String) null) </pre>
     * for the String property type in this case.
     * @param property Key
     * @see #whereNull(org.ujorm.Key)
     * @see COperator#NOT_EQ
     */
    public CCriterion<UJO> whereNotNull();

    /** Create a new CCriterion for this property where all results will be true (the result is independed on the value).
     *  The method evaluate(ujo) returns TRUE always.
     * @param property The parameter is required by Ujorm to location a basic database table and the join relations in case a composed Property
     */
    public CCriterion<UJO> forAll();

    /** Create a new CCriterion for this property where all results will be false (the result is independed on the value).
     *  The  method evaluate(method) returns FALSE always.
     * @param property The parameter is required by Ujorm to location a basic database table and the join relations in case a composed Property
     */
    public CCriterion<UJO> forNone();

}
