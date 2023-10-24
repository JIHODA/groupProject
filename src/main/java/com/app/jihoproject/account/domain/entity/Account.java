package com.app.jihoproject.account.domain.entity;

import com.app.jihoproject.domain.entity.AuditingEntity;
import com.app.jihoproject.group.domain.entity.GroupMembers;
import com.app.jihoproject.profile.form.ProfileForm;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Account extends AuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACCOUNT_ID")
    private Long id;

    @Column(unique = true)
    private String nickName;

    private LocalDate birthday;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String role;

    @ManyToMany
    private List<Skill> skills = new ArrayList<>();

    @Embedded
    private Profile profile;

    public static Account with(String email, String nickName, String password, String role, String birthday) {
        Account account = new Account();
        account.email = email;
        account.nickName = nickName;
        account.password = password;
        account.role = role;
        account.birthday = toLocalDate(birthday);
        account.profile = new Profile();
        return account;
    }

    private static LocalDate toLocalDate(String birthday){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return LocalDate.parse(birthday, formatter);
    }

    @Embeddable
    @NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder @Getter @ToString
    public static class Profile {
        private String bio;
        private String url;
        private String location;

        @Lob @Basic(fetch = FetchType.EAGER)
        private String image;
    }

    public void updateProfile(ProfileForm profileForm) {
        if (this.profile == null) {
            this.profile = new Profile();
        }
        this.profile.bio = profileForm.getBio();
        this.profile.url = profileForm.getUrl();
        this.profile.location = profileForm.getLocation();
        this.profile.image = profileForm.getImage();
    }

}
