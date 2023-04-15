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
public class struct_lead_comment {

    private Integer leadId;
    private String leadDesc;
    private Integer stageId;
    private String stageDesc;
    private String comment;
    private Integer commentId;
    private String createdDate;
}
