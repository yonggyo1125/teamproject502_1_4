package org.g9project4.global.rests.gov.sigunguapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SigunguBody {
    private SigunguItems items;
    private Integer numOfRows;
    private Integer pageNo;
    private Integer totalCount;
}
