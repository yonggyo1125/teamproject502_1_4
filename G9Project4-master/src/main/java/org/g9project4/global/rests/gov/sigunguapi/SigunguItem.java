package org.g9project4.global.rests.gov.sigunguapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SigunguItem {
    private String code;
    private String name;
}
