package com.zenscale.zencrm_2.service;

import com.zenscale.zencrm_2.entity.Leads;
import com.zenscale.zencrm_2.scaledb.DashboardDb;
import com.zenscale.zencrm_2.scaledb.ReportDb;
import com.zenscale.zencrm_2.scaledb.StageDb;
import com.zenscale.zencrm_2.structures.*;
import com.zenscale.zencrm_2.utils.CommonFunctions;
import com.zenscale.zencrm_2.utils.CommonStrings;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DashboardService {

    @Autowired
    DashboardDb dashboardDb;

    @Autowired
    StageDb stageDb;

    @Autowired
    ReportDb reportDb;

    @Autowired
    CommonService commonService;

    CommonFunctions cf = new CommonFunctions();




    public struct_response_leads_by_days getLeadsByDays(int bukrs, String fromDate, String toDate) {

        List<struct_leads_by_days> leadsByDays = new ArrayList<>();
        List<struct_leads_by_days> list = dashboardDb.getLeadsByDays(bukrs, fromDate, toDate);

        Date s = cf.getFromattedDate(CommonStrings.dateformat_hyphen_yyyyMMdd, fromDate);
        Date e = cf.getFromattedDate(CommonStrings.dateformat_hyphen_yyyyMMdd, toDate);
        List<String> listDates = cf.getDatesBetweenBetweenTwoDates(s, e);

        for (int i = 0; i < listDates.size(); i++) {
            struct_leads_by_days itemDays = new struct_leads_by_days();
            itemDays.setDate(listDates.get(i));
            for (int j = 0; j < list.size(); j++) {
                if (listDates.get(i).equals(list.get(j).getDate())) {
                    itemDays.setLeadCount(list.get(j).getLeadCount());
                }
            }
            leadsByDays.add(itemDays);
        }

        if (leadsByDays != null && leadsByDays.size() > 0) {
            return new struct_response_leads_by_days(CommonStrings.api_status_success, "Records retrieved successfully", leadsByDays);
        }
        return new struct_response_leads_by_days(CommonStrings.api_status_failure, "No record found", leadsByDays);
    }




    public struct_response_leads_by_stages getLeadsByStages(int bukrs, String fromDate, String toDate) {

        List<struct_stage_lead_count> leadsByStage = dashboardDb.getTotalLeadsByStages(bukrs, fromDate, toDate);
        int unassignedLeads = dashboardDb.getUnassignedLead(bukrs, fromDate, toDate);
        leadsByStage.add(new struct_stage_lead_count(-1, "Unassigned Leads", "", unassignedLeads));
        if (leadsByStage != null && leadsByStage.size() > 0) {
            return new struct_response_leads_by_stages(CommonStrings.api_status_success, "Records retrieved successfully", leadsByStage);
        }
        return new struct_response_leads_by_stages(CommonStrings.api_status_failure, "No record found", leadsByStage);
    }




}
