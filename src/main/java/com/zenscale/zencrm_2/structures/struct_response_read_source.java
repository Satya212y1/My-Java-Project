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
public class struct_response_read_source {

    private int status;
    private String msg;
    private int totalRecords;
    private List<struct_source> list;




    public struct_response_read_source(int status, String msg) {

        this.status = status;
        this.msg = msg;
    }
}
