package org.g9project4.global.rests.gov.detailpetapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DetailPetResult {
    private DetailPetResponse response;
}
