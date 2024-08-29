package org.g9project4.global.rests.gov.detailapi;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DetailItem {
    private Long contentid;
    private String contenttypeid;
    private String title;
    @JsonFormat(pattern = "yyyyMMddHHmmss")
    private LocalDateTime createdtime;
    @JsonFormat(pattern = "yyyyMMddHHmmss")
    private LocalDateTime modifiedtime;
    private String tel;
    private String telname;
    private String homepage;
    private String booktour;
    private String firstimage;
    private String firstimage2;
    private String cpyrhtDivCd;
    private String areacode;
    private Integer sigungucode;
    private String cat1;
    private String cat2;
    private String cat3;
    private String addr1;
    private String addr2;
    private String zipcode;
    private Double mapx;
    private Double mapy;
    private Integer mlevel;
    private String overview;

}
