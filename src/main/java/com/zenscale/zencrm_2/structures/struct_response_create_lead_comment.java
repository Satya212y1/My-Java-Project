package com.zenscale.zencrm_2.structures;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class struct_response_create_lead_comment {

    private int status;
    private String msg;
    private int commentId;




    public struct_response_create_lead_comment(int status, String msg) {

        this.status = status;
        this.msg = msg;
    }




}
