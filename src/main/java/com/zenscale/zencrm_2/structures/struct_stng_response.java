package com.zenscale.zencrm_2.structures;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class struct_stng_response {

    private int status;
    private String msg;

    private List<struct_stngs> stngs;




    public struct_stng_response(int status, String msg) {

        this.status = status;
        this.msg = msg;
    }




}
