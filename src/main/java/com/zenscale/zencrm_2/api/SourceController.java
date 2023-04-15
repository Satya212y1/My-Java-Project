package com.zenscale.zencrm_2.api;

import com.zenscale.zencrm_2.entity.SourceM;
import com.zenscale.zencrm_2.service.*;
import com.zenscale.zencrm_2.structures.*;
import com.zenscale.zencrm_2.utils.CommonFunctions;
import com.zenscale.zencrm_2.utils.CommonStrings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/crm/source/")
public class SourceController {

    @Autowired
    SourceService sourceService;

    @Autowired
    AuthService authService;

    CommonFunctions cf = new CommonFunctions();


    @Autowired
    EmailService emailService;

    @Autowired
    AlertCronService alertCronService;

    @Autowired
    AlertsService alertsService;


    @PostMapping("create_update")
    public struct_response_create_source createSource(@RequestHeader(name = "Authorization", required = false, defaultValue = "") String token,
                                                      @RequestBody(required = false) struct_body_create_source body) {

        if (token.equals("")) {
            return new struct_response_create_source(CommonStrings.api_status_failure, "Authorization missing");
        }

        int bukrs = authService.get_bukrs_token(token);
        if (bukrs == 0) {
            return new struct_response_create_source(CommonStrings.api_status_failure, "Invalid Authorization token");
        }

        if (body == null) {
            return new struct_response_create_source(CommonStrings.api_status_failure, "Body params missing");
        }

        if (cf.checkNullString(body.getDescr()).length() == 0) {
            return new struct_response_create_source(CommonStrings.api_status_failure, "Source description missing");
        }

        if (body.getDescr().length() > 100) {
            return new struct_response_create_source(CommonStrings.api_status_failure, "Source description cannot have more than 100 characters");
        }

        if (body.getSourceId() > 0) {
            if (!sourceService.isSourceExist(bukrs, body.getSourceId())) {
                return new struct_response_create_source(CommonStrings.api_status_failure, "Source does exist by this Sourceid");
            }
        }

        SourceM sourceM = sourceService.checkSourceDescExist(bukrs, body.getDescr());
        if (sourceM != null && sourceM.getId().getSourceId() > 0) {
            if (body.getDescr().equals(sourceM.getDescr()) && body.getSourceId() != sourceM.getId().getSourceId()) {
                return new struct_response_create_source(CommonStrings.api_status_failure, "Source description already exist with Source id = " + sourceM.getId().getSourceId());
            } else if (body.getDescr().equals(sourceM.getDescr()) && body.getSourceId() == sourceM.getId().getSourceId()) {
                return new struct_response_create_source(CommonStrings.api_status_failure, "No changes found");
            }
        }

        return sourceService.createSource(bukrs, body);
    }




    @GetMapping("read")
    public struct_response_read_source readSource(@RequestHeader(name = "Authorization", required = false, defaultValue = "") String token,
                                                  @RequestParam(name = "sourceid", required = false, defaultValue = "0") int sourceid,
                                                  @RequestParam(name = "search", required = false, defaultValue = "") String search) {

        if (token.equals("")) {
            return new struct_response_read_source(CommonStrings.api_status_failure, "Authorization missing");
        }

        int bukrs = authService.get_bukrs_token(token);
        if (bukrs == 0) {
            return new struct_response_read_source(CommonStrings.api_status_failure, "Invalid Authorization token");
        }

        if (sourceid > 0) {
            if (!sourceService.isSourceExist(bukrs, sourceid)) {
                return new struct_response_read_source(CommonStrings.api_status_failure, "Source does exist by this stageid");
            }
        }

        return sourceService.readSources(bukrs, sourceid, search);
    }




    @DeleteMapping("delete")
    public struct_response_delete_source deleteSource(@RequestHeader(name = "Authorization", required = false, defaultValue = "") String token,
                                                      @RequestParam(name = "sourceid", required = false, defaultValue = "0") int sourceid) {

        if (token.equals("")) {
            return new struct_response_delete_source(CommonStrings.api_status_failure, "Authorization missing");
        }

        int bukrs = authService.get_bukrs_token(token);
        if (bukrs == 0) {
            return new struct_response_delete_source(CommonStrings.api_status_failure, "Invalid Authorization token");
        }

        if (sourceid > 0) {
            if (!sourceService.isSourceExist(bukrs, sourceid)) {
                return new struct_response_delete_source(CommonStrings.api_status_failure, "Source does exist by this stageid");
            }
        }

        return sourceService.deleteSource(bukrs, sourceid);
    }



}
