package org.g9project4.global.rests.gov.sigunguapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.g9project4.global.rests.gov.detailapi.DetailResponse;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResult {


    @JsonProperty("currentCount")
    private Integer currentCount;

    @JsonProperty("data")
    private List<ApiData> data;

    @JsonProperty("matchCount")
    private Integer matchCount;

    @JsonProperty("page")
    private Integer page;

    @JsonProperty("perPage")
    private Integer perPage;

    @JsonProperty("totalCount")
    private Integer totalCount;
}

