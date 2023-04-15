package com.zenscale.zencrm_2.service;

import com.zenscale.zencrm_2.structures.struct_email_alert;
import com.zenscale.zencrm_2.structures.struct_lead_comment;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertCronService {

    @Autowired
    AlertsService alertsService;

    @Autowired
    LeadCommentsService leadCommentsService;

    @Autowired
    RegistrationService registrationService;




    public List<struct_email_alert> getListALerts() {
        Long currentMillis = System.currentTimeMillis();
        List<struct_email_alert> listALerts = alertsService.getAlertListForEmail(currentMillis);
        return listALerts;
    }




    public struct_lead_comment getCommentDetails(int bukrs, int commentId) {
        List<struct_lead_comment> list = leadCommentsService.leadCommentsDb.getRecords(bukrs, 0, 0, commentId, "");
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }




    public String getEmailByUid(String uid) {
        JSONObject jsonObject = registrationService.getUsersDetailByUid(uid);
        return jsonObject.getString("email");
    }




    public struct_lead_comment getLeadDetails(int bukrs, int leadId) {
        return null;
    }




}
