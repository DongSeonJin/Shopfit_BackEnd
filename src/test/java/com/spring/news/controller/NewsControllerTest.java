package com.spring.news.controller;

import com.spring.news.repository.NewsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class NewsControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    NewsRepository newsRepository;

//    @Test
//    @Transactional
//    public void getAllNewsTest() throws Exception {
//        // given : fixture 설정, 접속 주소 저장
//        long newsId = 1;
//        String title = "Title 1";
//        String content = "Content 1";
//        String imgUrl = "Img Url1";
//        String newsUrl = "News Url1";
//        LocalDateTime createdAt = LocalDateTime.of(2023, 8, 12, 0, 0, 0);
//        String url = "/news";
//
//        newsRepository.save(News.builder()
//                .newsId(newsId)
//                .title(title)
//                .content(content)
//                .imageUrl(imgUrl)
//                .newsUrl(newsUrl)
//                .createdAt(createdAt)
//                .build());
//        System.out.println(newsRepository.findAll());
//        // when : 설정한 url 주소로 접속 후 json 데이터 리턴받아 저장하기
//        ResultActions resultActions = mockMvc.perform(get(url)
//                .accept(MediaType.APPLICATION_JSON));
//        // then: 응답 데이터와 fixture 비교하여 검증하기
//
//        resultActions
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].content").value(content))
//                .andExpect(jsonPath("$[0].title").value(title));
//
//    }

    @Test
    public void getAllNewsTest() throws Exception {
        // given: 더미 데이터를 DB에 적재함, url fixture 세팅
        String url = "/news/list/1";

        // when: /news/list/1 로 Get 요청 수행 후 결과를 저장하기
        ResultActions resultActions = mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON));

        // then: 응답코드 200, 본문 내용은 배열, 그 배열의 길이는 3
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(3));
    }

    @Test
    public void redirectToNewsSiteTest() throws Exception {
        // given: 테스트용 데이터베이스에 존재하는 뉴스의 ID fixture, 요청 경로 세팅
        long newsId = 1L;
        String url = "/news/" + newsId;

        // when: 세팅한 경로로 Get 요청 수행 후 결과 저장
        ResultActions resultActions = mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON));

        // then: 예상한 경로로 리다이렉트되는지 확인
        resultActions
                .andExpect(redirectedUrl("https://google.com"));
    }

    @Test
    @Transactional
    public void testDeleteNewsById() throws Exception {
        // given: fixture 세팅
        long newsId = 1L;
        String url = "/news/" + newsId;

        // when: 삭제로직 실행
        ResultActions resultActions = mockMvc.perform(delete(url)
                .contentType(MediaType.APPLICATION_JSON));

        // then: 응답코드는 200이다
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void testSearchNews() throws Exception {
        // given: 테스트용 데이터베이스에 존재하는 뉴스의 키워드, 페이지번호 및 url fixture 설정
        String keyword = "Breaking";
        int pageNum = 1;
        String url = "/news/search/" + keyword + "/" + pageNum;

        // when: 검색 로직 실행
        ResultActions resultActions = mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON));

        // then: 응답코드 200, 본문 내용은 배열, 그 배열의 길이는 1개
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1));
    }


}

//    /*Dummy Data input query (TEST DB ONLY)*/
//    INSERT INTO news (news_id, title, content, image_url, news_url, created_at)
//    VALUES
//            (1, 'Breaking News: Exciting Discoveries on Mars', 'Scientists have made groundbreaking discoveries...', 'https://image-url.com/1', 'https://google.com', '2023-08-10 10:30:00'),
//            (2, 'New Study Reveals Surprising Benefits of Chocolate', 'A recent study suggests that chocolate...', 'https://image-url.com/2', 'https://news-url.com/2', '2023-08-09 15:45:00'),
//            (3, 'Tech Giant Unveils Revolutionary AI Product', 'The leading tech company introduced a cutting-edge AI...', 'https://image-url.com/3', 'https://news-url.com/3', '2023-08-08 09:20:00');