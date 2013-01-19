/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inspectime.commons.bo.enums;

import org.ujorm.extensions.StringWrapper;

/**
 * TaskStateEnum
 * @author Ponec
 */
public enum TaskStateEnum implements StringWrapper {

    NEW("NW"),
    ACCEPTED("AC"),
    REJECTED("RJ"),
    FIXED("FX"),
    REOPENED("RO"),
    ;

    private final String key;

    private TaskStateEnum(String key) {
        this.key = key;
    }

    @Override
    public String exportToString() {
        return key;
    }

}
