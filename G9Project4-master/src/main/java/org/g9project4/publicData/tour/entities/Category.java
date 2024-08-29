package org.g9project4.publicData.tour.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id @GeneratedValue
    private Long seq;

    private String category1;
    private String category2;
    private String category3;

    private String name1;
    private String name2;
    private String name3;
}
