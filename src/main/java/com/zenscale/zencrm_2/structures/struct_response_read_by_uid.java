package com.zenscale.zencrm_2.structures;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class struct_response_read_by_uid {

    private int status;
    private String msg;

    private String mobile = "";
    private String whatsApp = "";
    private String email = "";




    public struct_response_read_by_uid(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }




}
