package org.g9project4.global.rests.gov.areacodeapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CodeBody {
    private Codes items;
    private Integer numOfRows;
    private Integer pageNo;
    private Integer totalCount;
}
