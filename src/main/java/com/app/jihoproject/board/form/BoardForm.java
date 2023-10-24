package com.app.jihoproject.board.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardForm {

    private String title;
    private String content;
    private String writer;
    private LocalDate regDate;
    private Integer hit;

}
