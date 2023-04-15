package com.zenscale.zencrm_2.service;

import com.zenscale.zencrm_2.entity.*;
import com.zenscale.zencrm_2.repo.RepoAlertsAssign;
import com.zenscale.zencrm_2.repo.RepoAlertsH;
import com.zenscale.zencrm_2.repo.RepoAlertsI;
import com.zenscale.zencrm_2.scaledb.AlertsAb;
import com.zenscale.zencrm_2.structures.*;
import com.zenscale.zencrm_2.utils.CommonFunctions;
import com.zenscale.zencrm_2.utils.CommonStrings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AlertsService {

    @Autowired
    AlertsAb alertsAb;

    @Autowired
    RepoAlertsH repoAlertsH;

    @Autowired
    RepoAlertsI repoAlertsI;

    @Autowired
    RepoAlertsAssign repoAlertsAssign;

    @Autowired
    LeadCommentsService leadCommentsService;

    CommonFunctions cf = new CommonFunctions();




    public struct_response_create_alerts createAlert(int bukrs, struct_body_create_body_alerts body) {

        int maxAlertId = repoAlertsH.getMaxAlertId(bukrs) + 1;
        AlertsH alertsH = new AlertsH();
        alertsH.setId(new AlertsHId(bukrs, maxAlertId));
        alertsH.setDocId(body.getDocId());
        alertsH.setDocType(body.getDocType());
        alertsH.setCreby(body.getCreby());
        repoAlertsH.save(alertsH);

        int maxCounter = repoAlertsI.getMaxAlertICounter(bukrs, maxAlertId) + 1;
        AlertsI alertsI = new AlertsI();
        alertsI.setId(new AlertsIId(bukrs, maxAlertId, maxCounter));
        alertsI.setExecutionTime(cf.getTimestampFromDateString(CommonStrings.dateformat_hyphen_yyyyMMdd_HHmmss, body.getExecutionTime()));
        alertsI.setExecutionTimeStamp(body.getExecutionTimeStamp());
        alertsI.setStatus(CommonStrings.alerts_status_active);
        repoAlertsI.save(alertsI);

        return new struct_response_create_alerts(CommonStrings.api_status_success, "Alert created successfully", maxAlertId, maxCounter);
    }




    public struct_response_alerts_read readAlerts(int bukrs, int alertId, int alertCounter) {

        List<struct_alerts> list = alertsAb.findAllAlerts(bukrs, alertId, alertCounter);
        List<Integer> listDistinctAlertId = findDistinctAlertIds(list);

        List<struct_alerts_list> alertsLists = new ArrayList<>();
        for (Integer s : listDistinctAlertId) {
            struct_alerts_list item = new struct_alerts_list();
            for (struct_alerts a : list) {
                if (s == a.getAlertId()) {
                    item.setAlertId(a.getAlertId());
                    item.setDocId(a.getDocId());
                    item.setDocType(a.getDocType());
                    item.setCreon(a.getCreon());
                    item.setLeadId(a.getLeadId());
                    item.setLeadDesc(a.getLeadDesc());
                    item.setStageId(a.getStageId());
                    item.setStageDesc(a.getStageDesc());
                    item.setComment(a.getComments());
                    struct_executions executions = new struct_executions();
                    executions.setCounter(a.getCounter());
                    executions.setExecutionTime(a.getExecutionTime());
                    executions.setStatus(a.getStatus());
                    executions.setCreby(a.getCreby());
                    item.getAlertExecutions().add(executions);
                }
            }
            alertsLists.add(item);
        }

        return new struct_response_alerts_read(1, "Alerts retrieved successfully", alertsLists);
    }




    public void inactiveAlerts(int bukrs, struct_body_inactive_alerts body) {

        repoAlertsI.updateAlertStatus(bukrs, body.getAlertId(), body.getCounter(), CommonStrings.alerts_status_inactive);
    }




    public List<Integer> findDistinctAlertIds(List<struct_alerts> list) {

        List<Integer> alerts = list.stream()
                .map(struct_alerts::getAlertId)
                .distinct()
                .collect(Collectors.toList());
        return alerts;
    }




    public boolean validateDoctype(String doctype) {

        List<String> list = new ArrayList<>();
        list.add("CM");
        if (list.contains(doctype)) {
            return true;
        }
        return false;
    }




    public boolean isDocIdExists(int bukrs, int docid, String doctype) {

        boolean isExists = false;
        switch (doctype) {
            case "CM":
                if (leadCommentsService.isCommentExists(bukrs, docid)) {
                    isExists = true;
                }
                break;
        }
        return isExists;
    }




    public boolean isValidAlertStatus(String status) {

        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("I");
        if (list.contains(status)) {
            return true;
        }
        return false;
    }




    /*public struct_response_create_alerts validateExecutionTime(List<String> list) {

        if (list == null) {
            return new struct_response_create_alerts(CommonStrings.api_status_failure, "Execution Time List missing");
        }

        if (list.size() == 0) {
            return new struct_response_create_alerts(CommonStrings.api_status_failure, "Execution Time List is empty");
        }

        for (String s : list) {

        }
        return null;
    }*/




    public boolean isAlertHExists(int bukrs, int alertId) {

        return repoAlertsH.existsById(new AlertsHId(bukrs, alertId));
    }




    public boolean isAlertIExists(int bukrs, int alertId, int counter) {

        return repoAlertsI.existsById_BukrsAndId_AlertIdAndId_Counter(bukrs, alertId, counter);
    }




    public struct_response_alerts_status changeAlertStatus(int bukrs, struct_body_inactive_alerts body) {
        if (body.getStatus().equals(CommonStrings.alerts_status_active)) {
            repoAlertsI.updateAlertStatus(bukrs, body.getAlertId(), body.getCounter(), CommonStrings.alerts_status_active);
            return new struct_response_alerts_status(CommonStrings.api_status_success, "Alert is active");
        }
        repoAlertsI.updateAlertStatus(bukrs, body.getAlertId(), body.getCounter(), CommonStrings.alerts_status_inactive);
        return new struct_response_alerts_status(CommonStrings.api_status_success, "Alert is inactive");
    }




    public struct_response_assign_alerts assignAlert(int bukrs, struct_body_assign_alerts body) {
        AlertsAssign alertsAssign = new AlertsAssign();
        alertsAssign.setId(new AlertsAssignId(bukrs, body.getAlertId(), body.getAlertCounter()));
        alertsAssign.setUid(body.getUid());
        repoAlertsAssign.save(alertsAssign);
        return new struct_response_assign_alerts(CommonStrings.api_status_success, "ALert successfully assigned");
    }




    public List<struct_email_alert> getAlertListForEmail(Long currentMillis) {
        return alertsAb.getAlertListForEmail(currentMillis);
    }


}
