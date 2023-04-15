package com.zenscale.zencrm_2.structures;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class struct_alerts_list {

    private int alertId;
    private int docId;
    private String docType;
    private int leadId;
    private String leadDesc;
    private int stageId;
    private String stageDesc;
    private String comment;
    private String creon;


    private List<struct_executions> alertExecutions = new ArrayList<>();


}
