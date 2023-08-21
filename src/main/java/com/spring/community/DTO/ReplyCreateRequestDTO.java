package com.spring.community.DTO;

import com.spring.community.entity.Reply;
import com.spring.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @AllArgsConstructor
@NoArgsConstructor @Builder @ToString
public class ReplyCreateRequestDTO {

    // 글번호, 댓글쓴이, 댓글 내용, 생성일자
    private long postId;
    private String content;
    private User user;
    private LocalDateTime createdAt;

}
