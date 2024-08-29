package org.g9project4.global.rests.gov.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)//없는게 들어와도 배제
public class ApiItem {
    private String addr1;
    private String addr2;
    private String areacode;
    private String booktour;
    private String cat1;
    private String cat2;
    private String cat3;
    private Long contentid;
    private Long contenttypeid;
    @JsonFormat(pattern = "yyyyMMddHHmmss")
    private LocalDateTime createdtime;
    @JsonFormat(pattern = "yyyyMMddHHmmss")
    private LocalDateTime modifiedtime;
    private Double dist;//null일때를 대비해서
    private String firstimage;
    private String firstimage2;
    private String cpyrhtDivCd;
    private Double mapx;
    private Double mapy;
    private Integer mlevel;
    private Integer sigungucode;
    private String tel;
    private String title;

}
