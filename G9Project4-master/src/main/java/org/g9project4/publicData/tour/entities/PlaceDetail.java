package org.g9project4.publicData.tour.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.g9project4.global.rests.gov.detailapi.DetailItem;
import org.g9project4.global.rests.gov.detailpetapi.DetailPetItem;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaceDetail<DetailItem, DetailPetItem> {
    private DetailItem detailItem;
    private DetailPetItem detailPetItem;
}
