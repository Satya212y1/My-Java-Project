package com.zenscale.zencrm_2.api;

import com.zenscale.zencrm_2.entity.Leads;
import com.zenscale.zencrm_2.entity.StagesM;
import com.zenscale.zencrm_2.service.AuthService;
import com.zenscale.zencrm_2.service.CommonService;
import com.zenscale.zencrm_2.service.LeadsService;
import com.zenscale.zencrm_2.service.SourceService;
import com.zenscale.zencrm_2.structures.*;
import com.zenscale.zencrm_2.structures.struct_response_create_lead;
import com.zenscale.zencrm_2.utils.CommonFunctions;
import com.zenscale.zencrm_2.utils.CommonStrings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

@RestController
@RequestMapping("/api/crm/leads/")
public class LeadsController {

    @Autowired
    LeadsService leadsService;

    @Autowired
    AuthService authService;

    @Autowired
    SourceService sourceService;

    @Autowired
    CommonService commonService;

    CommonFunctions cf = new CommonFunctions();




    @PostMapping("create_update")
    public struct_response_create_lead createLead(@RequestHeader(name = "Authorization", required = false, defaultValue = "") String token,
                                                  @RequestBody(required = false) struct_body_create_lead body) {

        if (token.equals("")) {
            return new struct_response_create_lead(CommonStrings.api_status_failure, "Authorization missing");
        }

        int bukrs = authService.get_bukrs_token(token);
        if (bukrs == 0) {
            return new struct_response_create_lead(CommonStrings.api_status_failure, "Invalid Authorization token");
        }

        if (body == null) {
            return new struct_response_create_lead(CommonStrings.api_status_failure, "Body params missing");
        }

        if (cf.checkNullString(body.getLeadName()).length() == 0) {
            return new struct_response_create_lead(CommonStrings.api_status_failure, "Lead name missing");
        }

        if (cf.checkNullString(body.getMobile()).length() == 0) {
            return new struct_response_create_lead(CommonStrings.api_status_failure, "Mobile number missing");
        }

        if (cf.checkNullString(body.getEmail()).length() == 0) {
            return new struct_response_create_lead(CommonStrings.api_status_failure, "Email missing");
        }

        if (cf.checkNullString(body.getCreby()).length() == 0) {
            return new struct_response_create_lead(CommonStrings.api_status_failure, "Created by user missing");
        }

        if (body.getSourceId() == 0) {
            return new struct_response_create_lead(CommonStrings.api_status_failure, "Source id missing");
        }

        if (!sourceService.isSourceExist(bukrs, body.getSourceId())) {
            return new struct_response_create_lead(CommonStrings.api_status_failure, "Source id does not exist by this source id");
        }

        if (cf.checkNullString(body.getPostingDate()).length() > 0) {
            Date bodyPostingDate = leadsService.getPostingDate(body.getPostingDate());
            if (bodyPostingDate == null) {
                return new struct_response_create_lead(CommonStrings.api_status_failure, "Invalid date format ! Valid date format is yyyy-MM-dd");
            }
        }

        if (body.getLeadId() > 0) {
            if (!leadsService.isLeadExist(bukrs, body.getLeadId())) {
                return new struct_response_create_lead(CommonStrings.api_status_failure, "Lead does not exist by this lead id");
            }

            Date bodyPostingDate = leadsService.getPostingDate(body.getPostingDate());
            Leads leads = leadsService.findLeadExist(bukrs, body.getLeadId());

            if (leads.getName().equals(body.getLeadName()) &&
                    leads.getRemarks().equals(cf.checkNullString(body.getRemarks())) &&
                    leads.getMobile().equals(body.getMobile()) &&
                    leads.getPostingDate().compareTo(bodyPostingDate) == 0 &&
                    leads.getEmail().equals(body.getEmail()) &&
                    leads.getSourceId() == body.getSourceId() &&
                    leads.getCreby().equals(body.getCreby()) &&
                    leads.getAssignTo().equals(cf.checkNullString(body.getAssignTo()))) {
                return new struct_response_create_lead(CommonStrings.api_status_failure, "No changes found");
            }
        }

        return leadsService.createLead(bukrs, body);
    }




    @GetMapping("read")
    public struct_response_read_lead readLead(@RequestHeader(name = "Authorization", required = false, defaultValue = "") String token,
                                              @RequestParam(name = "limit", required = false, defaultValue = "0") int limit,
                                              @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
                                              @RequestParam(name = "leadid", required = false, defaultValue = "0") int leadid,
                                              @RequestParam(name = "status", required = false, defaultValue = "0") int status,
                                              @RequestParam(name = "sourceId", required = false, defaultValue = "0") int sourceId,
                                              @RequestParam(name = "stageId", required = false, defaultValue = "0") int stageId,
                                              @RequestParam(name = "search", required = false, defaultValue = "") String search,
                                              @RequestParam(name = "fromDate", required = false, defaultValue = "") String fromDate,
                                              @RequestParam(name = "toDate", required = false, defaultValue = "") String toDate,
                                              @RequestParam(name = "stageStatus", required = false, defaultValue = "") String stageStatus,
                                              @RequestParam(name = "postingDate", required = false, defaultValue = "") String postingDate) {

        if (token.equals("")) {
            return new struct_response_read_lead(CommonStrings.api_status_failure, "Authorization missing");
        }

        int bukrs = authService.get_bukrs_token(token);
        if (bukrs == 0) {
            return new struct_response_read_lead(CommonStrings.api_status_failure, "Invalid Authorization token");
        }

        if (fromDate.length() > 0 && toDate.length() > 0) {
            if (!cf.isDateFormatted(CommonStrings.dateformat_hyphen_yyyyMMdd, fromDate)) {
                return new struct_response_read_lead(CommonStrings.api_status_failure, "Invalid From-Date format ! Valid From-Date format is yyyy-MM-dd");
            }
            if (!cf.isDateFormatted(CommonStrings.dateformat_hyphen_yyyyMMdd, toDate)) {
                return new struct_response_read_lead(CommonStrings.api_status_failure, "Invalid To-Date format ! Valid To-Date format is yyyy-MM-dd");
            }
            /*if (!cf.isToDateGreaterThanFromDate(fromDate, toDate, CommonStrings.dateformat_hyphen_yyyyMMdd)) {
                return new struct_response_read_lead(CommonStrings.api_status_failure, "From-Date must occur before To-Date");
            }*/
        }

        return leadsService.readLeads(bukrs, leadid, status, sourceId, search.toLowerCase(), limit, offset, stageId, fromDate, toDate,
                fromDate, toDate, stageStatus, postingDate, token);
    }




    @DeleteMapping("delete")
    public struct_response_delete_lead deleteLead(@RequestHeader(name = "Authorization", required = false, defaultValue = "") String token,
                                                  @RequestParam(name = "leadid", required = false, defaultValue = "") int leadid) {

        if (token.equals("")) {
            return new struct_response_delete_lead(CommonStrings.api_status_failure, "Authorization missing");
        }

        int bukrs = authService.get_bukrs_token(token);
        if (bukrs == 0) {
            return new struct_response_delete_lead(CommonStrings.api_status_failure, "Invalid Authorization token");
        }

        if (!leadsService.isLeadExist(bukrs, leadid)) {
            return new struct_response_delete_lead(CommonStrings.api_status_failure, "Lead does exist by this lead id");
        }

        return leadsService.deleteLead(bukrs, leadid);
    }




    @PostMapping("assign_source_user")
    public struct_response_assign_lead assignLead(@RequestHeader(name = "Authorization", required = false, defaultValue = "") String token,
                                                  @RequestBody(required = false) struct_assign_lead_body body) {

        if (token.equals("")) {
            return new struct_response_assign_lead(CommonStrings.api_status_failure, "Authorization missing");
        }

        int bukrs = authService.get_bukrs_token(token);
        if (bukrs == 0) {
            return new struct_response_assign_lead(CommonStrings.api_status_failure, "Invalid Authorization token");
        }

        if (body == null) {
            return new struct_response_assign_lead(CommonStrings.api_status_failure, "Body params missing");
        }

        if (body.getSourceId() == 0) {
            return new struct_response_assign_lead(CommonStrings.api_status_failure, "Source id missing");
        }

        if (cf.checkNullString(body.getUserId()).length() == 0) {
            return new struct_response_assign_lead(CommonStrings.api_status_failure, "User id missing");
        }

        if (!sourceService.isSourceExist(bukrs, body.getSourceId())) {
            return new struct_response_assign_lead(CommonStrings.api_status_failure, "Source does not exist by this id");
        }

        if (!commonService.isUidExist(bukrs, body.getUserId())) {
            return new struct_response_assign_lead(CommonStrings.api_status_failure, "User id does exists");
        }

        return leadsService.assignSourceToUser(bukrs, body);
    }




    @GetMapping("leads_by_stages")
    public struct_response_leads_by_stages_kanban leadsByStages(@RequestHeader(value = "Authorization", required = false, defaultValue = "") String token,
                                                                @RequestParam(name = "fromDate", required = false, defaultValue = "") String fromDate,
                                                                @RequestParam(name = "toDate", required = false, defaultValue = "") String toDate) {

        if (token.length() == 0) {
            return new struct_response_leads_by_stages_kanban(CommonStrings.api_status_failure, "Authorization missing");
        }
        int bukrs = authService.get_bukrs_token(token);
        if (bukrs == 0) {
            return new struct_response_leads_by_stages_kanban(CommonStrings.api_status_failure, "Invalid authorization token");
        }

        if (fromDate.length() > 0 && toDate.length() > 0) {
            if (!cf.isDateFormatted(CommonStrings.dateformat_hyphen_yyyyMMdd, fromDate)) {
                return new struct_response_leads_by_stages_kanban(CommonStrings.api_status_failure, "Invalid From-Date format ! Valid From-Date format is yyyy-MM-dd");
            }
            if (!cf.isDateFormatted(CommonStrings.dateformat_hyphen_yyyyMMdd, toDate)) {
                return new struct_response_leads_by_stages_kanban(CommonStrings.api_status_failure, "Invalid To-Date format ! Valid To-Date format is yyyy-MM-dd");
            }
            /*if (!cf.isToDateGreaterThanFromDate(fromDate, toDate, CommonStrings.dateformat_hyphen_yyyyMMdd)) {
                return new struct_response_leads_by_stages_kanban(CommonStrings.api_status_failure, "From-Date must occur before To-Date");
            }*/
        }
        //System.out.println("token1 = " + token);
        return leadsService.getLeadsByStages(bukrs, fromDate, toDate, token);
    }




}
