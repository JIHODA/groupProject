package com.app.jihoproject.group.domain.entity;

import com.app.jihoproject.account.domain.entity.Account;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@EntityListeners(AuditingEntityListener.class)
public class GroupMembers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_members_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GROUP_ID")
    private GroupEntity groupEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    @CreatedDate
    private LocalDate join_date;

    private String role;

    public static GroupMembers addmember(GroupEntity groupEntity, Account account, String role){
        return GroupMembers.builder()
                .groupEntity(groupEntity)
                .account(account)
                .role(role)
                .build();
    }

}
