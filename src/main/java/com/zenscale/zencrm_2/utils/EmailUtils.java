package com.zenscale.zencrm_2.utils;

import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class EmailUtils {


    public MimeMessageHelper setMessageHelper(MimeMessage mimeMessage, String Subject, String process, ArrayList<File> attachments, List<String> tolist) {

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
            helper.setSubject(Subject);
            helper.setText(process, true);

            if (tolist != null && tolist.size() > 0) {
                helper.setTo(tolist.toArray(new String[0]));
            }

            helper.setFrom("support@zenscale.in");
            helper.setSentDate(new Date());

            if (attachments != null) {
                for (File fx : attachments) {
                    FileSystemResource file1 = new FileSystemResource(new File(fx.getPath()));
                    helper.addAttachment(fx.getName(), file1);
                }
            }

            return helper;
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }




}
