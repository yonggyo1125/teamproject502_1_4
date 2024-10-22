package org.g9project4.member.entities;

import jakarta.persistence.*;
import lombok.*;
import org.g9project4.file.entities.FileInfo;
import org.g9project4.global.entities.BaseEntity;
import org.g9project4.planner.entities.Planner;

import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor @AllArgsConstructor
public class Member extends BaseEntity implements Serializable {
    
    @Id @GeneratedValue
    private Long seq;

    @Column(length=45, nullable = false)
    private String gid;

    @Column(length = 65, unique = true, nullable = false)
    private String email;

    @Column(length = 65,nullable = false)
    private String password;

    @Column(length = 40,nullable = false)
    private String userName;

    @Column(length=15,nullable = false)
    private String mobile;

    @ToString.Exclude
    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Authorities> authorities;

    @ToString.Exclude
    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private transient List<Planner> planners;

    @Transient
    private transient FileInfo profileImage;
}
