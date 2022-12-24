package com.code.lib.Model;
import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.ToString;

@Data
@ToString

public class cartItem {
    private product product ;
    private float quality;
}
