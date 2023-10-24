package com.app.jihoproject.Skill.application.service;

import com.app.jihoproject.Skill.infra.repository.SkillRepository;
import com.app.jihoproject.account.domain.entity.Skill;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SkillService {

    private final SkillRepository skillRepository;

    public List<Skill> getKills(){
        return skillRepository.findAll().stream().toList();
    }

    @PostConstruct
    public void initZoneData() throws IOException {
        if (skillRepository.count() == 0) {
            Resource resource = new ClassPathResource("stacks.csv");
            List<String> allLines = Files.readAllLines(resource.getFile().toPath(), StandardCharsets.UTF_8);
            List<Skill> skills = allLines.stream().map(Skill::map).collect(Collectors.toList());
            skillRepository.saveAll(skills);
        }
    }
}
