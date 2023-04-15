package com.zenscale.zencrm_2.structures;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class struct_lead_assignment {

    private int stageId;
    private String stageDesc;
    private int leadId;
    private String leadDesc;
    private String creon;
    private String duration;

    //@JsonIgnore
    private String stats;




}
