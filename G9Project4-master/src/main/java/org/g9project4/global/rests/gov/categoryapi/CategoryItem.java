package org.g9project4.global.rests.gov.categoryapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryItem {
    private String code;
    private String name;
}
