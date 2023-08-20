package com.spring.community.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.community.DTO.PostSaveDTO;
import com.spring.community.DTO.PostUpdateDTO;
import com.spring.community.entity.Post;
import com.spring.community.repository.PostRepository;
import com.spring.news.repository.NewsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.awt.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    PostRepository postRepository;

    @BeforeEach
    public void setMockMvc() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @Transactional
    public void getAllPostsTest() throws Exception {
//        List<Post> postList = postRepository.findAll();
//        assertThat(postList.size()).isEqualTo(3);

        String url = "/post/list";

        ResultActions resultActions = mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void getPostById() throws Exception {
        // given
        String title = "두 번째 포스트";
        long postId = 2;

        String url = "/post/2";

        // when
        final ResultActions resultActions = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.postId").value(postId));

    }

//    @Test
//    @Transactional
//    public void createPostTest() throws Exception {
//        // given
//        PostSaveDTO postSaveDTO = new PostSaveDTO();
//        postSaveDTO.setTitle("test title");
//        postSaveDTO.setContent("test content");
//
//        String postSaveDTOJson = objectMapper.writeValueAsString(postSaveDTO);
//
//        // when
//        ResultActions resultActions = mockMvc.perform(post("/create")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(postSaveDTOJson));
//
//        // then
//        resultActions
//                .andExpect(status().isOk())
//                .andExpect(content().string("게시글이 저장되었습니다."));
//    }

    @Test
    @Transactional
    public void deletePostByIdTest() throws Exception {
        // given
        long postId = 2;
        String url = "/post/2";

        // when
        mockMvc.perform(delete(url).accept(MediaType.TEXT_PLAIN));

        // then
        assertEquals(2, postRepository.findAll().size());
    }

//    @Test
//    @Transactional
//    public void updatePostTest() throws Exception {
//
//        long postId = 2;
//        String updateTitle = "테스트제목";
//        String updateContent = "테스트내용";
//        Post postUpdateDTO = Post.builder()
//                .postId(postId)
//                .title(updateTitle)
//                .content(updateContent)
//                .build();
//
//        String url = "/post/2";
//
//        final String requestBody = objectMapper.writeValueAsString(postUpdateDTO);
//
//        // when
//        mockMvc.perform(put(url)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(requestBody));
//
//        // then
//        final ResultActions resultActions = mockMvc.perform( get(url)
//                .accept(MediaType.APPLICATION_JSON));
//
//        resultActions
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.title").value(updateTitle))
//                .andExpect(jsonPath("$.content").value(updateContent));
//
//    }
}

