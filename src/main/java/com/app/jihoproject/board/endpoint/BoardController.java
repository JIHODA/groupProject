package com.app.jihoproject.board.endpoint;

import com.app.jihoproject.account.domain.entity.Account;
import com.app.jihoproject.board.application.service.BoardService;
import com.app.jihoproject.board.domain.entity.Board;
import com.app.jihoproject.board.domain.entity.BoardCategory;
import com.app.jihoproject.board.form.BoardForm;
import com.app.jihoproject.board.infra.repository.BoardCategoryRepository;
import com.app.jihoproject.board.infra.repository.BoardRepository;
import com.app.jihoproject.group.domain.entity.GroupEntity;
import com.app.jihoproject.support.AccountDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/notice")
    public String viewMainNotice(Model model, @RequestParam(value="page", defaultValue="0") int page,
                                 @AuthenticationPrincipal AccountDetails accountDetails){

        BoardCategory boardCategory = boardService.getBoardCategory("메인공지");;

        Page<Board> board = boardService.getBoardList(page, boardCategory , null);

        Boolean isAdmin = false;
        if(accountDetails != null){
            if(accountDetails.getAccount().getRole().equals("ROLE_ADMIN")){
                isAdmin = true;
            }
        }

        model.addAttribute("isAdmin" , isAdmin);
        model.addAttribute("boardList", board);

        return "notice";
    }

    @GetMapping("/notice/write")
    public String viewMainWrite(Model model, @AuthenticationPrincipal AccountDetails accountDetails){

        return "group/board/write";
    }

    @PostMapping("/notice/write")
    public String mainNoticeSubmit(Model model, BoardForm boardForm, @AuthenticationPrincipal AccountDetails accountDetails){

        BoardCategory boardCategory = boardService.getBoardCategory("메인공지");
        Account account = accountDetails.getAccount();

        boardService.saveBoard(boardService.initBoard(boardForm, null, account, boardCategory));

        return "redirect:/notice";
    }

    @GetMapping("/notice/{boardId}")
    public String viewNoticeDetail(String current, @PathVariable("boardId") Long boardId,
                            Model model, @AuthenticationPrincipal AccountDetails accountDetails){

        Board board = boardService.getBoard(boardId);

        model.addAttribute("board", board);

        return "group/board/view";
    }
}
