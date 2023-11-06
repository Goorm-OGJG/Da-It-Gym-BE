package com.ogjg.daitgym.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class HealthClub extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "healthClub")
    private List<User> users = new ArrayList<>();

    private String name;

    private Double latitude;

    private Double longitude;

    private String address;

    @Builder
    public HealthClub(Long id, List<User> users, String name, Double latitude, Double longitude, String address) {
        this.id = id;
        this.users = users;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    public static HealthClub from(HealthClub healthClub) {
        return HealthClub.builder()
                .users(healthClub.getUsers())
                .name(healthClub.getName())
                .latitude(healthClub.getLatitude())
                .longitude(healthClub.getLongitude())
                .address(healthClub.getAddress())
                .build();
    }
}
