package com.spring.community.DTO;

import com.spring.community.entity.Reply;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @AllArgsConstructor
@ToString @Builder @NoArgsConstructor
public class ReplyUpdateRequestDTO {

    // 댓글번호, 댓글쓴이, 댓글내용, 업데이트일자

    private long replyId;
    private String nickname;
    private String content;
    private LocalDateTime updatedAt;

    public ReplyUpdateRequestDTO(Reply reply) {
        this.replyId = reply.getReplyId();
        this.nickname = reply.getUser().getNickname();
        this.content = reply.getContent();
        this.updatedAt = reply.getUpdatedAt();
    }

}
