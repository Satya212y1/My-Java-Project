package com.zenscale.zencrm_2.service;

import com.zenscale.zencrm_2.scaledb.ReportDb;
import com.zenscale.zencrm_2.structures.lead_cmts;
import com.zenscale.zencrm_2.structures.struct_list_lead;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    ReportDb reportDb;




    public List<struct_list_lead> getTotalLeads(int bukrs, int limit, int offset, String fromDate, String toDate, int leadId, int stageId) {

        List<struct_list_lead> list = reportDb.getTotalLeads(bukrs, limit, offset, fromDate, toDate, leadId, stageId);
        List<lead_cmts> listCmts =  reportDb.getLeadComments(bukrs);
        Map<Integer, struct_list_lead> mapLead = reportDb.getMapLeads(list);

        for (lead_cmts lc : listCmts) {
            if (mapLead.containsKey(lc.getLeadId())) {
                mapLead.get(lc.getLeadId()).getComments().add(lc);
            }
        }
        orderLeadsByDateDescending(list);
        return list;
    }




    public int getTotalLeadCount(int bukrs, String fromDate, String toDate, int leadId, int stageId) {

        return reportDb.getTotalLeadCount(bukrs, fromDate, toDate, leadId, stageId);
    }






    private void orderLeadsByDateDescending(List<struct_list_lead> list) {

        Collections.sort(list, new Comparator<struct_list_lead>() {
            public int compare(struct_list_lead o1, struct_list_lead o2) {

                if (o1.getCreationDate() == null || o2.getCreationDate() == null) {
                    return 0;
                }
                return o2.getCreationDate().compareTo(o1.getCreationDate());
            }
        });
    }

}
