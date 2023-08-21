package com.spring.community.DTO;

import com.spring.community.entity.Reply;
import com.spring.user.entity.User;
import lombok.*;

//@AllArgsConstructor
@Getter
@Setter
@Builder @ToString
@NoArgsConstructor
public class ReplyCreateRequestDTO {
    // 글번호, 댓글 내용, 글쓴이
    private long postId;
    private String content;
    private User user;

    public ReplyCreateRequestDTO(long postId, String content, User user) {
        this.postId = postId;
        this.content = content;
        this.user = user;
    }

}
