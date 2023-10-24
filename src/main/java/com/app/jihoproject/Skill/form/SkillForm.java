package com.app.jihoproject.Skill.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillForm {

    private String skillName;
}
