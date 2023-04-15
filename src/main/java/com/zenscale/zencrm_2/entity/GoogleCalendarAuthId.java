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
public class GoogleCalendarAuthId implements Serializable {

    private static final long serialVersionUID = 7720261224263401205L;
    private int bukrs;
    private String uid;




}
