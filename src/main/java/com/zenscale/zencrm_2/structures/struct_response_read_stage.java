package com.zenscale.zencrm_2.structures;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class struct_response_read_stage {

    private int status;
    private String msg;
    private int totalRecords;
    private List<struct_stages> list;




    public struct_response_read_stage(int status, String msg) {

        this.status = status;
        this.msg = msg;
    }




}
