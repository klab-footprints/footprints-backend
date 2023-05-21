package org.konkuk.klab.mtot.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Journey {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "journey_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;


    @ManyToOne(fetch = FetchType.LAZY)
    private Team team;

    @OneToMany(mappedBy = "journey")
    private List<Post> posts = new ArrayList<>();
    @OneToMany(mappedBy = "journey")
    private List<Photo> photos = new ArrayList<>();
    @OneToMany(mappedBy = "journey")
    private List<Locations> locations = new ArrayList<>();

    public Journey(Team team) {
        this.team = team;
    }


}



