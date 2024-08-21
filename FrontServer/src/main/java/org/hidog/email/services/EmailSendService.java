package org.hidog.email.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EmailSendService {
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    public boolean sendMail(EmailMessage message, String tpl, Map<String, Object> tplData) {
        String text = null;

        if (StringUtils.hasText(tpl)) {
            tplData = Objects.requireNonNullElse(tplData, new HashMap<>());
            Context context = new Context();

            tplData.put("to", message.to());
            tplData.put("subject", message.subject());
            tplData.put("message", message.message());

            context.setVariables(tplData);

            text = templateEngine.process("email/" + tpl, context);
        } else {
            text = message.message();
        }

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(message.to());
            mimeMessageHelper.setSubject(message.subject());
            mimeMessageHelper.setText(text, true);
            javaMailSender.send(mimeMessage);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return false;

    }

    public boolean sendMail(EmailMessage message) {
        return sendMail(message, null, null);
    }
}