package org.g9project4.publicData.myvisit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TourplaceDto {
    private Long contentId;
    private String title;
    private String address;
    private String firstImage;
    private String tel;
    private int finalPointValue;
}
