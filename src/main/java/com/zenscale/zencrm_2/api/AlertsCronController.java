package com.zenscale.zencrm_2.api;

import com.zenscale.zencrm_2.service.AlertCronService;
import com.zenscale.zencrm_2.service.AlertsService;
import com.zenscale.zencrm_2.service.EmailService;
import com.zenscale.zencrm_2.service.LeadCommentsService;
import com.zenscale.zencrm_2.structures.struct_body_inactive_alerts;
import com.zenscale.zencrm_2.structures.struct_email_alert;
import com.zenscale.zencrm_2.structures.struct_lead_comment;
import com.zenscale.zencrm_2.structures.struct_res;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AlertsCronController {


    @Autowired
    EmailService emailService;

    @Autowired
    AlertCronService alertCronService;

    @Autowired
    AlertsService alertsService;




    @Scheduled(cron = "0 0/1 * * * ?") // Every 1 Minutes for Order
    public void scheduleEvery5MinuteAlerts() {

        List<struct_email_alert> listALerts = alertCronService.getListALerts();
        for (struct_email_alert item : listALerts) {
            switch (item.getDocType()) {
                case "CM":
                    // TODO - Getting comment details...
                    struct_lead_comment commentDetails = alertCronService.getCommentDetails(item.getBukrs(), item.getDocId());
                    //System.out.println("commentDetails = " + commentDetails);
                    if (commentDetails != null) {
                        // TODO - Sending email...
                        String emailTo = alertCronService.getEmailByUid(item.getUid());
                        struct_res emailResponse = emailService.sendEmailAlerts(commentDetails, emailTo);
                        //System.out.println("emailResponse = " + emailResponse);
                        // TODO - Changing status of alert to INACTIVE...
                        if (emailResponse.getStatus() == 1) {
                            struct_body_inactive_alerts bodyInactiveAlerts = new struct_body_inactive_alerts();
                            bodyInactiveAlerts.setAlertId(item.getAlertId());
                            bodyInactiveAlerts.setCounter(item.getCounter());
                            bodyInactiveAlerts.setStatus("I");
                            alertsService.changeAlertStatus(item.getBukrs(), bodyInactiveAlerts);
                        }
                    }
                    break;
            }
        }
    }




}
