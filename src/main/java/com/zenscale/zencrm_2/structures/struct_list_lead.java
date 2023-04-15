package com.zenscale.zencrm_2.structures;

import io.swagger.models.auth.In;
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
public class struct_list_lead {

    private int leadId ;
    private String leadName;
    private String remarks;
    private String status;
    private String mobile;
    private String postingDate;
    private String email;
    private Integer sourceId;
    private String sourceDesc;
    private String creby;
    private String creationDate;
    private String assignTo;
    private String assignToUname;
    private Integer stageId;
    private String stageName;
    private String stageStatus = "";

    private List<lead_cmts> comments = new ArrayList<>();




    public String getStatus() {
        if (status.equals("O")) {
            return "Open";
        } else if (status.equals("I")) {
            return "Inprocess";
        } else if (status.equals("C")) {
            return "Closed";
        }
        return "";
    }




    public Integer getStageId() {
        if (stageId == null) {
            return 0;
        }
        return stageId;
    }




}
