package com.zenscale.zencrm_2.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class AlertsHId implements Serializable {

    private static final long serialVersionUID = 6635099274396236469L;
    private int bukrs;
    private int alertId;




}
