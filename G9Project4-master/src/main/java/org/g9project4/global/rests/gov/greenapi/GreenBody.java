package org.g9project4.global.rests.gov.greenapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GreenBody {
    private GreenItems items;
    private Integer numOfRows;
    private Integer pageNo;
    private Integer totalCount;
}
