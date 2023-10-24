package com.app.jihoproject.account.domain.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SKILL_ID")
    private Long id;

    @Column(nullable = false)
    private String skillName;

    @Column(nullable = false)
    private String url;

    public static Skill map(String line) {
        String[] split = line.split(",");
        Skill skill = new Skill();
        skill.skillName = split[0];
        skill.url = split[1];
        return skill;
    }

    @Override
    public String toString(){
        return skillName;
    }
}
