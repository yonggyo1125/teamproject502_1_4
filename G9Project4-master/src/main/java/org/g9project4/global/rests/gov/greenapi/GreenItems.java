package org.g9project4.global.rests.gov.greenapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GreenItems {
    List<GreenItem> item;
}
