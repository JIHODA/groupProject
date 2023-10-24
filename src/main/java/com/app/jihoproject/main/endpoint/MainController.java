package com.app.jihoproject.main.endpoint;

import com.app.jihoproject.board.domain.entity.Board;
import com.app.jihoproject.board.domain.entity.BoardCategory;
import com.app.jihoproject.group.domain.entity.GroupEntity;
import com.app.jihoproject.support.AccountDetails;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    @GetMapping("/")
    public String home(@AuthenticationPrincipal AccountDetails accountDetails, Model model){

        if (accountDetails != null) {
            model.addAttribute(accountDetails.getAccount());
        }

        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

}
