package org.g9project4.global.rests.gov.sigunguapi;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import org.g9project4.global.rests.gov.api.ApiHeader;

@Data
public class SigunguResponse {
    private ApiHeader header;
    private SigunguBody body;
}
