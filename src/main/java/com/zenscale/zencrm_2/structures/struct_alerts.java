package com.zenscale.zencrm_2.structures;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class struct_alerts {

    private Integer alertId;
    private Integer counter;
    private String executionTime;
    private String status;
    private Integer docId;
    private String docType;
    private String creby;
    private String creon;
    private String comments;
    private Integer leadId;
    private String leadDesc;
    private Integer stageId;
    private String stageDesc;




}
