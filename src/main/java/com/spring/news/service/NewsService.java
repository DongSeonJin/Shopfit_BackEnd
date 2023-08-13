package com.spring.news.service;

import com.spring.news.entity.News;

import java.util.List;

public interface NewsService {

    List<News> getAllNews();


    News getNewsById(long newsId);

    void deleteNewsById(long newsId);

    //검색기능
    List<News> searchNewsByKeyword(String keyword);
}
