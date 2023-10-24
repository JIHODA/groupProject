package com.app.jihoproject.account.application.service;

import com.app.jihoproject.account.domain.entity.Account;
import com.app.jihoproject.account.domain.entity.Skill;
import com.app.jihoproject.account.form.SignUpForm;
import com.app.jihoproject.account.infra.repository.AccountRepository;
import com.app.jihoproject.profile.form.ProfileForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;


    public Account signUp(SignUpForm signUpForm) {
        return saveNewAccount(signUpForm);

    }

    private Account saveNewAccount(SignUpForm signUpForm) {
        Account account = Account.with(signUpForm.getEmail(), signUpForm.getNickName(), passwordEncoder.encode(signUpForm.getPassword()), "ROLE_USER", signUpForm.getBirthday());
        return accountRepository.save(account);
    }

    public Account getAccountBy(String nickname) {
        return Optional.ofNullable(accountRepository.findByNickName(nickname))
                .orElseThrow(() -> new IllegalArgumentException(nickname + "에 해당하는 사용자가 없습니다."));
    }
    public List<Skill> getSkills(Account account){
        List<Skill> skills = accountRepository.findById(account.getId())
                .orElseThrow()
                .getSkills();

        return skills;
    }

    public void addStack(Account account, Skill skill){
        accountRepository.findById(account.getId())
                .ifPresent(a-> {
                    a.getSkills().add(skill);
                });
    }

    public void removeStack(Account account, Skill skill){
        accountRepository.findById(account.getId())
                .map(Account::getSkills)
                .ifPresent(a-> {
                    a.remove(skill);
                });
    }

    public void updateProfile(Account account, ProfileForm profileForm) {
        account.updateProfile(profileForm);
        accountRepository.save(account);
    }
}
