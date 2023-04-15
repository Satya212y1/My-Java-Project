package com.zenscale.zencrm_2.structures;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigInteger;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class struct_email_alert {

    private Integer bukrs;
    private Integer alertId;
    private Integer counter;
    private String executionTime;
    private BigInteger executionTimeStamp;
    private String status;
    private String uid;
    private Integer docId;
    private String docType;
    private String creby;

}
