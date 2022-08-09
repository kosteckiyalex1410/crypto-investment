package com.crypto.model;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public enum RequestType {
    OLDEST("oldest"),
    NEWEST("newest"),
    MIN("min"),
    MAX("max");

    private final String name;

    RequestType(String name){
        this.name = name;
    }

    public static RequestType fromString(String name){
            return Arrays.stream(RequestType.values())
                    .filter(type -> type.name.equals(name))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Request Type " + name + " is not supported"));
    }
}
