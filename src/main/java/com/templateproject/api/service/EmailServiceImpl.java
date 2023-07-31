package com.templateproject.api.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service("emailSenderService")
public class EmailServiceImpl implements EmailSenderService{

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendEmail(
            String name,
            String fromAddress,
            String toAddress,
            String body
    ) throws Exception {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom(fromAddress);
        mimeMessageHelper.setTo(toAddress);
        mimeMessageHelper.setText(body, true);
        mimeMessageHelper.setSubject("Formulaire Brainwave : nouveau message de " + name);

        javaMailSender.send(mimeMessage);
    }
}
