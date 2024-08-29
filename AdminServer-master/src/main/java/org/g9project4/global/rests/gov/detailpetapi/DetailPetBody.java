package org.g9project4.global.rests.gov.detailpetapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DetailPetBody {
    private DetailPetItems items = null;
    private Integer numOfRows;
    private Integer pageNo;
    private Integer totalCount;
}
