package com.zenscale.zencrm_2.structures;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class struct_response_create_stage {

    private int status;
    private String msg;
    private int stageId;




    public struct_response_create_stage(int status, String msg) {

        this.status = status;
        this.msg = msg;
    }




}
