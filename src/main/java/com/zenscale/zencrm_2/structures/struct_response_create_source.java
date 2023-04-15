package com.zenscale.zencrm_2.structures;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class struct_response_create_source {


    private int status;
    private String msg;
    private int sourceId;




    public struct_response_create_source(int status, String msg) {

        this.status = status;
        this.msg = msg;
    }




}
