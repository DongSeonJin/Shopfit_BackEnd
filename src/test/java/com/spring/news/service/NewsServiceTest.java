package com.spring.news.service;

import com.spring.news.entity.News;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class NewsServiceTest {

    @Autowired
    NewsService newsService;

    @Test
    @Transactional
    public void getAllNewsTest(){
        // given: 테스트용으로 더미데이터 10개 DB에 적재
        // when: 전체 데이터 가져오기
        List<News> allNewsList = newsService.getAllNews();
        //then: 길이가 10일 것이다
        assertThat(allNewsList.size()).isEqualTo(10);
    }

    @Test
    @Transactional
    public void deleteNewsByIdTest() {
        // given: 테스트용 더미 데이터를 추가하여 DB에 적재
        News news = new News();
        // when: 해당 뉴스를 삭제
        newsService.deleteNewsById(news.getNewsId());
        // then: 해당 뉴스가 삭제되었으므로 가져오지 못해야 함
        News deletedNews = newsService.getNewsById(news.getNewsId());
        assertThat(deletedNews).isNull();
    }


}
