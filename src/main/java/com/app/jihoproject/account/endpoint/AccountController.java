package com.app.jihoproject.account.endpoint;

import com.app.jihoproject.Skill.application.service.SkillService;
import com.app.jihoproject.account.application.service.AccountService;
import com.app.jihoproject.account.domain.entity.Account;
import com.app.jihoproject.account.domain.entity.Skill;
import com.app.jihoproject.account.form.SignUpForm;
import com.app.jihoproject.group.application.service.GroupService;
import com.app.jihoproject.group.domain.entity.GroupEntity;
import com.app.jihoproject.profile.form.ProfileForm;
import com.app.jihoproject.support.AccountDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final GroupService groupService;

    @GetMapping("/sign-up")
    public String signUp(Model model){

        model.addAttribute(new SignUpForm());

        return "account/sign-up";
    }

    @PostMapping("/sign-up")
    public String signUpSubmit(@Valid @ModelAttribute SignUpForm signForm, Errors errors){
        if (errors.hasErrors()) {
            return "account/sign-up";
        }

        Account account = accountService.signUp(signForm);
        return "redirect:/";
    }

    @GetMapping("/profile/{nickName}")
    public String viewProfile(@PathVariable String nickName, Model model, @AuthenticationPrincipal AccountDetails accountDetails) {
        Account accountByNickname = accountService.getAccountBy(nickName);
        List<Skill> skillList = accountService.getSkills(accountByNickname);
        accountByNickname.updateProfile(ProfileForm.from(accountByNickname));

        model.addAttribute("skillUrl",skillList.stream().map(Skill::getUrl).collect(Collectors.toList()));
        model.addAttribute("account", accountByNickname);
        model.addAttribute("isOwner", accountByNickname.getNickName().equals(accountDetails.getAccount().getNickName()));
        model.addAttribute("editable", false);
        return "account/profile";
    }

    @GetMapping("/profile/grouplist/{nickName}")
    public String viewGroupList(@PathVariable String nickName, Model model, @AuthenticationPrincipal AccountDetails accountDetails){
        Account accountByNickname = accountService.getAccountBy(nickName);
        List<GroupEntity> groupList = groupService.getGroupList(accountByNickname);

        model.addAttribute("isOwner", accountByNickname.getNickName().equals(accountDetails.getAccount().getNickName()));
        model.addAttribute("account", accountByNickname);
        model.addAttribute("groupList", groupList);
        return "account/grouplist";
    }


}
