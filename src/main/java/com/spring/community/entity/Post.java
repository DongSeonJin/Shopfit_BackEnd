package com.spring.community.entity;

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
import java.util.List;

@Getter @ToString @Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class) //CreatedAt, updatedAt 자동으로 현제시간 설정하는 JPA
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE) // on delete cascasde; 유저 탈퇴시 게시글도 같이 삭제
    private User user;

    @Column(name="nickname", nullable = false)
    private String nickname;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private PostCategory postCategory;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @ColumnDefault("0")
    @Column(name = "view_count", nullable = false)
    private Long viewCount;

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

    @OneToMany(mappedBy = "post", // post가 부모이므로 매핑
            cascade = CascadeType.REMOVE, //post 삭제시 like 삭제
            orphanRemoval = true, // 연관관계 제거시 like 삭제
            fetch = FetchType.LAZY) // post 엔터티 로드할 때 likes 엔터티를 지연 로딩 -> 성능 최적화
    private List<Like> likes;

}
