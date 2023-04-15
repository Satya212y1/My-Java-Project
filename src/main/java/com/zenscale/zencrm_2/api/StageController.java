package com.zenscale.zencrm_2.api;


import com.zenscale.zencrm_2.entity.StagesM;
import com.zenscale.zencrm_2.service.AuthService;
import com.zenscale.zencrm_2.service.StageService;
import com.zenscale.zencrm_2.structures.struct_body_create_stage;
import com.zenscale.zencrm_2.structures.struct_response_create_stage;
import com.zenscale.zencrm_2.structures.struct_response_delete_stage;
import com.zenscale.zencrm_2.structures.struct_response_read_stage;
import com.zenscale.zencrm_2.utils.CommonFunctions;
import com.zenscale.zencrm_2.utils.CommonStrings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/crm/stages/")
public class StageController {

    @Autowired
    AuthService authService;

    @Autowired
    StageService stageService;

    CommonFunctions cf = new CommonFunctions();




    @PostMapping("create_update")
    public struct_response_create_stage createStage(@RequestHeader(name = "Authorization", required = false, defaultValue = "") String token,
                                                    @RequestBody(required = false) struct_body_create_stage body) {

        if (token.equals("")) {
            return new struct_response_create_stage(CommonStrings.api_status_failure, "Authorization missing");
        }

        int bukrs = authService.get_bukrs_token(token);
        if (bukrs == 0) {
            return new struct_response_create_stage(CommonStrings.api_status_failure, "Invalid Authorization token");
        }

        if (body == null) {
            return new struct_response_create_stage(CommonStrings.api_status_failure, "Body params missing");
        }

        if (cf.checkNullString(body.getDescr()).length() == 0) {
            return new struct_response_create_stage(CommonStrings.api_status_failure, "Stage description missing");
        }

        if (body.getDescr().length() > 100) {
            return new struct_response_create_stage(CommonStrings.api_status_failure, "Stage description cannot have more than 100 characters");
        }

        if (cf.checkNullString(body.getStatus()).length() == 0) {
            return new struct_response_create_stage(CommonStrings.api_status_failure, "Status missing");
        }

        if (!stageService.validateStageStatus(body.getStatus())) {
            return new struct_response_create_stage(CommonStrings.api_status_failure, "Invalid status ! Valid status are 'O' for Open & 'C' for Closed");
        }

        if (body.getStageId() > 0) {
            if (!stageService.isStageExist(bukrs, body.getStageId())) {
                return new struct_response_create_stage(CommonStrings.api_status_failure, "Stage does exist by this stageid");
            }
        }

        StagesM stagesM = stageService.checkStageDescExist(bukrs, body.getDescr());
        if (stagesM != null && stagesM.getId().getStageId() > 0) {
            if (body.getDescr().equals(stagesM.getDescr()) && body.getStageId() != stagesM.getId().getStageId()) {
                return new struct_response_create_stage(CommonStrings.api_status_failure, "Stage description already exist with stage id = " + stagesM.getId().getStageId());
            } else if (body.getDescr().equals(stagesM.getDescr())
                    && body.getStageId() == stagesM.getId().getStageId()
                    && cf.checkNullString(body.getColorCode()).equals(cf.checkNullString(stagesM.getColorCode()))
                    && stagesM.getStatus().equals(body.getStatus())) {
                return new struct_response_create_stage(CommonStrings.api_status_failure, "No changes found");
            }
        }

        return stageService.createStage(bukrs, body);
    }




    @GetMapping("read")
    public struct_response_read_stage readStage(@RequestHeader(name = "Authorization", required = false, defaultValue = "") String token,
                                                @RequestParam(name = "stageid", required = false, defaultValue = "0") int stageid,
                                                @RequestParam(name = "search", required = false, defaultValue = "") String search) {

        if (token.equals("")) {
            return new struct_response_read_stage(CommonStrings.api_status_failure, "Authorization missing");
        }

        int bukrs = authService.get_bukrs_token(token);
        if (bukrs == 0) {
            return new struct_response_read_stage(CommonStrings.api_status_failure, "Invalid Authorization token");
        }

        if (stageid > 0) {
            if (!stageService.isStageExist(bukrs, stageid)) {
                return new struct_response_read_stage(CommonStrings.api_status_failure, "Stage does exist by this stageid");
            }
        }

        return stageService.readStages(bukrs, stageid, search);
    }




    @DeleteMapping("delete")
    public struct_response_delete_stage deleteStage(@RequestHeader(name = "Authorization", required = false, defaultValue = "") String token,
                                                    @RequestParam(name = "stageid", required = false, defaultValue = "0") int stageid) {

        if (token.equals("")) {
            return new struct_response_delete_stage(CommonStrings.api_status_failure, "Authorization missing");
        }

        int bukrs = authService.get_bukrs_token(token);
        if (bukrs == 0) {
            return new struct_response_delete_stage(CommonStrings.api_status_failure, "Invalid Authorization token");
        }

        if (stageid > 0) {
            if (!stageService.isStageExist(bukrs, stageid)) {
                return new struct_response_delete_stage(CommonStrings.api_status_failure, "Stage does exist by this stageid");
            }
        }

        return stageService.deleteStages(bukrs, stageid);
    }




}
