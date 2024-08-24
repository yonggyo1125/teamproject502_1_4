package org.g9project4.publicData.tour.services;

import lombok.RequiredArgsConstructor;
import org.g9project4.global.rests.gov.api.ApiItem;
import org.g9project4.global.rests.gov.api.ApiResult;
import org.g9project4.publicData.tour.entities.TourPlace;
import org.g9project4.publicData.tour.repositories.TourPlaceRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiUpdateService {

    private final RestTemplate restTemplate;
    private final TourPlaceRepository tourPlaceRepository;
    private String serviceKey = "n5fRXDesflWpLyBNdcngUqy1VluCJc1uhJ0dNo4sNZJ3lkkaYkkzSSY9SMoZbZmY7/O8PURKNOFmsHrqUp2glA==";
    //    @Scheduled(fixedRate = 1L, timeUnit = TimeUnit.DAYS)
    public void update() {
        for(int i = 0;i<100;i++) {

            String url = String.format("https://apis.data.go.kr/B551011/KorService1/areaBasedList1?MobileOS=AND&MobileApp=test&numOfRows=1000&pageNo=%d&serviceKey=%s&_type=json", i, serviceKey);

            ResponseEntity<ApiResult> response = null;
            try {
                response = restTemplate.getForEntity(URI.create(url), ApiResult.class);
            } catch (Exception e) {
                break;
            }
            if (response.getStatusCode().is2xxSuccessful()) {
                List<ApiItem> items = response.getBody().getResponse().getBody().getItems().getItem();
                for (ApiItem item : items) {
                    try {
                        String address = item.getAddr1();
                        address += StringUtils.hasText(item.getAddr2()) ? " " + item.getAddr2() : "";

                        TourPlace tourPlace = TourPlace.builder()
                                .contentId(item.getContentid())
                                .contentTypeId(item.getContenttypeid())
                                .category1(item.getCat1())
                                .category2(item.getCat2())
                                .category3(item.getCat3())
                                .title(item.getTitle())
                                .tel(item.getTel())
                                .address(address)
                                .areaCode(item.getAreacode())
                                .distance(item.getDist())
                                .bookTour(item.getBooktour().equals("1"))
                                .distance(item.getDist())
                                .firstImage(item.getFirstimage())
                                .firstImage2(item.getFirstimage2())
                                .cpyrhtDivCd(item.getCpyrhtDivCd())
                                .latitude(item.getMapy())
                                .longitude(item.getMapx())
                                .mapLevel(item.getMlevel())
                                .sigunguCode(item.getSigungucode())
                                .build();
                        tourPlaceRepository.saveAndFlush(tourPlace);
                    } catch (Exception e) {//예외 발생하면 이미 등록된 여행지
                        e.printStackTrace();
                        break;
                    }
                }
            }
        }
    }
}
