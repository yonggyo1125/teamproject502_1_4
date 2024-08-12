package org.g9project4.config.controllers;

import lombok.Data;

@Data
public class ApiConfig {
    private String publicOpenApiKey; // 공공 API 인증키

    private String kakaoJavascriptKey; // 카카오 API - 자바스크립트 앱 키
    private String kakaoRestApiKey; // 카카오 API - REST API 키
    
    private String tmapJavascriptKey; // TMAP API 자바스크립트 앱 키
}
