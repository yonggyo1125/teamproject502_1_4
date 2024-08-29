package org.g9project4.global.rests.gov.categoryapi;

import lombok.Data;
import org.g9project4.global.rests.gov.api.ApiHeader;

@Data
public class CategoryResponse {
    private ApiHeader header;
    private CategoryBody body;
}
