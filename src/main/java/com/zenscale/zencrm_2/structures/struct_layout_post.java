package com.zenscale.zencrm_2.structures;

import lombok.Data;

import java.util.List;

@Data
public class struct_layout_post {

    private String uid;
    private List<struct_layout> layout;
}