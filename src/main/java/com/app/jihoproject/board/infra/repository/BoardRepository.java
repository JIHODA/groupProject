package com.app.jihoproject.board.infra.repository;

import com.app.jihoproject.board.domain.entity.Board;
import com.app.jihoproject.board.domain.entity.BoardCategory;
import com.app.jihoproject.group.domain.entity.GroupEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findAllByBoardCategoryAndGroupEntity(Pageable pageable, BoardCategory boardCategory, GroupEntity groupEntity);
}
