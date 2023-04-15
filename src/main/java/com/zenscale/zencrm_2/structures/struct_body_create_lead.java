package com.zenscale.zencrm_2.structures;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class struct_body_create_lead {

    private int leadId;

    private String leadName;
    private String remarks;
    private String mobile;
    private String postingDate;
    private String email;
    private int sourceId;
    private String assignTo;
    private String creby;




}
