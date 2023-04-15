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
public class struct_response_alerts_read {

    private int status;
    private String msg;

    private List<struct_alerts_list> alertsList;




    public struct_response_alerts_read(int status, String msg) {

        this.status = status;
        this.msg = msg;
    }




}
