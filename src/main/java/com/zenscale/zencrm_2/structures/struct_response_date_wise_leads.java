package com.zenscale.zencrm_2.structures;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class struct_response_date_wise_leads {

    private int status;
    private String msg;

    private List<struct_leads_by_days> data;
}
