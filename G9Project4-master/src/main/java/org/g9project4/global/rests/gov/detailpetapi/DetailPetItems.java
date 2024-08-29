package org.g9project4.global.rests.gov.detailpetapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DetailPetItems {
    private List<DetailPetItem> item;
}
