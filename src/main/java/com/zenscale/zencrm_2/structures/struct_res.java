package com.zenscale.zencrm_2.structures;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class struct_res {

    private int status;
    private String msg;





    public struct_res(int status, String msg) {

        this.status = status;
        this.msg = msg;
    }




}
