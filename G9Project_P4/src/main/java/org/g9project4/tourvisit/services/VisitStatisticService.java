package org.g9project4.tourvisit.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.g9project4.global.rests.gov.api.ApiResult2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class VisitStatisticService {
    private final RestTemplate restTemplate;
    private final ObjectMapper om;

    /**
     * 광역시 방문 통계
     *
     */
    public void updateMetcoRegnVisit(int pageNo, LocalDate sdate, LocalDate edate) {
        String serviceKey = "RtrIIdYjcb3IXn1a/zF7itGWY5ZFS3IEj85ohFx/snuKG9hYABL5Tn8jEgCEaCw6uEIHvUz30yF4n0GGP6bVIA==";

        pageNo = Math.max(pageNo, 1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        String url = String.format("https://apis.data.go.kr/B551011/DataLabService/metcoRegnVisitrDDList?MobileOS=AND&MobileApp=TEST&serviceKey=%s&startYmd=%s&endYmd=%s&numOfRows=1000&pageNo=%d&_type=json", serviceKey, formatter.format(sdate), formatter.format(edate), pageNo);

        ResponseEntity<ApiResult2> response = restTemplate.getForEntity(URI.create(url), ApiResult2.class);
        System.out.println(response);

    }
}
