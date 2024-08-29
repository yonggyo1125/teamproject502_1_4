package org.g9project4.publicData.tour.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.g9project4.member.entities.Member;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TourPlacePointId {
    private Member member;
    private TourPlace tourPlace;
}
