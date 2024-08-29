package org.g9project4.global.rests.gov.sigunguapi;

import lombok.Data;
import org.g9project4.global.rests.gov.api.ApiHeader;

@Data
public class SigunguResponse {
    private ApiHeader header;
    private SigunguBody body;
}
