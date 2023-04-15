package com.zenscale.zencrm_2.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "stng_lead_assignment")
public class StngLeadAssignment implements Serializable {

    @EmbeddedId
    private StngLeadAssignmentId id;

    @Column(name = "userId", nullable = false)
    private String userId;




}
