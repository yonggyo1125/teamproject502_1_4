package org.hidog.global.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class CommonInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        checkDevice(request);

        return true;
    }

    /**
     * PC와 MOBILE 수동 변환
     * ?device=MOBILE
     * ?device=PC
     * @param request
     */
    private void checkDevice(HttpServletRequest request) {
        String device = request.getParameter("device");
        if(!StringUtils.hasText(device)){
            return;
        }

        device = device.toUpperCase().equals("MOBILE") ? "MOBILE" : "PC";

        HttpSession session = request.getSession();
        session.setAttribute("device", device);
    }
}
