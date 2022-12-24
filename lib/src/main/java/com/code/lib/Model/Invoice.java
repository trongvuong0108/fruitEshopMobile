package com.code.lib.Model;

import java.io.Serializable;

import lombok.Data;

@Data
public class Invoice implements Serializable {
    private int id;
    private String createAt ;
    private String name;
    private String address;
    private String phone;
    private float total;
}
