package com.zenscale.zencrm_2.service;

import com.zenscale.zencrm_2.structures.struct_lead_comment;
import com.zenscale.zencrm_2.structures.struct_res;
import com.zenscale.zencrm_2.utils.CommonStrings;
import com.zenscale.zencrm_2.utils.EmailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmailService {

    @Autowired
    TemplateEngine templateEngine;

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    EmailUtils emailUtils;




    public struct_res sendEmailAlerts(struct_lead_comment body, String email) {
        List<String> toEmails = new ArrayList<>();
        toEmails.add(email);

        Context context = new Context();
        context.setVariable("data", body);

        String process = templateEngine.process(CommonStrings.email_template_alerts, context);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        emailUtils.setMessageHelper(mimeMessage, CommonStrings.email_template_alerts_subject, process, null, toEmails);

        try {
            javaMailSender.send(mimeMessage);
        } catch (MailException e) {
            e.printStackTrace();
            return new struct_res(-1, e.getMessage());
        } finally {
            return new struct_res(1, "Email sent successfully");
        }
    }





}
