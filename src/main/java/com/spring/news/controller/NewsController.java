package com.spring.news.controller;


import com.spring.news.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/news")
public class NewsController {

    private NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService){
        this.newsService = newsService;
    }

    // 컨트롤러 레이어는 테스트 코드 작성하기


    //뉴스 목록 조회 - GET - /


    // 개별 뉴스 조회 - GET - /{newsId}


    // ID로 뉴스 삭제  - DELETE - /{newsId}


    // 검색 - Get - /"search"











}
