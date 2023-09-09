package com.spring.community.DTO;

import com.spring.community.entity.Post;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PostListResponseDTO {

    private Long postId;
    private String nickname;
    private String title;
    private LocalDateTime updatedAt;
    private Long viewCount;
    private String imageUrl1;
    private Long likeCnt;
    //좋아요도 묶어서 조회
    private Long replyCnt;

    public PostListResponseDTO(Post post) {
        this.postId = post.getPostId();
        this.nickname = post.getNickname();
        this.title = post.getTitle();
        this.updatedAt = post.getUpdatedAt();
        this.viewCount = post.getViewCount();
        this.imageUrl1 = post.getImageUrl1();
        this.likeCnt = getLikeCnt();
//        this.replyCnt = 0L; // 실제 값을 출력하는 것은 Controller 에서 담당하기 때문에 0으로 초기화만 해둔다.
        this.replyCnt = getReplyCnt();
    }


}
