package com.spring.news.service;

import com.spring.news.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NewsService {

//    List<News> getAllNews();
    Page<News> getAllNews(int pageNum);

    News getNewsById(long newsId);


    void deleteNewsById(long newsId);

    //검색기능
//    List<News> searchNewsByKeyword(String keyword);
    Page<News> searchNewsByKeyword(String keyword, int pageNum);
}
