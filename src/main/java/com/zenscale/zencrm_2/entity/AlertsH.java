package com.zenscale.zencrm_2.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "alerts_h")
public class AlertsH {

    @EmbeddedId
    AlertsHId id;

    private int docId;
    private String docType;
    private String creby;




}
