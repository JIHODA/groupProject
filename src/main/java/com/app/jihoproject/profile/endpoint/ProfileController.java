package com.app.jihoproject.profile.endpoint;

import com.app.jihoproject.Skill.application.service.SkillService;
import com.app.jihoproject.Skill.form.SkillForm;
import com.app.jihoproject.Skill.infra.repository.SkillRepository;
import com.app.jihoproject.account.application.service.AccountService;
import com.app.jihoproject.account.domain.entity.Account;
import com.app.jihoproject.account.domain.entity.Skill;
import com.app.jihoproject.profile.form.ProfileForm;
import com.app.jihoproject.support.AccountDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final AccountService accountService;
    private final SkillService skillService;
    private final SkillRepository skillRepository;
    private final ObjectMapper objectMapper;

    @GetMapping("/setting/profile")
    public String settingProfile(@AuthenticationPrincipal AccountDetails accountDetails, Model model){
        List<Skill> skills = accountService.getSkills(accountDetails.getAccount());
        model.addAttribute("skills",skills.stream().map(Skill::toString).collect(Collectors.toList()));

        List<String> skillList = skillService.getKills()
                .stream()
                .map(Skill::getSkillName)
                .collect(Collectors.toList());
        String whitelist = null;
        try {
            whitelist = objectMapper.writeValueAsString(skillList);
        }catch (Exception e){
            e.printStackTrace();
        }
        Account account = accountDetails.getAccount();
        account.updateProfile(ProfileForm.from(account));

        model.addAttribute("skillUrl", skills.stream().map(Skill::getUrl).collect(Collectors.toList()));
        model.addAttribute("whitelist",whitelist);
        model.addAttribute("account", account);
        model.addAttribute("isOwner", true);
        model.addAttribute("editable", true);
    return "/account/profile";
    }

    @PostMapping("/setting/profile")
    public String settingProfileSubmit(@AuthenticationPrincipal AccountDetails accountDetails,
                                       ProfileForm profileForm){
        accountService.updateProfile(accountDetails.getAccount(), profileForm);
        return "redirect:/profile/"+ URLEncoder.encode(accountDetails.getAccount().getNickName(), StandardCharsets.UTF_8);
    }

    @PostMapping("/setting/profile/add")
    @ResponseStatus(HttpStatus.OK)
    public void addZone(@AuthenticationPrincipal AccountDetails accountDetails, @RequestBody SkillForm skillForm) {
        Skill skill = skillRepository.findBySkillName(skillForm.getSkillName());
        accountService.addStack(accountDetails.getAccount(), skill);
    }

    @PostMapping("/setting/profile/remove")
    @ResponseStatus(HttpStatus.OK)
    public void removeZone(@AuthenticationPrincipal AccountDetails accountDetails, @RequestBody SkillForm skillForm) {
        Skill skill = skillRepository.findBySkillName(skillForm.getSkillName());
        accountService.removeStack(accountDetails.getAccount(), skill);
    }
}
