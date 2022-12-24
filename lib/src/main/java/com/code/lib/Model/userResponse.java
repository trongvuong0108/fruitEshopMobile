package com.code.lib.Model;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class userResponse implements Serializable {
    private String address;
    private String phone;
    private String fullname;
    private String email;
    private String username;
}
