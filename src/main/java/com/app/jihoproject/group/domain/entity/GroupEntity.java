package com.app.jihoproject.group.domain.entity;

import com.app.jihoproject.account.domain.entity.Account;
import com.app.jihoproject.board.domain.entity.Board;
import com.app.jihoproject.group.form.GroupForm;
import com.app.jihoproject.support.AccountDetails;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@EntityListeners(AuditingEntityListener.class)
public class GroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long id;

    @Column(unique = true)
    private String path;

    private String title;

    private String description;

    private Boolean isOpen;

    @CreatedDate
    private LocalDate created;

    @OneToMany(mappedBy = "groupEntity", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<GroupMembers> members = new ArrayList<>();

    @OneToMany(mappedBy = "groupEntity", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Board> boards = new ArrayList<>();

    public static GroupEntity from(GroupForm groupForm){
        return GroupEntity.builder()
                .path(groupForm.getPath())
                .title(groupForm.getTitle())
                .description(groupForm.getDescription())
                .isOpen(groupForm.getIsOpen())
                .build();
    }

    public boolean isMember(AccountDetails accountDetails){
       for(GroupMembers mem : this.members){
           if(mem.getAccount().getId().equals(accountDetails.getAccount().getId())){
               return true;
           }
       }
        return false;
    }

    public boolean isManager(AccountDetails accountDetails){

        for(GroupMembers mem : this.members){
            if(mem.getRole().equals("manager")){
                mem.getAccount().getId();
                return mem.getAccount().getId().equals(accountDetails.getAccount().getId());
            }
        }
        return false;
    }

    public void removeMember(GroupMembers groupMembers){
        this.members.remove(groupMembers);
    }
}
