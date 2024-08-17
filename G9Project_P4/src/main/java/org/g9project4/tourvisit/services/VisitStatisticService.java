package org.g9project4.tourvisit.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.g9project4.global.rests.gov.api.ApiResult2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
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
    public void updateSidoVisit(String type) {
        String serviceKey = "RtrIIdYjcb3IXn1a/zF7itGWY5ZFS3IEj85ohFx/snuKG9hYABL5Tn8jEgCEaCw6uEIHvUz30yF4n0GGP6bVIA==";

        int pageNo = 1;
        type = StringUtils.hasText(type) ? type : "1D";

        LocalDate edate = LocalDate.now().minusMonths(1L);
        LocalDate sdate = edate.minusDays(1L);
        if (type.equals("1W")) { // 1주 전
            sdate = edate.minusWeeks(1L);
        } else if (type.equals("1M")) { // 1달 전
            sdate = edate.minusMonths(1L);
        } else if (type.equals("3M")) { // 3달 전
            sdate = edate.minusMonths(3L);
        } else if (type.equals("6M")) { // 6달 전
            sdate = edate.minusMonths(6L);
        } else if (type.equals("1Y")) { // 1년 전
            sdate = edate.minusYears(1L);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        String url = String.format("https://apis.data.go.kr/B551011/DataLabService/metcoRegnVisitrDDList?MobileOS=AND&MobileApp=TEST&serviceKey=%s&startYmd=%s&endYmd=%s&numOfRows=1000&pageNo=%d&_type=json", serviceKey, formatter.format(sdate), formatter.format(edate), pageNo);

        ResponseEntity<ApiResult2> response = restTemplate.getForEntity(URI.create(url), ApiResult2.class);
        System.out.println(response);

    }
}
