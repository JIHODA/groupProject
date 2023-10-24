package com.app.jihoproject.Skill.endpoint;

import com.app.jihoproject.Skill.application.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class SkillController {

    private final SkillService skillService;


}
