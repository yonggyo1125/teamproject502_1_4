package org.g9project4.publicData.tour.services;

import lombok.RequiredArgsConstructor;
import org.g9project4.publicData.tour.exceptions.CategoryNotFoundException;
import org.g9project4.global.rests.gov.api.ApiItem;
import org.g9project4.global.rests.gov.api.ApiResult;
import org.g9project4.global.rests.gov.areacodeapi.Code;
import org.g9project4.global.rests.gov.areacodeapi.CodeResult;
import org.g9project4.global.rests.gov.categoryapi.CategoryItem;
import org.g9project4.global.rests.gov.categoryapi.CategoryResult;
import org.g9project4.global.rests.gov.greenapi.GreenItem;
import org.g9project4.global.rests.gov.greenapi.GreenResult;
import org.g9project4.global.rests.gov.sigunguapi.SigunguItem;
import org.g9project4.global.rests.gov.sigunguapi.SigunguResult;
import org.g9project4.publicData.tour.entities.*;
import org.g9project4.publicData.tour.repositories.*;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ApiUpdateService {

    private final RestTemplate restTemplate;
    private final TourPlaceRepository tourPlaceRepository;
    private final GreenPlaceRepository greenPlaceRepository;

    private final AreaCodeRepository areaCodeRepository;
    private final SigunguCodeRepository sigunguCodeRepository;
    private final CategoryRepository categoryRepository;




    //    @Scheduled(fixedRate = 1L, timeUnit = TimeUnit.DAYS)

    /**
     * 관광지 업데이트
     *
     * @param
     */
    public void update(String sKey) {
     //km   for (int i = 0; i < 100; i++) {

        for (int i = 0; i < 20; i++) {
            String url = String.format("https://apis.data.go.kr/B551011/KorService1/areaBasedList1?MobileOS=AND&MobileApp=TEST&numOfRows=1000&pageNo=%d&serviceKey=%s&_type=json", i,  sKey);

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
                                .modifiedTime(item.getModifiedtime())
                                .createdTime(item.getCreatedtime())
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
                                .sigunguCode(item.getSigunguCode())
                                .mapLevel(item.getMlevel())
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

    /**
     * 생태 관광지 업데이트
     *
     * @param sKey
     */
    public void greenUpdate(String sKey) {
        for (int i = 0; i < 5; i++) {
            String url = String.format("https://apis.data.go.kr/B551011/GreenTourService1/areaBasedList1?numOfRows=30&pageNo=%d&MobileOS=AND&MobileApp=test&_type=json&serviceKey=%s", i, sKey);
            ResponseEntity<GreenResult> response = restTemplate.getForEntity(url, GreenResult.class);
            try {
                response = restTemplate.getForEntity(URI.create(url), GreenResult.class);
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
            if (response.getStatusCode().is2xxSuccessful()) {
                List<GreenItem> items = response.getBody().getResponse().getBody().getItems().getItem();
                for (GreenItem item : items) {
                    try {
                        GreenPlace greenPlace = GreenPlace.builder()
                                .address(item.getAddr())
                                .areacode(item.getAreacode())
                                .contentId(item.getContentid())
                                .firstImage(item.getMainimage())
                                .cpyrhtDivCd(item.getCpyrhtDivCd())
                                .modifiedtime(item.getModifiedtime())
                                //km      .sigugunCode(item.getSigungucode())
                                .subTitle(item.getSubtitle())
                                .summary(item.getSummary())
                                .tel(item.getTel())
                                .telName(item.getTelname())
                                .title(item.getTitle())
                                .build();
                        greenPlaceRepository.saveAndFlush(greenPlace);
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        }
    }

    /**
     * 지역 코드 업데이트
     *
     * @param sKey
     */
    public void areaCodeUpdate(String sKey) {
        String url = String.format("https://apis.data.go.kr/B551011/KorService1/areaCode1?numOfRows=50&MobileOS=AND&MobileApp=test&_type=json&serviceKey=%s", sKey);

        try {
            ResponseEntity<CodeResult> response = restTemplate.getForEntity(URI.create(url), CodeResult.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                List<Code> codes = response.getBody().getResponse().getCodeBody().getItems().getItem();
                for (Code code : codes) {
                    AreaCode areaCode = AreaCode.builder()
                            .areaCode(code.getCode())
                            .name(code.getName())
                            .build();
                    areaCodeRepository.saveAndFlush(areaCode);
                }
                List<AreaCode> areaCodeList = areaCodeRepository.findAll();
                for (AreaCode areaCode : areaCodeList) {
                    String url2 = String.format("https://apis.data.go.kr/B551011/KorService1/areaCode1?numOfRows=50&MobileOS=and&MobileApp=test&areaCode=%s&_type=json&serviceKey=%s", areaCode.getAreaCode(), sKey);
                    ResponseEntity<SigunguResult> sigunguResponse = restTemplate.getForEntity(URI.create(url2), SigunguResult.class);
                    if (response.getStatusCode().is2xxSuccessful()) {
                        List<SigunguItem> sigungus = sigunguResponse.getBody().getResponse().getBody().getItems().getItem();
                        for (SigunguItem code : sigungus) {
                            SigunguCode sigunguCode = SigunguCode.builder()
                                    .areaCode(areaCode.getAreaCode())
                                    .sigunguCode(code.getCode())
                                    .name(code.getName())
                                    .build();
                            sigunguCodeRepository.saveAndFlush(sigunguCode);
                        }
                    }
                }
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private List<CategoryItem> getCategory(String sKey, String cat1, String cat2, String cat3) {
        String url = String.format("https://apis.data.go.kr/B551011/KorService1/categoryCode1?MobileOS=and&MobileApp=test&cat1=%s&cat2=%s&cat3=%s&_type=json&serviceKey=%s", cat1, cat2, cat3, sKey);
        ResponseEntity<CategoryResult> response = restTemplate.getForEntity(URI.create(url), CategoryResult.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody().getResponse().getBody().getItems().getItem();
        }
        throw new CategoryNotFoundException();
    }

    public void categoryUpdate(String sKey) {
        try {
            List<CategoryItem> categoryItems = getCategory(sKey, "", "", "");
            for (CategoryItem categoryItem : categoryItems) {

                List<CategoryItem> categoryItems1 = getCategory(sKey, categoryItem.getCode(), "", "");
                for (CategoryItem categoryItem1 : categoryItems1) {
                    List<CategoryItem> categoryItems2 = getCategory(sKey, categoryItem.getCode(), categoryItem1.getCode(), "");
                    for (CategoryItem categoryItem2 : categoryItems2) {
                        Category category = Category.builder()
                                .category1(categoryItem.getCode())
                                .category2(categoryItem1.getCode())
                                .category3(categoryItem2.getCode())
                                .name1(categoryItem.getName())
                                .name2(categoryItem1.getName())
                                .name3(categoryItem2.getName())
                                .build();
                        categoryRepository.saveAndFlush(category);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
