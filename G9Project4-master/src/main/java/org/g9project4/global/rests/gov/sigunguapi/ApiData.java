package org.g9project4.global.rests.gov.sigunguapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiData {

    @JsonProperty("시군구명")
    private String sigunguName;

    @JsonProperty("시군구코드")
    private Integer sigunguCode2;

    @JsonProperty("시도코드")
    private Integer sidoCode;
}

//시군구명": "남양주시",
//"시군구코드": 41360,
//"시도코드": 41