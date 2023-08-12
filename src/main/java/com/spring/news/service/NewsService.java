package com.spring.news.service;

import com.spring.news.DTO.NewsViewResponseDTO;
import com.spring.news.entity.News;

import java.util.List;

public interface NewsService {

    List<News> getAllNews();

    void deleteNewsById(long id);

    //검색기능
    List<News> searchNewsByKeyword(String keyword);
}
