package com.app.jihoproject.board.domain.entity;

import com.app.jihoproject.account.domain.entity.Account;
import com.app.jihoproject.board.form.BoardForm;
import com.app.jihoproject.group.domain.entity.GroupEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder @Getter
public class Board{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID")
    private Long boardId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "MEDIUMTEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID", nullable = false)
    private Account writer;

    private Integer hit;

    @CreatedDate
    @Column(name = "REG_DATE", updatable = false)
    private LocalDate regDate;

    @LastModifiedDate
    @Column(name = "MODIFY_DATE")
    private LocalDate modifyDate;

    @ManyToOne
    @JoinColumn(name = "BOARD_CATEGORY_ID")
    private BoardCategory boardCategory;

    private String boardType;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "GROUP_ID")
    private GroupEntity groupEntity;

    public static Board from(BoardForm boardForm, Account account, GroupEntity groupEntity,
                      BoardCategory boardCategory, String boardType){
        return Board.builder()
                .title(boardForm.getTitle())
                .content(boardForm.getContent())
                .writer(account)
                .groupEntity(groupEntity)
                .boardCategory(boardCategory)
                .boardType(boardType)
                .build();
    }
}
