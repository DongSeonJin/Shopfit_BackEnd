package com.spring.community.DTO;

import com.spring.community.entity.Reply;
import com.spring.user.entity.User;
import lombok.*;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.time.LocalDateTime;

@Getter @Setter @AllArgsConstructor
@NoArgsConstructor @ToString @Builder
public class ReplyResponseDTO {

    // 댓글 번호, 댓글내용, 생성일자, 업데이트일자
    private long replyId;
    private String content;
    private String nickname;
    private String profileImage;

    public ReplyResponseDTO(Reply reply) {
        this.replyId = reply.getReplyId();
        this.content = reply.getContent();
        this.nickname = reply.getUser().getNickname();
        this.profileImage = reply.getUser().getImageUrl();
    }
}
