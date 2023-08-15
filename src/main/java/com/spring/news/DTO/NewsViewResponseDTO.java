package com.spring.news.DTO;

import com.spring.news.entity.News;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewsViewResponseDTO {

    private long newsId;
    private String title;
    private String content;
    private String imageUrl;
    private String newsUrl;
    private LocalDateTime createdAt;

    public NewsViewResponseDTO(News news) {
        this.newsId = news.getNewsId();
        this.title = news.getTitle();
        this.content = news.getContent();
        this.imageUrl = news.getImageUrl();
        this.newsUrl = news.getNewsUrl();
        this.createdAt = news.getCreatedAt();
    }

}
