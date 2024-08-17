package org.g9project4.tourvisit.services;

import lombok.RequiredArgsConstructor;
import org.g9project4.global.rests.gov.api.ApiBody2;
import org.g9project4.global.rests.gov.api.ApiResult2;
import org.g9project4.tourvisit.entities.SidoVisit;
import org.g9project4.tourvisit.repositories.SidoVisitRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SidoVisitStatisticService {
    private final RestTemplate restTemplate;
    private final SidoVisitRepository repository;


    /**
     * 광역시 방문 통계
     *
     */
    public void updateSidoVisit(String type) {

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
        Map<String, Map<String, Object>> data = new HashMap<>();

        for (int i = 1; i <= totalPages; i++) {
            ApiResult2 result = getData(i, limit, sdate, edate);

            ApiBody2 body = result.getResponse().getBody();

            List<Map<String, String>> items = body.getItems().getItem();
            for (Map<String, String> item : items) {
                String areaCode = item.get("areaCode");
                Map<String, Object> visitData = data.getOrDefault(areaCode, new HashMap<>());
                visitData.put("areaName", item.get("areaNm"));

                double type1 = (double)visitData.getOrDefault("type1", 0.0); // 현지인
                double type2 = (double)visitData.getOrDefault("type2", 0.0); // 외지인
                double type3 = (double)visitData.getOrDefault("type3", 0.0);

                String divNm = item.get("touDivNm");
                double num = Double.valueOf(Objects.requireNonNullElse(item.get("touNum"), "0.0"));
                if (divNm.contains("현지인")) {
                    type1 += num;
                } else if (divNm.contains("외지인")) {
                    type2 += num;
                } else if (divNm.contains("외국인")) {
                    type3 += num;
                }

                visitData.put("type1", type1);
                visitData.put("type2", type2);
                visitData.put("type3", type3);
                data.put(areaCode, visitData);
            }
        }

        List<SidoVisit> items = new ArrayList<>();
        for (Map.Entry<String, Map<String, Object>> entry : data.entrySet()) {
            String areaCode = entry.getKey();
            Map<String, Object> visitData = entry.getValue();
            String areaName = (String)visitData.get("areaName");

            double type1 = (double)visitData.getOrDefault("type1", 0.0);
            double type2 = (double)visitData.getOrDefault("type2", 0.0);
            double type3 = (double)visitData.getOrDefault("type3", 0.0);

            SidoVisit item = repository.findById(areaCode).orElseGet(SidoVisit::new);
            item.setAreaCode(areaCode);
            item.setAreaName(areaName);

            if (type.equals("1W")) { // 1주 전
                item.setType1W1(type1);
                item.setType2W1(type2);
                item.setType3W1(type3);
            } else if (type.equals("1M")) { // 1달 전
                item.setType1M1(type1);
                item.setType2M1(type2);
                item.setType3M1(type3);
            } else if (type.equals("3M")) { // 3달 전
                item.setType1M3(type1);
                item.setType2M3(type2);
                item.setType3M3(type3);
            } else if (type.equals("6M")) { // 6달 전
                item.setType1M6(type1);
                item.setType2M6(type2);
                item.setType3M6(type3);
            } else if (type.equals("1Y")) { // 1년 전
                item.setType1Y1(type1);
                item.setType2Y1(type2);
                item.setType3Y1(type3);
            } else { // 1D - 하루 전
                item.setType1D1(type1);
                item.setType2D1(type2);
                item.setType3D1(type3);
            }

            items.add(item);
        }

        repository.saveAllAndFlush(items);

        // 통계 데이터 업데이트

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


    // 일별 통계
    @Scheduled(cron = "0 0 1 * * *")  // 매일 새벽 1시
    public void updateSidoVisit1D() {
        updateSidoVisit("1D");
    }

    @Scheduled(cron = "0 10 1 * * *") // 매일 새벽 1시 10분
    public void updateSidoVisit1W() {
        updateSidoVisit("1W");
    }

    @Scheduled(cron = "0 20 1 * * *") // 매일 새벽 1시 20분
    public void updateSidoVisit1M() {
        updateSidoVisit("1M");
    }

    @Scheduled(cron = "0 30 1 * * *") // 매일 새벽 1시 30분
    public void updateSidoVisit3M() {
        updateSidoVisit("3M");
    }

    @Scheduled(cron = "0 50 1 * * *") // 매일 새벽 1시 50분
    public void updateSidoVisit6M() {
        updateSidoVisit("6M");
    }

    @Scheduled(cron = "0 20 2 * * *") // 매일 새벽 2시 20분
    public void updateSidoVisit1Y() {
        updateSidoVisit("1Y");
    }
}
