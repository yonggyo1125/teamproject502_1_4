package org.g9project4.global.rests.gov.detailpetapi;

import lombok.Data;
import org.g9project4.global.rests.gov.api.ApiHeader;

@Data
public class DetailPetResponse {
    private ApiHeader header;
    private DetailPetBody body;
}
