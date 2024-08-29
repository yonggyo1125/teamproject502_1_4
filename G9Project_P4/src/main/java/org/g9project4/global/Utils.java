package org.g9project4.global;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.g9project4.file.entities.FileInfo;
import org.g9project4.file.services.FileInfoService;
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
    private final ObjectMapper om;
    private final FileInfoService fileInfoService;

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

        return fromGateway ? request.getScheme() + "://" + gatewayHost + "/app" + url : String.format("%s://%s:%d%s%s", request.getScheme(), request.getServerName(), request.getServerPort(), request.getContextPath(), url);
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
    public String getMessage(String code){
        List<String> messages = getCodeMessages(new String[]{code});

        return messages.isEmpty() ? code : messages.get(0);
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
     * 줄개행 문자(\n, \n\r) -> <br>
     * @param data
     * @return
     */
    public String nl2br(String data) {
        data = data.replace("\n", "<br>")
                .replace("\r", "");

        return data;
    }

    /**
     * 비회원을 구분하는 Unique ID
     *   IP + User-Agent
     * @return
     */
    public int guestUid() {
        String ip = request.getRemoteAddr();
        String ua = request.getHeader("User-Agent");

        return Objects.hash(ip, ua);
    }

    public String toJson(Object data) {
        try {
            return om.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Long toLong(String num) {
        return Long.valueOf(num);
    }

    public List<Map<String, String>> toList(String json) {

        try {
            return om.readValue(json, new TypeReference<>() {});
        } catch (JsonProcessingException e) {}

        return null;
    }

    public Map<String, String> toMap(String json) {
        try {
            return om.readValue(json, new TypeReference<>() {});
        } catch (JsonProcessingException e) {}

        return null;
    }

    public String getThumbUrl(Long seq, int width, int height) {
        return String.format("%s?seq=%d&width=%d&height=%d", url("/file/thumb"), seq, width, height);
    }

    public String getThumbUrl(String url, int width, int height) {
        return String.format("%s?url=%s&width=%d&height=%d", url("/file/thumb"), url, width, height);
    }

    public List<FileInfo> getSelectedImages(String gid, String location) {
        List<FileInfo> items = fileInfoService.getSelectedList(gid, location);
        items = Objects.requireNonNullElse(items, Collections.EMPTY_LIST);

        return items.stream()
                    .filter(item -> item.getContentType().contains("image/"))
                    .toList();
    }

    public List<FileInfo> getSelectedImages(String gid) {
        return getSelectedImages(gid, null);
    }
}