package org.g9project4.global.rests.gov.areacodeapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Code {
    private String code;
    private String name;
}
