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
public class lead_cmts {

    @JsonIgnore
    private int leadId;

    private int stageId;
    private String comment;
    private String commentDate;
    private int commentId;




}
