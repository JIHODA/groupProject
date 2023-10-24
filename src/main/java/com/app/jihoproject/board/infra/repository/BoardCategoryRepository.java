package com.app.jihoproject.board.infra.repository;

import com.app.jihoproject.board.domain.entity.BoardCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardCategoryRepository extends JpaRepository<BoardCategory, Long> {
    BoardCategory findByCategoryName(String categoryName);
}
