package com.zenscale.zencrm_2.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@ToString
public class StngIdentity implements Serializable {

    private static final long serialVersionUID = -2069045553721163777L;
    private int bukrs;
    private String stngid;




}
