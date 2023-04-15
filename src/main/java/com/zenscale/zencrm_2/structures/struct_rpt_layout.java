package com.zenscale.zencrm_2.structures;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;

@Data
@NoArgsConstructor
public class struct_rpt_layout {

    private int status;
    private String msg;
    private int totalRecords;
    HashMap<Integer, Object> head_pos;
    HashMap<Integer, Object> head_width;
    HashMap<Integer, HashMap<Integer, Object>> item;
    List<struct_layout> layout;
    HashMap<Integer, String> head_color;




    public struct_rpt_layout(int status, String msg) {

        this.status = status;
        this.msg = msg;
    }




}