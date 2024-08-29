package org.g9project4.publicData.tour.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GreenPlace {

    private String address;
    private String areacode;
    @Id
    private String contentId;
    private String firstImage;
    private String cpyrhtDivCd;
    @JsonFormat(pattern = "yyyyMMddHHmmss")
    private LocalDateTime modifiedtime;
    private String sigunguCode;
    private String subTitle;
    @Lob
    private String summary;
    private String tel;
    private String telName;
    private String title;
    private String contentType = "생태";
}
