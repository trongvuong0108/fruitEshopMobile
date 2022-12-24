package com.code.lib.Model;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class product implements Serializable {
    private String name ;
    private float price_out;
    private category category;
    private String img ;
    private float quantity;
}
