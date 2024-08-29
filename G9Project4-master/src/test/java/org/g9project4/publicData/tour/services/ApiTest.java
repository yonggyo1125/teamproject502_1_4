package org.g9project4.publicData.tour.services;

import com.ctc.wstx.shaded.msv_core.util.Uri;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.g9project4.global.rests.gov.api.ApiResult;
import org.g9project4.global.rests.gov.detailpetapi.DetailPetItem;
import org.g9project4.global.rests.gov.detailpetapi.DetailPetResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@SpringBootTest
public class ApiTest {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    private String sKey = "n5fRXDesflWpLyBNdcngUqy1VluCJc1uhJ0dNo4sNZJ3lkkaYkkzSSY9SMoZbZmY7%2FO8PURKNOFmsHrqUp2glA%3D%3D";

    @Test
    void test1() {
        Long contentId = 129703L;
        String detailPetUrl = String.format("https://apis.data.go.kr/B551011/KorService1/detailPetTour1?serviceKey=%s&MobileOS=and&MobileApp=test&contentId=%d&_type=json", sKey, contentId);
        ResponseEntity<DetailPetResult> response = null;
        try {
            restTemplate.getForObject(detailPetUrl, DetailPetResult.class);
            response = restTemplate.getForEntity(URI.create("https://apis.data.go.kr/B551011/KorService1/detailPetTour1?serviceKey=n5fRXDesflWpLyBNdcngUqy1VluCJc1uhJ0dNo4sNZJ3lkkaYkkzSSY9SMoZbZmY7%2FO8PURKNOFmsHrqUp2glA%3D%3D&MobileOS=and&MobileApp=test&contentId=125266&_type=json"), DetailPetResult.class);
        } catch (RestClientException e) {
            e.printStackTrace();
        }
        System.out.println(response);
    }
}
