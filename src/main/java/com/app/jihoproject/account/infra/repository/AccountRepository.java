package com.app.jihoproject.account.infra.repository;

import com.app.jihoproject.account.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByEmail(String email);
    boolean existsByNickName(String nickName);

    Account findByEmail(String email);
    Account findByNickName(String nickName);
}
