package com.zenscale.zencrm_2.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class StngLeadAssignmentId implements Serializable {

    private static final long serialVersionUID = -8255871815524166788L;

    @Column(name = "bukrs", nullable = false)
    private Integer bukrs;

    @Column(name = "sourceId", nullable = false)
    private Integer sourceId;


}
