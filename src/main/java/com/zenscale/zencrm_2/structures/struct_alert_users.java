package com.zenscale.zencrm_2.structures;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class struct_alert_users {

    @JsonIgnore
    private Integer alertId;
    private Integer counter;
    private String uid;




}
