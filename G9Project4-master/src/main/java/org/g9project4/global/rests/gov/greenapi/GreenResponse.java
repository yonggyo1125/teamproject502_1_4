package org.g9project4.global.rests.gov.greenapi;

import lombok.Data;
import org.g9project4.global.rests.gov.api.ApiHeader;

@Data
public class GreenResponse {
    private ApiHeader header;
    private GreenBody body;
}
