package org.g9project4.publicData.tour.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.g9project4.member.entities.Member;

@Data
@Entity
@IdClass(TourPlacePointId.class)
@Table(indexes = {
        @Index(name="idx_tourplacepoint_visit", columnList = "finalPointValueVisit DESC"),
        @Index(name="idx_tourplacepoint_age", columnList = "finalPointValueAge DESC"),
        @Index(name="idx_tourplacepoint_interest", columnList = "finalPointValueInterest DESC")
})
public class TourPlacePoint {
    @Id
    private Member member;
    @Id
    private TourPlace tourPlace;

    private Integer finalPointValueVisit;
    private Integer finalPointValueAge;
    private Integer finalPointValueInterest;
}
