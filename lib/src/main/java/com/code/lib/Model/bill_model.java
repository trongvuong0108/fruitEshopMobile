package com.code.lib.Model;

import java.util.List;

import lombok.Data;

@Data
public class bill_model {
    private List<detail_BillModel> detailList;
    private String name;
    private String address;
    private String phone;
    private String email;
}
