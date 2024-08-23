package org.hidog.global;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.*;
import java.util.stream.Collectors;

@Component("utils")
@RequiredArgsConstructor
public class Utils { // 빈의 이름 - utils

    private final MessageSource messageSource;
    private final HttpServletRequest request;
    private final DiscoveryClient discoveryClient;

    private static final ResourceBundle commonsBundle;
    private static final ResourceBundle validationsBundle;
    private static final ResourceBundle errorsBundle;

    static {
        commonsBundle = ResourceBundle.getBundle("messages.commons");
        validationsBundle = ResourceBundle.getBundle("messages.validations");
        errorsBundle = ResourceBundle.getBundle("messages.errors");
    }

    public String url(String url) { //Admin서버의 정적자원 사용 할때
        List<ServiceInstance> instances = discoveryClient.getInstances("admin-service");

        try {
            return String.format("%s%s", instances.get(0).getUri().toString(), url);
        } catch (Exception e) {
            return String.format("%s://%s:%d%s%s", request.getScheme(), request.getServerName(), request.getServerPort(), request.getContextPath(), url);
        }
    }

    public String frontUrl(String url) {//Front서버의 정적자원 사용 할때
        List<ServiceInstance> instances = discoveryClient.getInstances("front-service");

        try {
            return String.format("%s%s", instances.get(0).getUri().toString(), url);
        } catch (Exception e) {
            return String.format("%s://%s:%d%s%s", request.getScheme(), request.getServerName(), request.getServerPort(), request.getContextPath(), url);
        }
    }

    public String redirectUrl(String url) { //게이트웨이를 통해 접속할 때 url주소
        String _fromGateway = Objects.requireNonNullElse(request.getHeader("from-gateway"), "false");
        String gatewayHost = Objects.requireNonNullElse(request.getHeader("gateway-host"), "");
        boolean fromGateway = _fromGateway.equals("true");

        return fromGateway ? request.getScheme() + "://" + gatewayHost + "/admin" + url : request.getContextPath() + url;
    }


    public Map<String, List<String>> getErrorMessages(Errors errors) {//JSON 받을 때는 에러를 직접 가공
        // FieldErrors


        Map<String, List<String>> messages = errors.getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, e -> getCodeMessages(e.getCodes())));

        // GlobalErrors
        List<String> gMessages = errors.getGlobalErrors()
                .stream()
                .flatMap(e -> getCodeMessages(e.getCodes()).stream()).toList();

        if (!gMessages.isEmpty()) {
            messages.put("global", gMessages);
        }
        return messages;
    }


    public List<String> getCodeMessages(String[] codes) {
        ResourceBundleMessageSource ms = (ResourceBundleMessageSource) messageSource;
        ms.setUseCodeAsDefaultMessage(false);

        List<String> messages = Arrays.stream(codes)
                .map(c -> {
                    try {
                        return ms.getMessage(c, null, request.getLocale());
                    } catch (Exception e) {
                        return "";
                    }
                })
                .filter(s -> !s.isBlank())
                .toList();

        ms.setUseCodeAsDefaultMessage(true);
        return messages;
    }


    public static String getMessage(String code, String type) {
        type = StringUtils.hasText(type) ? type : "validations";

        ResourceBundle bundle = null;
        if (type.equals("commons")) {
            bundle = commonsBundle;
        } else if (type.equals("errors")) {
            bundle = errorsBundle;
        } else {
            bundle = validationsBundle;
        }

        return bundle.getString(code);
    }

    public String getMessage(String code){
        List<String> messages = getCodeMessages(new String[]{code});

        return messages.isEmpty() ? code : messages.get(0);
    }

    /**
     * 요청 데이터 단일 조회 편의 함수
     *
     * @param name
     * @return
     */
    public String getParam(String name) {
        return request.getParameter(name);
    }

    /**
     * 요청 데이터 복수개 조회 편의 함수
     *
     * @param name
     * @return
     */
    public String[] getParams(String name) {
        return request.getParameterValues(name);
    }


    /**
     * 접속 장비가 모바일인지 체크
     *
     * @return
     */
    public boolean isMobile() {

        // 모바일 수동 전환 체크, 처리
        HttpSession session = request.getSession();
        String device = (String)session.getAttribute("device");

        if (StringUtils.hasText(device)) {
            return device.equals("MOBILE");
        }

        // User-Agent 요청 헤더 정보
        String ua = request.getHeader("User-Agent");

        String pattern = ".*(iPhone|iPod|iPad|BlackBerry|Android|Windows CE|LG|MOT|SAMSUNG|SonyEricsson).*";

        return ua.matches(pattern);
    }

    /**
     * 모바일, PC 뷰 템플릿 경로 생성
     *
     * @param path
     * @return
     */
    public String tpl(String path) {
        String prefix = isMobile() ? "mobile/" : "front/";

        return prefix + path;
    }


    /**
     * 줄개행 문자(\n 또는 \r\n) -> <br>
     * @param data
     * @return
     */
    public String nl2br(String data) {
        data = data.replace("\n", "<br>")
                .replace("\r", ""); // 윈도우

        return data;
    }
}