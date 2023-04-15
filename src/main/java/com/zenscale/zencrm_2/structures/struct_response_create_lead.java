package com.zenscale.zencrm_2.structures;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class struct_response_create_lead {


    private int status;
    private String msg;
    private int leadId;




    public struct_response_create_lead(int status, String msg) {

        this.status = status;
        this.msg = msg;
    }




}
