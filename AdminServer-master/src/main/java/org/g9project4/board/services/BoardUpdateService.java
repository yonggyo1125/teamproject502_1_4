package org.g9project4.board.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import org.g9project4.board.controllers.RequestBoard;

import org.g9project4.board.entities.BoardData;
import org.g9project4.global.Utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
@RequiredArgsConstructor
public class BoardUpdateService {
    private final RestTemplate restTemplate;
    private final ObjectMapper om;
    private final Utils utils;

    public void update(String mode, List<BoardData> items) {
        mode = StringUtils.hasText(mode) ? mode : "update";

        String url = utils.url("/board/admin/" + mode, "api-service");
        Map<String, List<BoardData>> params = new HashMap<>();
        params.put("items", items);

        try {
            String jsonBody = om.writeValueAsString(params);
            HttpHeaders headers = utils.getCommonHeaders("POST");
            HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(URI.create(url), HttpMethod.POST, request, String.class);

            System.out.println(response);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void update(String mode, RequestBoard form) {
        mode = StringUtils.hasText(mode) ? mode : mode;

    }
}