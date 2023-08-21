package com.spring.community.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @AllArgsConstructor
@ToString @Builder @NoArgsConstructor
public class ReplyUpdateRequestDTO {

    // 댓글번호, 댓글쓴이, 댓글내용, 업데이트일자

    private long replyId;
    // 댓글쓴이
    private String content;
    private LocalDateTime updatedAt;

}
