package com.zenscale.zencrm_2.structures;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class struct_response_get_bukrs {

    private int status;
    private String msg;

    private int companyCode;




    public struct_response_get_bukrs(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }




}
