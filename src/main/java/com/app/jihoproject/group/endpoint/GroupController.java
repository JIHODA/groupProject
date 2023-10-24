package com.app.jihoproject.group.endpoint;

import com.app.jihoproject.account.application.service.AccountService;
import com.app.jihoproject.account.domain.entity.Account;
import com.app.jihoproject.board.application.service.BoardService;
import com.app.jihoproject.board.domain.entity.Board;
import com.app.jihoproject.board.domain.entity.BoardCategory;
import com.app.jihoproject.board.form.BoardForm;
import com.app.jihoproject.group.domain.entity.GroupMembers;
import com.app.jihoproject.support.AccountDetails;
import com.app.jihoproject.group.application.service.GroupService;
import com.app.jihoproject.group.domain.entity.GroupEntity;
import com.app.jihoproject.group.form.GroupForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;
    private final BoardService boardService;
    private final AccountService accountService;

    @GetMapping("/newgroup")
    public String newGroup(Model model){
        model.addAttribute(new GroupForm());
        return "group/form";
    }

    @PostMapping("/newgroup")
    public String newGroupSubmit(@Valid @ModelAttribute GroupForm groupForm, @AuthenticationPrincipal AccountDetails accountDetails, Errors errors){
        if (errors.hasErrors()) {
            return "group/form";
        }

        GroupEntity newGroup = groupService.createGroup(groupForm, accountDetails.getAccount());

        return "redirect:/group/"+ URLEncoder.encode(newGroup.getPath(), StandardCharsets.UTF_8);
    }

    @GetMapping("/group/{path}")
    public String viewGroup(@PathVariable String path, Model model, @AuthenticationPrincipal AccountDetails accountDetails) {
        GroupEntity group = groupService.getGroup(path);
        GroupMembers manager = groupService.getManager(group);

        String managerNickName = manager.getAccount().getNickName();

        model.addAttribute("managerNickName", managerNickName);
        model.addAttribute("group", group);
        return "group/view";
    }

    @GetMapping("/group/{path}/notice")
    public String viewGroupNotice(@PathVariable("path") String path, Model model,
                                  @RequestParam(value="page", defaultValue="0") int page, @AuthenticationPrincipal AccountDetails accountDetails){
        GroupEntity group = groupService.getGroup(path);
        BoardCategory boardCategory = boardService.getBoardCategory("그룹공지");;

        Page<Board> board = boardService.getBoardList(page, boardCategory , group);

        model.addAttribute("current", "notice");
        model.addAttribute("boardList", board);
        model.addAttribute("group", group);

        return "group/board/notice";
    }

    @GetMapping("/group/{path}/community")
    public String viewGroupBoard(@PathVariable("path") String path, Model model,
                                     @RequestParam(value="page", defaultValue="0") int page, @AuthenticationPrincipal AccountDetails accountDetails){
        GroupEntity group = groupService.getGroup(path);
        BoardCategory boardCategory = boardService.getBoardCategory("커뮤니티");;

        Page<Board> board = boardService.getBoardList(page, boardCategory , group);

        model.addAttribute("current", "community");
        model.addAttribute("boardList", board);
        model.addAttribute("group", group);

        return "group/board/community";
    }

    @GetMapping("/group/{path}/{current}/write")
    public String viewWrite(@PathVariable("path") String path, @PathVariable("current") String current, Model model,
                                  @AuthenticationPrincipal AccountDetails accountDetails){
        GroupEntity group = groupService.getGroup(path);
        if((current.equals("notice") && !group.isManager(accountDetails)) ||
                (current.equals("community") && !group.isMember(accountDetails))){
            return "redirect:/";
        }

        model.addAttribute("group",group);
        model.addAttribute("current", current);
        return "group/board/write";
    }

    @PostMapping("/group/{path}/{current}/write")
    public String noticeWriteSubmit(@PathVariable("path") String path, @PathVariable("current") String current, Model model,
                                    BoardForm boardForm, @AuthenticationPrincipal AccountDetails accountDetails){
        GroupEntity groupEntity = groupService.getGroup(path);
        BoardCategory boardCategory = null;
        Account account = accountDetails.getAccount();
        if(current.equals("notice")) {
            if (!groupEntity.isManager(accountDetails)) {
                return "redirect:/";
            }else {
                boardCategory = boardService.getBoardCategory("그룹공지");
            }
        } else {
            if (!groupEntity.isMember(accountDetails)) {
                return "redirect:/";
            }else {
                boardCategory = boardService.getBoardCategory("커뮤니티");
            }
        }

        boardService.saveBoard(boardService.initBoard(boardForm, groupEntity, account, boardCategory));

        return "redirect:/group/"+URLEncoder.encode(path, StandardCharsets.UTF_8)+"/"+current;
    }

    @GetMapping("/group/{path}/{current}/{boardId}")
    public String viewBoard(@PathVariable("path") String path, @PathVariable("current") String current, @PathVariable("boardId") Long boardId,
                        Model model, @AuthenticationPrincipal AccountDetails accountDetails){
        GroupEntity group = groupService.getGroup(path);
        Board board = boardService.getBoard(boardId);

        model.addAttribute("board", board);
        model.addAttribute("group",group);
        model.addAttribute("current", current);
        return "group/board/view";
    }

    @PostMapping("/group/{path}/join")
    public String joinMember(@PathVariable("path") String path,@AuthenticationPrincipal AccountDetails accountDetails){
        GroupEntity group = groupService.getGroup(path);
        Account account = accountDetails.getAccount();

        groupService.saveMember(group, account);

        return "redirect:/group/"+URLEncoder.encode(path, StandardCharsets.UTF_8);
    }

    @PostMapping("/group/{path}/remove")
    public String removeMember(@PathVariable("path") String path, String nickName){
        Account account = accountService.getAccountBy(nickName);
        GroupEntity group = groupService.getGroup(path);

        groupService.removeMember(group, account);

        return "redirect:/group/"+URLEncoder.encode(path, StandardCharsets.UTF_8);
    }

}
