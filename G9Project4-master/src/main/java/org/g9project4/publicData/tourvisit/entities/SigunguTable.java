package org.g9project4.publicData.tourvisit.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SigunguTable {


    @Id   @Column(length = 10)
    private String sigunguCode2;
    @Column(length = 20)
    private String sigunguName;
    @Column(length = 10)
    private String sidoCode;
}
