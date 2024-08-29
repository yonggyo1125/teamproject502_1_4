package org.g9project4.global.rests.gov.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiBody2 {
    private ApiItems2 items;
    private Integer numOfRows;
    private Integer pageNo;
    private Integer totalCount;
}
