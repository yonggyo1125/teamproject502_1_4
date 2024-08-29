package org.g9project4.global.rests.gov.detailapi;

import lombok.Data;
import org.g9project4.global.rests.gov.api.ApiHeader;

@Data
public class DetailResponse {
    private ApiHeader header;
    private DetailBody body;
}
