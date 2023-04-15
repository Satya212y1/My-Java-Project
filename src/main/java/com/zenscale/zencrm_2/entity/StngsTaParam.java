package com.zenscale.zencrm_2.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "stngs_ta_params")
public class StngsTaParam {


    @Id
    @Column(name = "bukrs", nullable = false)
    private Integer id;

    @Column(name = "preProcessDays", nullable = false)
    private Integer preProcessDays;

    @Column(name = "postProcessDays", nullable = false)
    private Integer postProcessDays;

    /*@Column(name = "creon", nullable = false)
    private Instant creon;*/



}