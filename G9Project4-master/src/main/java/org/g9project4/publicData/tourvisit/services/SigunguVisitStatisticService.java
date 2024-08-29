package org.g9project4.publicData.tourvisit.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.g9project4.global.rests.gov.api.ApiBody2;
import org.g9project4.global.rests.gov.api.ApiHeader;
import org.g9project4.global.rests.gov.api.ApiResponse2;
import org.g9project4.global.rests.gov.api.ApiResult2;
import org.g9project4.publicData.tourvisit.entities.SidoVisit;
import org.g9project4.publicData.tourvisit.entities.SigunguVisit;
import org.g9project4.publicData.tourvisit.repositories.SidoVisitRepository;
import org.g9project4.publicData.tourvisit.repositories.SigunguVisitRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SigunguVisitStatisticService {
    private final RestTemplate restTemplate;
    private final SigunguVisitRepository repository1;
    private final SidoVisitRepository repository2;

    public void updateVisit(String type) {

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
        int totalPages = (int) Math.ceil(total / (double) limit);

        // type1 - 현지인, type2 - 외지인, type3 - 외국인
        Map<String, Map<String, Object>> data = new HashMap<>();
        for (int i = 1; i <= totalPages; i++) {
            ApiResult2 result = getData(i, limit, sdate, edate);

            ApiBody2 body = result.getResponse().getBody();

            List<Map<String, String>> items = body.getItems().getItem();
            for (Map<String, String> item : items) {
                String sigunguCode = item.get("signguCode");

                Map<String, Object> visitData = data.getOrDefault(sigunguCode, new HashMap<>());
                visitData.put("sigunguName", item.get("signguNm"));

                double type1 = (double) visitData.getOrDefault("type1", 0.0); // 현지인
                double type2 = (double) visitData.getOrDefault("type2", 0.0); // 외지인
                double type3 = (double) visitData.getOrDefault("type3", 0.0);

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
                data.put(sigunguCode, visitData);

            }
        }

        List<SigunguVisit> items = new ArrayList<>();
        for (Map.Entry<String, Map<String, Object>> entry : data.entrySet()) {
            String sigunguCode = entry.getKey();
            Map<String, Object> visitData = entry.getValue();
            String sigunguName = (String) visitData.get("sigunguName");

            double type1 = (double) visitData.getOrDefault("type1", 0.0);
            double type2 = (double) visitData.getOrDefault("type2", 0.0);
            double type3 = (double) visitData.getOrDefault("type3", 0.0);

            SigunguVisit item = repository1.findById(sigunguCode).orElseGet(SigunguVisit::new);
            item.setSigunguCode(sigunguCode);
            item.setSigunguName(sigunguName);

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

            if (item.getSidoVisit() == null) { // 처음 통계
                String areaCode = sigunguCode.substring(0, 2);
                SidoVisit sidoVisit = repository2.findById(areaCode).orElse(null);
                item.setSidoVisit(sidoVisit);
            }

            items.add(item);
        }

        repository1.saveAllAndFlush(items);

        // 통계 데이터 업데이트
    }



    // LIMITED_NUMBER_OF_SERVICE_REQUESTS_EXCEEDS_ERROR라는 오류는 API 호출 제한을 초과했음 에로로 인한 수정
    private static final int MAX_RETRIES = 3;
    private static final long RETRY_DELAY_MS = 60000; // 60초

    private ApiResult2 getData(int pageNo, int limit, LocalDate sdate, LocalDate edate) {
        String sKey = "7rVGv4M2LZhWVFhu97TYGa8Lltf6eOFPG99BKHny11wiv2TWbUle1fP3Foos%2BQcjBgTlHVDYcoG8RwfuspzfxA%3D%3D";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String url = String.format("https://apis.data.go.kr/B551011/DataLabService/locgoRegnVisitrDDList?MobileOS=AND&MobileApp=TEST&serviceKey=%s&startYmd=%s&endYmd=%s&numOfRows=%d&pageNo=%d&_type=json",
                sKey, formatter.format(sdate), formatter.format(edate), limit, pageNo);

        /* km 로그 정보 필요시 주석 해제바람  시작 */

    log.info("Request URL: {}", url); // 요청 URL 로깅

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                ResponseEntity<ApiResult2> response = restTemplate.getForEntity(URI.create(url), ApiResult2.class);
                log.info("Response Status: {}", response.getStatusCode()); // 응답 상태 코드 로깅

                if (!response.getStatusCode().is2xxSuccessful()) {
                    log.error("Failed to fetch data from API. Status code: {}", response.getStatusCode());
                    continue; // 재시도
                }

                ApiResult2 result = response.getBody();
                if (result == null) {
                    log.error("API result is null.");
                    continue; // 재시도
                }

                ApiResponse2 apiResponse = result.getResponse();
                if (apiResponse == null) {
                    log.error("API response is null.");
                    continue; // 재시도
                }

                ApiHeader header = apiResponse.getHeader();
                if (header == null) {
                    log.error("API response header is null.");
                    continue; // 재시도
                }

                if (!header.getResultCode().equals("0000")) {
                    log.error("API response error. Result code: {}", header.getResultCode());
                    continue; // 재시도
                }

                return result; // 성공
            } catch (Exception e) {
                log.error("Error occurred during API call: {}", e.getMessage());
            }

            try {
                Thread.sleep(RETRY_DELAY_MS); // 대기 후 재시도
            } catch (InterruptedException e) {
                log.error("Interrupted during retry delay: {}", e.getMessage());
            }
        }

        log.error("Failed to fetch data after {} attempts", MAX_RETRIES);
        return null;
    /* km 로그 정보 필요시 주석 해제바람  끝 */

}
    // 일별 통계
    @Scheduled(cron = "0 0 3 * * *")  // 매일 새벽 1시
    public void updateVisit1D() {
        updateVisit("1D");
    }

    @Scheduled(cron = "0 10 3 * * *") // 매일 새벽 1시 10분
    public void updateVisit1W() {
        updateVisit("1W");
    }

    @Scheduled(cron = "0 20 3 * * *") // 매일 새벽 1시 20분
    public void updateVisit1M() {
        updateVisit("1M");
    }

    @Scheduled(cron = "0 30 3 * * *") // 매일 새벽 1시 30분
    public void updateVisit3M() {
        updateVisit("3M");
    }

    @Scheduled(cron = "0 50 3 * * *") // 매일 새벽 1시 50분
    public void updateVisit6M() {
        updateVisit("6M");
    }

    @Scheduled(cron = "0 20 4 * * *") // 매일 새벽 2시 20분
    public void updateVisit1Y() {
        updateVisit("1Y");
    }
}
