package com.zenscale.zencrm_2.structures;

import com.zenscale.zencrm_2.structures.struct_list_lead;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class struct_leadsinfo_by_stage {

    private Integer stageId;
    private String stageName;
    private String colorCode;
    private Integer totalLeads;

    private List<struct_list_lead> listLeads = new ArrayList<>();
}
