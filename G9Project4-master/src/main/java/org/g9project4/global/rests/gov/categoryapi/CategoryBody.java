package org.g9project4.global.rests.gov.categoryapi;

import lombok.Data;

@Data
public class CategoryBody {
    private CategoryItems items;
    private Integer numOfRows;
    private Integer pageNo;
    private Integer totalCount;
}
