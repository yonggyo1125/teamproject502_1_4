package org.g9project4.tourvisit.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.g9project4.global.rests.gov.api.ApiBody2;
import org.g9project4.global.rests.gov.api.ApiResult2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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


        int pageNo = 1;
        int limit = 1000;
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

        ApiResult2 result2 = getData(1, 1, sdate, edate);
        if (result2 == null) return;

        int total = result2.getResponse().getBody().getTotalCount();
        int totalPages = (int)Math.ceil(total / (double)limit);

        // type1 - 현지인, type2 - 외지인, type3 - 외국인
        double type1 = 0.0, type2 = 0.0, type3 = 0.0;
        for (int i = 1; i <= totalPages; i++) {
            ApiResult2 result = getData(i, limit, sdate, edate);

            ApiBody2 body = result.getResponse().getBody();

            List<Map<String, String>> items = body.getItems().getItem();
            for (Map<String, String> item : items) {
                String divNm = item.get("touDivNm");
                double num = Double.valueOf(Objects.requireNonNullElse(item.get("touNum"), "0.0"));
                if (divNm.contains("현지인")) {
                    type1 += num;
                } else if (divNm.contains("외지인")) {
                    type2 += num;
                } else if (divNm.contains("외국인")) {
                    type3 += num;
                }
            }
        }
    }

    private ApiResult2 getData(int pageNo, int limit, LocalDate sdate, LocalDate edate) {

        String serviceKey = "RtrIIdYjcb3IXn1a/zF7itGWY5ZFS3IEj85ohFx/snuKG9hYABL5Tn8jEgCEaCw6uEIHvUz30yF4n0GGP6bVIA==";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        String url = String.format("https://apis.data.go.kr/B551011/DataLabService/metcoRegnVisitrDDList?MobileOS=AND&MobileApp=TEST&serviceKey=%s&startYmd=%s&endYmd=%s&numOfRows=%d&pageNo=%d&_type=json", serviceKey, formatter.format(sdate), formatter.format(edate), limit, pageNo);

        ResponseEntity<ApiResult2> response = restTemplate.getForEntity(URI.create(url), ApiResult2.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            return null;
        }

        ApiResult2 result = response.getBody();
        if (!result.getResponse().getHeader().getResultCode().equals("0000")) {
            return null;
        }

        return result;
    }
}
