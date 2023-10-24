package com.app.jihoproject.board.application.service;

import com.app.jihoproject.account.domain.entity.Account;
import com.app.jihoproject.account.domain.entity.Skill;
import com.app.jihoproject.board.domain.entity.Board;
import com.app.jihoproject.board.domain.entity.BoardCategory;
import com.app.jihoproject.board.form.BoardForm;
import com.app.jihoproject.board.infra.repository.BoardCategoryRepository;
import com.app.jihoproject.board.infra.repository.BoardRepository;
import com.app.jihoproject.group.domain.entity.GroupEntity;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardCategoryRepository boardCategoryRepository;
    private final BoardRepository boardRepository;

    @PostConstruct
    public void initBoardCategory() throws IOException {
        if (boardCategoryRepository.count() == 0) {
            List<BoardCategory> category = new ArrayList<>();
            category.add(BoardCategory.builder().categoryName("메인공지").build());
            category.add(BoardCategory.builder().categoryName("그룹공지").build());
            category.add(BoardCategory.builder().categoryName("커뮤니티").build());
            boardCategoryRepository.saveAll(category);
        }
    }

    public void saveBoard(Board board){
        boardRepository.save(board);
    }

    public Board initBoard(BoardForm boardForm, GroupEntity groupEntity, Account account,
                              BoardCategory boardCategory){
        if(groupEntity != null){
            return Board.from(boardForm, account, groupEntity, boardCategory, "");
        }else{
            return Board.from(boardForm,account,null,boardCategory,"");
        }
    }

    public BoardCategory getBoardCategory(String categoryName){
        return boardCategoryRepository.findByCategoryName(categoryName);
    }

    public Page<Board> getBoardList(int page, BoardCategory boardCategory, GroupEntity groupEntity) {

        Pageable pageable = PageRequest.of(page, 10, Sort.by("boardId").descending());
        return boardRepository.findAllByBoardCategoryAndGroupEntity(pageable, boardCategory, groupEntity);
    }

    public Board getBoard(Long boardId){
        return boardRepository.findById(boardId).orElseThrow(()->
                new NoSuchElementException("게시글이 없습니다."+boardId)
        );
    }
}
