package com.spring.news.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Entity
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "news")
public class News {
    @Id
    @Column(name = "news_id", updatable=false)
    private long newsId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "news_url")
    private String newsUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;


}
