package com.zenscale.zencrm_2.api;

import com.zenscale.zencrm_2.service.AuthService;
import com.zenscale.zencrm_2.service.LeadStageAssignmentService;
import com.zenscale.zencrm_2.service.LeadsService;
import com.zenscale.zencrm_2.service.StageService;
import com.zenscale.zencrm_2.structures.*;
import com.zenscale.zencrm_2.utils.CommonStrings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/crm/lead_stage_assignment/")
public class LeadStageAssignmentController {

    @Autowired
    AuthService authService;

    @Autowired
    LeadStageAssignmentService leadStageAssignmentService;




    @PostMapping("create_update")
    public struct_response_lead_assignment leadAssignment(@RequestHeader(name = "Authorization", required = false, defaultValue = "") String token,
                                                          @RequestBody(required = false) struct_body_lead_assignment body) {

        if (token.equals("")) {
            return new struct_response_lead_assignment(CommonStrings.api_status_failure, "Authorization missing");
        }

        int bukrs = authService.get_bukrs_token(token);
        if (bukrs == 0) {
            return new struct_response_lead_assignment(CommonStrings.api_status_failure, "Invalid Authorization token");
        }

        if (body == null) {
            return new struct_response_lead_assignment(CommonStrings.api_status_failure, "Body params missing");
        }

        if (body.getStageId() == 0) {
            return new struct_response_lead_assignment(CommonStrings.api_status_failure, "Stage missing");
        }

        if (body.getLeadId() == 0) {
            return new struct_response_lead_assignment(CommonStrings.api_status_failure, "Lead missing");
        }

        if (!leadStageAssignmentService.leadsService.isLeadExist(bukrs, body.getLeadId())) {
            return new struct_response_lead_assignment(CommonStrings.api_status_failure, "Lead does not exists by this lead id");
        }

        if (!leadStageAssignmentService.stageService.isStageExist(bukrs, body.getStageId())) {
            return new struct_response_lead_assignment(CommonStrings.api_status_failure, "Stage does not exists by this stage id");
        }

        if (leadStageAssignmentService.isRecordExistByLeadIdAndStageId(bukrs, body.getLeadId(), body.getStageId())) {
            return new struct_response_lead_assignment(CommonStrings.api_status_failure, "No changes found !  Stage id : "
                    + body.getStageId() + " already assigned to Lead id : " + body.getLeadId());
        }

        return leadStageAssignmentService.createLeadAssignment(bukrs, body);
    }




    @GetMapping("read")
    public struct_response_lead_assignment_read leadRead(@RequestHeader(name = "Authorization", required = false, defaultValue = "") String token,
                                                         @RequestParam(name = "stageId", required = false, defaultValue = "0") int stageId,
                                                         @RequestParam(name = "leadId", required = false, defaultValue = "0") int leadId) {

        if (token.equals("")) {
            return new struct_response_lead_assignment_read(CommonStrings.api_status_failure, "Authorization missing");
        }

        int bukrs = authService.get_bukrs_token(token);
        if (bukrs == 0) {
            return new struct_response_lead_assignment_read(CommonStrings.api_status_failure, "Invalid Authorization token");
        }

        return leadStageAssignmentService.readLeadStageAssignment(bukrs, stageId, leadId);
    }




}
