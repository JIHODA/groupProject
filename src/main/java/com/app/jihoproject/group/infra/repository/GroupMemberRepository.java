package com.app.jihoproject.group.infra.repository;

import com.app.jihoproject.account.domain.entity.Account;
import com.app.jihoproject.group.domain.entity.GroupMembers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupMemberRepository extends JpaRepository<GroupMembers, Long> {

    List<GroupMembers> findByAccountId(Long account_id);

    GroupMembers findByGroupEntityIdAndAccount_Id(Long groupId, Long accountId);
}
