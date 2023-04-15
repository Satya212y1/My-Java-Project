package com.zenscale.zencrm_2.structures;

import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.json.JSONArray;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class struct_response_read_events {

    private int status;
    private String msg;

    @JsonRawValue
    private String data;



    public struct_response_read_events(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }




}
