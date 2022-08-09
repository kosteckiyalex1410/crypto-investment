package com.crypto.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class Values {

    private Date timestamp;
    private String symbol;
    private Double price;
}
