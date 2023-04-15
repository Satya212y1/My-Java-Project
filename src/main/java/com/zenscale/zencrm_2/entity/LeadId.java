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
public class LeadId implements Serializable {

    private static final long serialVersionUID = 1069400047004340547L;
    private int bukrs;
    private int leadId;
    private int objid;


}
