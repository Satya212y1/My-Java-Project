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
public class struct_response_leads_by_stages_kanban {

    private int status;
    private String msg;
    private List<struct_leadsinfo_by_stage> leadsByStage;



    public struct_response_leads_by_stages_kanban(int status, String msg) {

        this.status = status;
        this.msg = msg;
    }
}
