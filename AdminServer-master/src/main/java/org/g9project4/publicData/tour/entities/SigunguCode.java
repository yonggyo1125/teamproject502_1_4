package org.g9project4.publicData.tour.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(SigunguCodeId.class)
public class SigunguCode {
    @Id
    private String areaCode;
    @Id
    private String sigunguCode;
    private String name;

}
