package com.spring.community.DTO;

import com.spring.community.entity.Reply;
import com.spring.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @AllArgsConstructor
@NoArgsConstructor @Builder @ToString
public class ReplyCreateRequestDTO {

    // 글번호, 댓글쓴이, 댓글 내용
    private Long userId;
    private Long postId;
    private String content;
    private String nickname;

    public ReplyCreateRequestDTO(Reply reply) {
        this.userId = reply.getUser().getUserId();
        this.postId = reply.getPost().getPostId();
        this.content = reply.getContent();
        this.nickname = reply.getUser().getNickname();

    }

}
