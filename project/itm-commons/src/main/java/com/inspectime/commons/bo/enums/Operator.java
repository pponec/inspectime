/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inspectime.commons.bo.enums;

import org.ujorm.extensions.ValueExportable;

/**
 * Approval operator
 * @author Ponec
 */
public enum Operator implements ValueExportable {

    /** A leaf of the tree */
    LEAF,
    /** Operator AND for all children */
    AND,
    /** Operator OR for all children */
    OR;

    public String exportToString() {
        return name().substring(0, 1);
    }


}
