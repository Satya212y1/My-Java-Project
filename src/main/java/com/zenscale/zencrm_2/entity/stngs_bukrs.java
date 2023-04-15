package com.zenscale.zencrm_2.entity;

import com.zenscale.zencrm_2.structures.struct_stngs;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "stngs_bukrs")
@Data
@NoArgsConstructor
@ToString
public class stngs_bukrs {

    @EmbeddedId
    private StngIdentity stngIdentity;

    private String status;


    public stngs_bukrs(int bukrs , struct_stngs stngs){
    StngIdentity si = new StngIdentity();
        si.setBukrs(bukrs);
        si.setStngid(stngs.getStngId());
        this.stngIdentity = si;
        this.status = stngs.getStatus();
    }
}
