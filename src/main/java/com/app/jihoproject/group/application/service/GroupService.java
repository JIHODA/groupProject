package com.app.jihoproject.group.application.service;

import com.app.jihoproject.account.domain.entity.Account;
import com.app.jihoproject.account.form.SignUpForm;
import com.app.jihoproject.group.domain.entity.GroupEntity;
import com.app.jihoproject.group.domain.entity.GroupMembers;
import com.app.jihoproject.group.form.GroupForm;
import com.app.jihoproject.group.infra.repository.GroupMemberRepository;
import com.app.jihoproject.group.infra.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.provisioning.GroupManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;

    public GroupEntity createGroup(GroupForm groupForm, Account account) {
        GroupEntity groupEntity = saveNewGroup(groupForm);
        saveManager(groupEntity, account);

        return groupEntity;
    }

    public void deleteGroup(GroupEntity groupEntity){
        groupRepository.delete(groupEntity);
    }

    private GroupEntity saveNewGroup(GroupForm groupForm) {
        GroupEntity groupEntity = GroupEntity.from(groupForm);

        return groupRepository.save(groupEntity);
    }

    private void saveManager(GroupEntity groupEntity, Account account){
        groupMemberRepository.save(GroupMembers.addmember(groupEntity,account,"manager"));
    }

    public void saveMember(GroupEntity groupEntity, Account account){
        groupMemberRepository.save(GroupMembers.addmember(groupEntity,account,"member"));
    }

    public void removeMember(GroupEntity groupEntity, Account account){
        GroupMembers groupmembers = groupMemberRepository.findByGroupEntityIdAndAccount_Id(groupEntity.getId(), account.getId());
        groupEntity.removeMember(groupmembers);
        groupRepository.save(groupEntity);
    }

    public GroupEntity getGroup(Account account, String path) {
        GroupEntity groupEntity = groupRepository.findByPath(path);
        checkGroupExists(path, groupEntity);
        return groupEntity;
    }

    public GroupEntity getGroup(String path) {
        GroupEntity groupEntity = groupRepository.findByPath(path);
        checkGroupExists(path, groupEntity);
        return groupEntity;
    }

    public List<GroupEntity> getGroupList(Account account){
        List<GroupMembers> byAccountId = groupMemberRepository.findByAccountId(account.getId());
        List<GroupEntity> groupList = new ArrayList<>();
        for(GroupMembers gm : byAccountId){
            groupList.add(gm.getGroupEntity());
        }
        return groupList;
    }

    private void checkGroupExists(String path, GroupEntity groupEntity) {
        if (groupEntity == null) {
            throw new IllegalArgumentException(path + "에 해당하는 모임이 없습니다.");
        }
    }

    public GroupMembers getManager(GroupEntity groupEntity){
        GroupMembers groupMembers = null;
        for(GroupMembers gms : groupEntity.getMembers()){
            if(gms.getRole().equals("manager")){
                groupMembers = gms;
            }
        }
        return groupMembers;
    }
}
