package com.spring.community.entity;

import com.spring.community.DTO.PostUpdateDTO;
import com.spring.user.DTO.UserUpdateDTO;
import com.spring.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@ToString
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class) //CreatedAt, updatedAt 자동으로 현제시간 설정하는 JPA
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @Column(name="nickname", nullable = false)
    private String nickname;

    @Column(name = "category_name", nullable = false)
    private String categoryName;

    @Column(name = "title")
    private String title;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "view_count")
    private Long viewCount = 0L; // default 0으로 생성

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate //자동으로 생성일자로 설정
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @LastModifiedDate // 자동으로 업데이트일자로 설정
    private LocalDateTime updatedAt;

    @Column(name = "image_url1")
    private String imageUrl1;

    @Column(name = "image_url2")
    private String imageUrl2;

    @Column(name = "image_url3")
    private String imageUrl3;

    public void update(PostUpdateDTO postUpdateDTO) {
        this.postId = postUpdateDTO.getPostId();
        this.nickname = postUpdateDTO.getNickname();
        this.title = postUpdateDTO.getTitle();
        this.content = postUpdateDTO.getContent();
    }

}
