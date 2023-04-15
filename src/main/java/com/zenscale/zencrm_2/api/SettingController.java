package com.zenscale.zencrm_2.api;

import com.zenscale.zencrm_2.service.AuthService;
import com.zenscale.zencrm_2.service.StngService;
import com.zenscale.zencrm_2.structures.struct_response;
import com.zenscale.zencrm_2.structures.struct_stng;
import com.zenscale.zencrm_2.structures.struct_stng_response;
import com.zenscale.zencrm_2.structures.struct_stngs;
import com.zenscale.zencrm_2.utils.CommonStrings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/crm/setting/")
public class SettingController {


    @Autowired
    AuthService authService;

    @Autowired
    StngService stngService;




    @GetMapping("/get_single")
    public struct_stng_response getdefaultStng(@RequestHeader(name = "Authorization", required = false) String token,
                                               @RequestParam(required = false) String stngid) {

        if (token == null || token.length() <= 0) {
            return new struct_stng_response(CommonStrings.api_status_failure, "Authorization Missing");
        }
        int bukrs = authService.get_bukrs_token(token);
        if (bukrs <= 0) {
            return new struct_stng_response(CommonStrings.api_status_failure, "Invalid Token");
        }
        if (stngid == null || stngid.length() <= 0) {
            return new struct_stng_response(CommonStrings.api_status_failure, "Missing Setting Id");
        }
        struct_stngs s = stngService.get_stngs(bukrs, stngid);
        if (s == null) {
            return new struct_stng_response(CommonStrings.api_status_failure, "No Record Exist", null);
        }
        List<struct_stngs> slist = new ArrayList<>();
        slist.add(s);

        return new struct_stng_response(1, "Retrieved Successfully", slist);
    }




    @GetMapping("/get_all")
    public struct_stng_response getAllStng(@RequestHeader(name = "Authorization", required = false) String token,
                                           @RequestParam(name = "type", required = false, defaultValue = "") String type,
                                           @RequestParam(name = "parent_stng_id", required = false, defaultValue = "") String parent_stng_id) {

        if (token == null || token.length() <= 0) {
            return new struct_stng_response(CommonStrings.api_status_failure, "Authorization Missing");
        }

        int bukrs = authService.get_bukrs_token(token);

        if (bukrs <= 0) {
            return new struct_stng_response(CommonStrings.api_status_failure, "Invalid Token");
        }

        if (!type.equals("") && !parent_stng_id.equals("")) {
            return new struct_stng_response(CommonStrings.api_status_failure, "Specify either type or parent_stng id or send both empty");
        }

        if (type.length() > 0) {
            if (!type.equals("M")) {
                return new struct_stng_response(CommonStrings.api_status_failure, "Invalid type");
            }
        }

        if (type.length() > 0) {
            if (!parent_stng_id.equals("PLNRULES")) {
                return new struct_stng_response(CommonStrings.api_status_failure, "Invalid parent stng id");
            }
        }

        List<struct_stngs> sList = stngService.get_all_stngs(bukrs, type, parent_stng_id);

        return new struct_stng_response(1, "Retrieved Successfully", sList);
    }




    @PostMapping("/save")
    public struct_response saveStng(@RequestHeader(name = "Authorization", required = false) String token,
                                    @RequestBody(required = false) struct_stng body) {

        if (token == null || token.length() <= 0) {
            return new struct_response(CommonStrings.api_status_failure, "Authorization Missing");
        }

        int bukrs = authService.get_bukrs_token(token);

        if (bukrs <= 0) {
            return new struct_response(CommonStrings.api_status_failure, "Invalid Token");
        }

        if (body == null) {
            return new struct_response(CommonStrings.api_status_failure, "Missing Body");
        }

        if (body.getStngid() == null || body.getStngid().length() <= 0) {
            return new struct_response(CommonStrings.api_status_failure, "Missing Setting Id");
        }

        if (body.getStatus() == null || body.getStatus().length() != 1) {
            return new struct_response(CommonStrings.api_status_failure, "Invalid Status");
        }

        int exist = stngService.checkSettingId(body.getStngid());
        if (exist <= 0) {
            return new struct_response(CommonStrings.api_status_failure, "Invalid Setting Id");
        }

        if (body.getStngid().equals("ZENSCALE") && stngService.checkJobOrderDataExist(bukrs) && body.getStatus().equals("I")) {
            return new struct_response(CommonStrings.api_status_failure, "Deactivation not allowed , Data Synchronization started.");
        }

        struct_stngs s = new struct_stngs();
        s.setStngId(body.getStngid());
        s.setStatus(body.getStatus());
        stngService.save_stng(bukrs, s);

        return new struct_response(1, "Setting Saved Successfully");
    }




}
