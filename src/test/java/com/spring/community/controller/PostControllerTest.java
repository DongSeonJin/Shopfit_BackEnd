package com.spring.community.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.community.DTO.PostCreateRequestDTO;
import com.spring.community.DTO.PostUpdateDTO;
import com.spring.community.entity.Post;
import com.spring.community.repository.PostJPARepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

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
    PostJPARepository postJPARepository;

    @BeforeEach
    public void setMockMvc() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @Transactional
    public void getAllPostsTest() throws Exception {
        List<Post> postList = postJPARepository.findAll();
        assertThat(postList.size()).isEqualTo(4);

        String url = "/post/list";

        ResultActions resultActions = mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void getPostsByCategoryId() throws Exception {
        int categoryId = 2;
        String url = "/post/list/2";

        ResultActions resultActions = mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[3].postCategory.categoryId").value(categoryId));


    }

    @Test
    @Transactional
    public void getPostById() throws Exception {
        // given
        String title = "제목 2";
        long postId = 2;

        String url = "/post/2";

        // when
        ResultActions resultActions = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.postId").value(postId));

    }


    @Test
    @Transactional
    public void createPostTest() throws Exception {
        // given
        String url = "/post/create";
        Long userId = 5L;
        int categoryId = 1;
        String nickname = "닉네임5";
        String title = "제목테스트";
        String content = "컨텐츠테스트";

        PostCreateRequestDTO postCreateRequestDTO = PostCreateRequestDTO.builder()
                .userId(userId)
                .categoryId(categoryId)
                .nickname(nickname)
                .title(title)
                .content(content)
                .build();

        // 데이터 역직렬화(JSON)
        String postSaveDTOJson = objectMapper.writeValueAsString(postCreateRequestDTO);

        // when
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(postSaveDTOJson));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string("게시글이 저장되었습니다."));
    }


    @Test
    @Transactional
    public void deletePostByIdTest() throws Exception {
        // given
        long postId = 2;
        String url = "/post/2";

        // when
        mockMvc.perform(delete(url).accept(MediaType.TEXT_PLAIN));

        // then
        assertEquals(3, postJPARepository.findAll().size());
    }

    @Test
    @Transactional
    public void updatePostTest() throws Exception {

        String updateTitle = "테스트제목";
        String updateContent = "테스트내용";
        PostUpdateDTO postUpdateDTO = PostUpdateDTO.builder()
                .title(updateTitle)
                .content(updateContent)
                .build();

        String url = "/post/20";
        System.out.println("테스트코드" + postUpdateDTO);

        final String requestBody = objectMapper.writeValueAsString(postUpdateDTO);

        // when
        mockMvc.perform(patch(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        final ResultActions resultActions = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON));


        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("title").value(updateTitle))
                .andExpect(jsonPath("content").value(updateContent));

    }
}

