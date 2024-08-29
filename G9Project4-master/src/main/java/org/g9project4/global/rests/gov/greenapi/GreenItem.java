package org.g9project4.global.rests.gov.greenapi;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Lob;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GreenItem {
    private String addr;
    private String areacode;
    private String contentid;
    @JsonFormat(pattern = "yyyyMMddHHmmss")
    private LocalDateTime createdtime;
    private String mainimage;
    private String cpyrhtDivCd;
    @JsonFormat(pattern = "yyyyMMddHHmmss")
    private LocalDateTime modifiedtime;
    private String sigungucode;
    private String subtitle;
    @Lob
    private String summary;
    private String tel;
    private String telname;
    private String title;

}
