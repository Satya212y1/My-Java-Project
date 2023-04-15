package com.zenscale.zencrm_2.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigInteger;
import java.sql.Timestamp;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "alerts_i")
public class AlertsI {

    @EmbeddedId
    AlertsIId id;

    private Timestamp executionTime;
    private BigInteger executionTimeStamp;
    private String status;




}
