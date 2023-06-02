package org.konkuk.klab.mtot.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberTeam {

    @Id @GeneratedValue
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    private Team team;

    public MemberTeam(Member member, Team team){
        addMember(member);
        addTeam(team);
    }

    private void addTeam(Team team) {
        this.team = team;
        team.getMemberTeams().add(this);
    }

    private void addMember(Member member) {
        this.member = member;
        member.getMemberTeams().add(this);
    }
}
