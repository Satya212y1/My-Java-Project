package com.zenscale.zencrm_2.api;

import com.zenscale.zencrm_2.service.AlertsService;
import com.zenscale.zencrm_2.service.AuthService;
import com.zenscale.zencrm_2.structures.*;
import com.zenscale.zencrm_2.utils.CommonFunctions;
import com.zenscale.zencrm_2.utils.CommonStrings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/crm/alerts/")
public class AlertsController {

    @Autowired
    AuthService authService;

    @Autowired
    AlertsService alertsService;

    CommonFunctions cf = new CommonFunctions();




    @PostMapping("create")
    public struct_response_create_alerts createAlerts(@RequestHeader(name = "Authorization", required = false, defaultValue = "") String token,
                                                      @RequestBody(required = false) struct_body_create_body_alerts body) {

        if (token.length() == 0) {
            return new struct_response_create_alerts(CommonStrings.api_status_failure, "Authorization missing");
        }
        int bukrs = authService.get_bukrs_token(token);
        if (bukrs == 0) {
            return new struct_response_create_alerts(CommonStrings.api_status_failure, "Invalid authorization token");
        }

        if (body == null) {
            return new struct_response_create_alerts(CommonStrings.api_status_failure, "Body params missing");
        }

        if (body.getDocId() == 0) {
            return new struct_response_create_alerts(CommonStrings.api_status_failure, "DocId missing");
        }

        if (cf.checkNullString(body.getDocType()).length() == 0) {
            return new struct_response_create_alerts(CommonStrings.api_status_failure, "Doc Type missing");
        }

        if (!alertsService.validateDoctype(body.getDocType())) {
            return new struct_response_create_alerts(CommonStrings.api_status_failure, "Invalid doctype ! Valid doctype's are 'CM' for Comments");
        }

        /*if (!alertsService.isDocIdExists(bukrs, body.getDocId(), body.getDocType())) {
            return new struct_response_create_alerts(CommonStrings.api_status_failure, "DocId does not exists for Doctype : " + body.getDocType() + "");
        }*/

        if (cf.checkNullString(body.getCreby()).length() == 0) {
            return new struct_response_create_alerts(CommonStrings.api_status_failure, "Creby missing");
        }

        if (cf.checkNullString(body.getExecutionTime()).length() == 0) {
            return new struct_response_create_alerts(CommonStrings.api_status_failure, "Execution Time missing");
        }

        if (cf.isDateFormatted(CommonStrings.dateformat_hyphen_yyyyMMdd_HHmmss, body.getExecutionTime()) == false) {
            return new struct_response_create_alerts(CommonStrings.api_status_failure, "Invalid ExecutionTime format ! Posting date format should be yyyy-MM-dd HH:mm:ss");
        }

        return alertsService.createAlert(bukrs, body);
    }




    @PostMapping("assign")
    public struct_response_assign_alerts assignAlerts(@RequestHeader(name = "Authorization", required = false, defaultValue = "") String token,
                                                      @RequestBody(required = false) struct_body_assign_alerts body) {

        if (token.length() == 0) {
            return new struct_response_assign_alerts(CommonStrings.api_status_failure, "Authorization missing");
        }
        int bukrs = authService.get_bukrs_token(token);
        if (bukrs == 0) {
            return new struct_response_assign_alerts(CommonStrings.api_status_failure, "Invalid authorization token");
        }
        if (body == null) {
            return new struct_response_assign_alerts(CommonStrings.api_status_failure, "Body params missing");
        }
        if (body.getAlertId() == 0) {
            return new struct_response_assign_alerts(CommonStrings.api_status_failure, "AlertId missing");
        }
        if (body.getAlertCounter() == 0) {
            return new struct_response_assign_alerts(CommonStrings.api_status_failure, "Alert counter missing");
        }
        if (cf.checkNullString(body.getUid()).length() == 0) {
            return new struct_response_assign_alerts(CommonStrings.api_status_failure, "UID missing");
        }
        if (!alertsService.isAlertHExists(bukrs, body.getAlertId())) {
            return new struct_response_assign_alerts(CommonStrings.api_status_failure, "Alert does not exists by this alert id");
        }
        if (!alertsService.isAlertIExists(bukrs, body.getAlertId(), body.getAlertCounter())) {
            return new struct_response_assign_alerts(CommonStrings.api_status_failure, "Alert does not exists by this alert id & alert counter");
        }
        return alertsService.assignAlert(bukrs, body);
    }




    @GetMapping("read")
    public struct_response_alerts_read alertsRead(@RequestHeader(name = "Authorization", required = false, defaultValue = "") String token,
                                                  @RequestParam(name = "alertId", required = false, defaultValue = "0") int alertId,
                                                  @RequestParam(name = "alertCounter", required = false, defaultValue = "0") int alertCounter) {

        if (token.length() == 0) {
            return new struct_response_alerts_read(CommonStrings.api_status_failure, "Authorization missing");
        }
        int bukrs = authService.get_bukrs_token(token);
        if (bukrs == 0) {
            return new struct_response_alerts_read(CommonStrings.api_status_failure, "Invalid authorization token");
        }

        return alertsService.readAlerts(bukrs, alertId, alertCounter);
    }




    @PostMapping("change_alert_status")
    public struct_response_alerts_status alertsStatus(@RequestHeader(name = "Authorization", required = false, defaultValue = "") String token,
                                                      @RequestBody(required = false) struct_body_inactive_alerts body) {

        if (token.length() == 0) {
            return new struct_response_alerts_status(CommonStrings.api_status_failure, "Authorization missing");
        }
        int bukrs = authService.get_bukrs_token(token);
        if (bukrs == 0) {
            return new struct_response_alerts_status(CommonStrings.api_status_failure, "Invalid authorization token");
        }

        if (body == null) {
            return new struct_response_alerts_status(CommonStrings.api_status_failure, "Body params missing");
        }

        if (body.getAlertId() == 0) {
            return new struct_response_alerts_status(CommonStrings.api_status_failure, "AlertId missing");
        }

        if (body.getCounter() == 0) {
            return new struct_response_alerts_status(CommonStrings.api_status_failure, "Counter missing");
        }

        if (cf.checkNullString(body.getStatus()).length() == 0) {
            return new struct_response_alerts_status(CommonStrings.api_status_failure, "Status missing");
        }

        if (!alertsService.isValidAlertStatus(body.getStatus())) {
            return new struct_response_alerts_status(CommonStrings.api_status_failure, "Invalid status ! Valid status's are 'A' for Active & 'I' for Inactive");
        }

        if (!alertsService.isAlertIExists(bukrs, body.getAlertId(), body.getCounter())) {
            return new struct_response_alerts_status(CommonStrings.api_status_failure, "Record does not exists");
        }

        return alertsService.changeAlertStatus(bukrs, body);
    }




}
