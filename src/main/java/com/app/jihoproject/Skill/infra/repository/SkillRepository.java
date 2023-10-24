package com.app.jihoproject.Skill.infra.repository;

import com.app.jihoproject.account.domain.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkillRepository extends JpaRepository<Skill,Long> {

    Skill findBySkillName(String skillName);
}
