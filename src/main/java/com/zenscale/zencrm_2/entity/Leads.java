package com.zenscale.zencrm_2.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "leads")
@Accessors(chain = true)
public class Leads implements Serializable {

    @EmbeddedId
    private LeadId id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "remarks", nullable = false)
    private String remarks;

    @Column(name = "status")
    private String status;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "postingDate", nullable = false)
    private Date postingDate;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "sourceId", nullable = false)
    private Integer sourceId;

    private String assignTo;

    @Column(name = "creby", nullable = false)
    private String creby;

    /*@Column(name = "creon", nullable = false)
    private Date creon;*/

    @Column(name = "stats", nullable = false)
    private String stats;

    @Column(name = "delstats", nullable = false)
    private String delstats;




}
