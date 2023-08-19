package com.spring.news.controller;


import com.spring.news.DTO.NewsViewResponseDTO;
import com.spring.news.entity.News;
import com.spring.news.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/news")
public class NewsController {

    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    // 컨트롤러 레이어는 테스트 코드 작성하기


    // 뉴스 목록 조회 - GET - /
//    @GetMapping
//    public ResponseEntity<List<NewsViewResponseDTO>> findAllNews() {
//        List<NewsViewResponseDTO> news = newsService.getAllNews()
//                .stream()
//                .map(NewsViewResponseDTO::new)
//                .toList();
//
//        return ResponseEntity.ok(news);
//    }

    @GetMapping({"/list/{pageNum}", "/list"})
    public ResponseEntity<Page<NewsViewResponseDTO>> getAllNews(@PathVariable(required = false) Integer pageNum) {
        int currentPageNum = pageNum != null ? pageNum : 1;
        Page<NewsViewResponseDTO> newsPage = newsService.getAllNews(currentPageNum)
                .map(NewsViewResponseDTO::new); //News를 NewsViewResponseDTO로 변환하기
        return ResponseEntity.ok(newsPage);
    }


    // 개별 뉴스 조회 - GET - /{newsId}
    @GetMapping("/{newsId}")
    public RedirectView redirectToNewsSite(@PathVariable long newsId) {
        News news = newsService.getNewsById(newsId);
        String newsUrl = news.getNewsUrl();

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(newsUrl);

        return redirectView;
    }


    // ID로 뉴스 삭제  - DELETE - /{newsId}
    @DeleteMapping("/{newsId}")
    public ResponseEntity<String> deleteNewsById(@PathVariable long newsId) {
        newsService.deleteNewsById(newsId);

        return ResponseEntity.ok("뉴스가 삭제되었습니다");
    }


    // 검색 - Get - /"search"
//    @GetMapping("/search")
//    public ResponseEntity<List<NewsViewResponseDTO>> searchNews(@RequestParam String keyword) {
//        List<News> searchResults = newsService.searchNewsByKeyword(keyword);
//
//        List<NewsViewResponseDTO> searchResultsDTO = searchResults.stream()
//                .map(NewsViewResponseDTO::new)
//                .collect(Collectors.toList());
//
//        return ResponseEntity.ok(searchResultsDTO);
//    }

    ///news/search/example(키워드)/1(페이지번호) 형식으로 매핑
    @GetMapping({"/search/{keyword}/{pageNum}", "/search/{keyword}"})
    public ResponseEntity<Page<NewsViewResponseDTO>> searchNews(@PathVariable String keyword,
                                                                @PathVariable(required = false) Integer pageNum) {
        int currentPageNum = pageNum != null ? pageNum : 1;
        Page<News> searchResultsPage = newsService.searchNewsByKeyword(keyword, currentPageNum);

        Page<NewsViewResponseDTO> searchResultsDTOPage = searchResultsPage
                .map(NewsViewResponseDTO::new);

        return ResponseEntity.ok(searchResultsDTOPage);
    }

}
