package com.spring.community.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.spring.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @ToString @Builder
@Entity @Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class) //CreatedAt, updatedAt 자동으로 현제시간 설정하는 JPA
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE) // on delete cascasde; 유저 탈퇴시 게시글도 같이 삭제
    private User user;

    @Column(name="nickname", nullable = false)
    private String nickname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private PostCategory postCategory;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "view_count", nullable = false)
    @Builder.Default // viewcount의 default값을 0으로 설정할 시, 넣어야 할 어노테이션. 없으면 builder가 무시됨.
    private Long viewCount = 0L;

    @Column(name = "created_at")
    @CreatedDate //자동으로 생성일자로 설정
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate // 자동으로 업데이트일자로 설정
    private LocalDateTime updatedAt;

    @Column(name = "image_url1", nullable = true, columnDefinition = "TEXT")
    @Lob
    private String imageUrl1;

    @Column(name = "image_url2", nullable = true, columnDefinition = "TEXT")
    @Lob
    private String imageUrl2;

    @Column(name = "image_url3", nullable = true, columnDefinition = "TEXT")
    @Lob
    private String imageUrl3;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY) // reply에 Lazy 속성 추가 예정
    private List<Reply> replies = new ArrayList<>();



}
