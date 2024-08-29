package org.g9project4.global;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.g9project4.global.exceptions.BadRequestException;
import org.g9project4.global.rests.JSONData;
import org.g9project4.publicData.tour.constants.ContentType;
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
    private final ObjectMapper objectMapper;

    private static final ResourceBundle commonsBundle;
    private static final ResourceBundle validationsBundle;
    private static final ResourceBundle errorsBundle;

    static {
        commonsBundle = ResourceBundle.getBundle("messages.commons");
        validationsBundle = ResourceBundle.getBundle("messages.validations");
        errorsBundle = ResourceBundle.getBundle("messages.errors");
    }

    public String url(String url) {
        List<ServiceInstance> instances = discoveryClient.getInstances("front-service");

        try {
            return String.format("%s%s", instances.get(0).getUri().toString(), url);
        } catch (Exception e) {
            return String.format("%s://%s:%d%s%s", request.getScheme(), request.getServerName(), request.getServerPort(), request.getContextPath(), url);
        }
    }

    public String redirectUrl(String url) {
        String _fromGateway = Objects.requireNonNullElse(request.getHeader("from-gateway"), "false");
        String gatewayHost = Objects.requireNonNullElse(request.getHeader("gateway-host"), "");
        boolean fromGateway = _fromGateway.equals("true");

        return fromGateway ? request.getScheme() + "://" + gatewayHost + "/app" + url : request.getContextPath() + url;
    }

    public String adminUrl(String url) {
        List<ServiceInstance> instances = discoveryClient.getInstances("admin-service");
        return String.format("%s%s", instances.get(0).getUri().toString(), url);
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

    public String getMessage(String code) {
        List<String> messages = getCodeMessages(new String[]{code});

        return messages.isEmpty() ? code : messages.get(0);
    }

    /**
     * 줄개행 문자(\n, \n\r) -> <br>
     *
     * @param str
     * @return
     */
    public String nl2br(String str) {
        return str.replace("\n", "<br>").replace("\r", "");

    }

    public ContentType getTypeByID(String id) {
        switch (id) {
            case ("12"):
                return ContentType.TourSpot;
            case ("14"):
                return ContentType.CultureFacility;
            case ("15"):
                return ContentType.Festival;
            case ("25"):
                return ContentType.TourCourse;
            case ("28"):
                return ContentType.Leports;
            case ("32"):
                return ContentType.Accommodation;
            case ("38"):
                return ContentType.Shopping;
            case ("39"):
                return ContentType.Restaurant;
            case ("1"):
                return ContentType.GreenTour;
        }
        throw new BadRequestException("Wrong contentType");
    }

    public ContentType typeCode(String type) {
        switch (type) {
            case ("spot"):
                return ContentType.TourSpot;
            case ("culture"):
                return ContentType.CultureFacility;
            case ("festival"):
                return ContentType.Festival;
            case ("course"):
                return ContentType.TourCourse;
            case ("leports"):
                return ContentType.Leports;
            case ("stay"):
                return ContentType.Accommodation;
            case ("shopping"):
                return ContentType.Shopping;
            case ("restaurant"):
                return ContentType.Restaurant;
            case ("green"):
                return ContentType.GreenTour;
        }
        throw new BadRequestException("Wrong contentType");
    }


    /**
     * 접속 장비가 모바일인지 체크
     *
     * @return
     */
    public boolean isMobile() {

        // 모바일 수동 전환 체크, 처리
        HttpSession session = request.getSession();
        String device = (String) session.getAttribute("device");

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
     * 비회원을 구분하는 Unique ID
     * IP + User-Agent
     *
     * @return
     */
    public int guestUid() {
        String ip = request.getRemoteAddr();
        String ua = request.getHeader("User-Agent");

        return Objects.hash(ip, ua);
    }

    public String toJson(Object data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "{}";
    }

    public Long toLong(String num) {
        return Long.valueOf(num);
    }

    public List<Map<String, String>> toList(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
        }
        return null;
    }

    public Map<String, String> toMap(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (JsonProcessingException e) {}

        return null;
    }
    public String getThumbUrl(Long seq, int width, int height) {
        return String.format("%s?seq=%d&width=%d&height=%d", url("/file/thumb"), seq, width, height);
    }

    public String getThumbUrl(String url, int width, int height) {
        return String.format("%s?url=%s&width=%d&height=%d", url("/file/thumb"), url, width, height);
    }
    /**
     * 달력
     */


}