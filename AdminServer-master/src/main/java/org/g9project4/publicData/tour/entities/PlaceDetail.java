package org.g9project4.publicData.tour.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaceDetail<DetailItem, DetailPetItem> {
    private DetailItem detailItem;
    private DetailPetItem detailPetItem;
}
