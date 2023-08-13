package com.spring.news.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.news.entity.News;
import com.spring.news.repository.NewsRepository;
import com.spring.news.service.NewsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;


import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class NewsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    NewsRepository newsRepository;

    @Test
    @Transactional
    public void findAllNewsTest() throws Exception {
        // given : fixture 설정, 접속 주소 저장
        long newsId = 1;
        String title = "Title 1";
        String content = "Content 1";
        String imgUrl = "Img Url1";
        String newsUrl = "News Url1";
        LocalDateTime createdAt = LocalDateTime.of(2023, 8, 12, 0, 0, 0);
        String url = "/news";

        newsRepository.save(News.builder()
                .newsId(newsId)
                .title(title)
                .content(content)
                .imageUrl(imgUrl)
                .newsUrl(newsUrl)
                .createdAt(createdAt)
                .build());
        System.out.println(newsRepository.findAll());
        // when : 설정한 url 주소로 접속 후 json 데이터 리턴받아 저장하기
        ResultActions resultActions = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON));
        // then: 응답 데이터와 fixture 비교하여 검증하기

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value(content))
                .andExpect(jsonPath("$[0].title").value(title));

    }

}
