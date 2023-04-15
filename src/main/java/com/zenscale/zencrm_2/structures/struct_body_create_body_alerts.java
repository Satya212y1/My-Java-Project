package com.zenscale.zencrm_2.structures;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigInteger;
import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class struct_body_create_body_alerts {

    private int docId;
    private String docType;
    private String creby;
    private String executionTime;
    private BigInteger executionTimeStamp;




}
