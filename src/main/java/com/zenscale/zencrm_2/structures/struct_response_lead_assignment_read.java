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
public class struct_response_lead_assignment_read {


    private int status;
    private String msg;

    private int totalRecord;
    private List<struct_lead_assignment> data;
    private List<struct_lead_assignment> dataHistory;




    public struct_response_lead_assignment_read(int status, String msg) {

        this.status = status;
        this.msg = msg;
    }




}
