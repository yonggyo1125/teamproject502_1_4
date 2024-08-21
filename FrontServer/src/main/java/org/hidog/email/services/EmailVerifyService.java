package org.hidog.email.services;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.hidog.global.Utils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailVerifyService {
    private final EmailSendService sendService;
    private final HttpSession session;

    public boolean sendCode(String email) {
        int authNum = (int)(Math.random() * 900000) + 100000;

        session.setAttribute("EmailAuthNum", authNum);
        session.setAttribute("EmailAuthStart", System.currentTimeMillis());

        EmailMessage emailMessage = new EmailMessage(
                email,
                Utils.getMessage("Email.verification.subject", "commons"),
                Utils.getMessage("Email.verification.message", "commons"));
        Map<String, Object> tplData = new HashMap<>();
        tplData.put("authNum", authNum);

        return sendService.sendMail(emailMessage, "auth", tplData);
    }

    public boolean check(int code) {

        Integer authNum = (Integer)session.getAttribute("EmailAuthNum");
        Long stime = (Long)session.getAttribute("EmailAuthStart");
        if (authNum != null && stime != null) {

            boolean isExpired = (System.currentTimeMillis() - stime.longValue()) > 1000 * 60 * 3;
            if (isExpired) {
                session.removeAttribute("EmailAuthNum");
                session.removeAttribute("EmailAuthStart");

                return false;
            }

            boolean isVerified = code == authNum.intValue();
            session.setAttribute("EmailAuthVerified", isVerified);

            return isVerified;
        }

        return false;
    }
}
