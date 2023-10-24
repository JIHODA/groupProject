package com.app.jihoproject.group.infra.repository;

import com.app.jihoproject.group.domain.entity.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<GroupEntity, Long> {

    GroupEntity findByPath(String path);

    boolean existsByPath(String path);
}
