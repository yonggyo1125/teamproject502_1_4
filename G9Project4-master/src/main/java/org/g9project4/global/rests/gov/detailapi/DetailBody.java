package org.g9project4.global.rests.gov.detailapi;

import lombok.Data;

@Data
public class DetailBody {
    private DetailItems items;
    private Integer numOfRows;
    private Integer pageNo;
    private Integer totalCount;
}
