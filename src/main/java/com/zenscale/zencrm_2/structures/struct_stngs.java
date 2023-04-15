package com.zenscale.zencrm_2.structures;


import com.zenscale.zencrm_2.entity.stngs_bukrs;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class struct_stngs {


    private String stngId;
    private String status;
    private String descr;
    private String type;
    private String parent;




    public struct_stngs(stngs_bukrs sb) {

        this.stngId = sb.getStngIdentity().getStngid();
        this.status = sb.getStatus();
    }




}
