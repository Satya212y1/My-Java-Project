package com.zenscale.zencrm_2.structures;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.catalina.LifecycleState;

import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class struct_response_create_alerts {

    private int status;
    private String msg;
    private int alertId;
    private int alertCounter;




    public struct_response_create_alerts(int status, String msg) {

        this.status = status;
        this.msg = msg;
    }




}
