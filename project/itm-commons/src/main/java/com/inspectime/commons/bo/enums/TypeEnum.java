/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.inspectime.commons.bo.enums;

import java.io.Serializable;
import org.ujorm.extensions.StringWrapper;

/**
 * Feature
 * @author Pavel Ponec
 */
public enum TypeEnum implements StringWrapper, Serializable {

    FEATURE("F"),
    BUG("B"),
    ;

   private final String key;

    private TypeEnum(String key) {
        this.key = key;
    }

    @Override
    public String exportToString() {
        return key;
    }
}
