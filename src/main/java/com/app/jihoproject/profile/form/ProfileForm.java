package com.app.jihoproject.profile.form;

import com.app.jihoproject.account.domain.entity.Account;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import java.util.Optional;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class ProfileForm {

    @Length(max = 50)
    private String bio;

    @Length(max = 50)
    private String url;

    @Length(max = 50)
    private String location;

    private String image;

    protected ProfileForm(Account account) {
        this.bio = Optional.ofNullable(account.getProfile()).map(Account.Profile::getBio).orElse(null);
        this.url = Optional.ofNullable(account.getProfile()).map(Account.Profile::getUrl).orElse(null);
        this.location = Optional.ofNullable(account.getProfile()).map(Account.Profile::getLocation).orElse(null);
        this.image = Optional.ofNullable(account.getProfile()).map(Account.Profile::getImage).orElse(null);
    }

    public static ProfileForm from(Account account) {
        return new ProfileForm(account);
    }

}
