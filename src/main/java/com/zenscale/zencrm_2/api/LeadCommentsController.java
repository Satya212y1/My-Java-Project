package com.zenscale.zencrm_2.api;

import com.zenscale.zencrm_2.service.AuthService;
import com.zenscale.zencrm_2.service.LeadCommentsService;
import com.zenscale.zencrm_2.service.LeadsService;
import com.zenscale.zencrm_2.service.StageService;
import com.zenscale.zencrm_2.structures.*;
import com.zenscale.zencrm_2.utils.CommonFunctions;
import com.zenscale.zencrm_2.utils.CommonStrings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/crm/lead_comments/")
public class LeadCommentsController {

    @Autowired
    LeadCommentsService leadCommentsService;

    @Autowired
    AuthService authService;

    @Autowired
    LeadsService leadsService;

    @Autowired
    StageService stageService;

    CommonFunctions cf = new CommonFunctions();




    @PostMapping("create_update")
    public struct_response_create_lead_comment createLeadComment(@RequestHeader(name = "Authorization", required = false, defaultValue = "") String token,
                                                                 @RequestBody(required = false) struct_body_create_lead_comment body) {

        if (token.equals("")) {
            return new struct_response_create_lead_comment(CommonStrings.api_status_failure, "Authorization missing");
        }

        int bukrs = authService.get_bukrs_token(token);
        if (bukrs == 0) {
            return new struct_response_create_lead_comment(CommonStrings.api_status_failure, "Invalid Authorization token");
        }

        if (body == null) {
            return new struct_response_create_lead_comment(CommonStrings.api_status_failure, "Body params missing");
        }

        if (body.getLeadId() == 0) {
            return new struct_response_create_lead_comment(CommonStrings.api_status_failure, "Lead id missing");
        }

        if (body.getStageId() == 0) {
            return new struct_response_create_lead_comment(CommonStrings.api_status_failure, "Stage id missing");
        }

        if (cf.checkNullString(body.getComment()).length() == 0) {
            return new struct_response_create_lead_comment(CommonStrings.api_status_failure, "Comment missing");
        }

        if (!leadsService.isLeadExist(bukrs, body.getLeadId())) {
            return new struct_response_create_lead_comment(CommonStrings.api_status_failure, "Lead does not exists by this lead id");
        }

        if (!stageService.isStageExist(bukrs, body.getStageId())) {
            return new struct_response_create_lead_comment(CommonStrings.api_status_failure, "Stage does not exists by this stage id");
        }

        if (body.getCommentId() > 0) {
            if (!leadCommentsService.isCommentExists(bukrs, body.getCommentId())) {
                return new struct_response_create_lead_comment(CommonStrings.api_status_failure, "Comment does not exists by this id");
            }
        }

        return leadCommentsService.createLeadComment(bukrs, body);
    }




    @GetMapping("read")
    public struct_response_read_lead_comment readLeadComment(@RequestHeader(name = "Authorization", required = false, defaultValue = "") String token,
                                                             @RequestParam(name = "leadId", required = false, defaultValue = "0") int leadId,
                                                             @RequestParam(name = "stageId", required = false, defaultValue = "0") int stageId,
                                                             @RequestParam(name = "commentId", required = false, defaultValue = "0") int commentId,
                                                             @RequestParam(name = "search", required = false, defaultValue = "") String search) {

        if (token.equals("")) {
            return new struct_response_read_lead_comment(CommonStrings.api_status_failure, "Authorization missing");
        }

        int bukrs = authService.get_bukrs_token(token);
        if (bukrs == 0) {
            return new struct_response_read_lead_comment(CommonStrings.api_status_failure, "Invalid Authorization token");
        }

        if (leadId == 0) {
            return new struct_response_read_lead_comment(CommonStrings.api_status_failure, "Lead id missing");
        }

        /*if (stageId == 0) {
            return new struct_response_read_lead_comment(CommonStrings.api_status_failure, "Stage id missing");
        }*/

        if (!leadsService.isLeadExist(bukrs, leadId)) {
            return new struct_response_read_lead_comment(CommonStrings.api_status_failure, "Lead does not exists by this lead id");
        }

        if (stageId > 0) {
            if (!stageService.isStageExist(bukrs, stageId)) {
                return new struct_response_read_lead_comment(CommonStrings.api_status_failure, "Stage does not exists by this stage id");
            }
        }

        return leadCommentsService.readLeadComments(bukrs, leadId, stageId, commentId, search);
    }




}
