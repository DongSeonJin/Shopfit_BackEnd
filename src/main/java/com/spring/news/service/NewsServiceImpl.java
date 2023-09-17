package com.spring.news.service;

import com.spring.exception.CustomException;
import com.spring.exception.ExceptionCode;
import com.spring.news.entity.News;
import com.spring.news.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class NewsServiceImpl implements NewsService{

    private final NewsRepository newsRepository;
    final int PAGE_SIZE = 10;

    @Autowired
    public NewsServiceImpl(NewsRepository newsRepository){
        this.newsRepository = newsRepository;
    }

    //서비스레이어는 테스트 코드 작성하기

    // 리스트 -> 추후 페이징 처리 방식으로 수정해야 함 (1페이지 게시글 10개)
//    @Override
//    public List<News> getAllNews() {
//        return newsRepository.findAll();
//    }


    @Override
    public Page<News> getAllNews(int pageNum) {
        Page<News> allNews = newsRepository.findAll(PageRequest.of(pageNum - 1, PAGE_SIZE, Sort.Direction.DESC, "newsId"));
        if (allNews.getTotalPages() < pageNum) {  // 만약 페이지 번호가 범위를 벗어나는 경우에는 가장 마지막 페이지로 자동으로 이동
            return newsRepository.findAll(PageRequest.of(allNews.getTotalPages() - 1, PAGE_SIZE, Sort.Direction.DESC, "newsId"));
        } else {
            return allNews;
        }
    }


    @Override
    public News getNewsById(long newsId) {
        return newsRepository.findById(newsId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NEWS_ID_NOT_FOUND));
    }

    @Override
    public void deleteNewsById(long newsId) {
        newsRepository.deleteById(newsId);
    }

    // 검색기능 -> 추후 페이징 처리 방식으로 수정해야 함 (1페이지 게시글 10개)
//    @Override
//    public List<News> searchNewsByKeyword(String keyword) {
//        return newsRepository.findByTitleContaining(keyword);
//    }

    @Override
    public Page<News> searchNewsByKeyword(String keyword, int pageNum) {
        Pageable pageable = PageRequest.of(pageNum - 1, PAGE_SIZE, Sort.Direction.DESC, "newsId");
        Page<News> searchResults = newsRepository.findByTitleContainingIgnoreCase(keyword, pageable);

        if (searchResults.getTotalElements() == 0) {
            // 만약 검색 결과가 0일 경우 빈 페이지를 생성하여 반환
            return Page.empty(pageable);
        }

        if (searchResults.getTotalPages() < pageNum) {
            pageable = PageRequest.of(searchResults.getTotalPages() - 1, PAGE_SIZE, Sort.Direction.DESC, "newsId");
            searchResults = newsRepository.findByTitleContainingIgnoreCase(keyword, pageable);
        }

        return searchResults;
    }


}
