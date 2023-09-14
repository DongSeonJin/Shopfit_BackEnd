package com.spring.community.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.community.DTO.PostCreateRequestDTO;
import com.spring.community.DTO.PostListByUserIdDTO;
import com.spring.community.DTO.PostTop4DTO;
import com.spring.community.DTO.PostUpdateDTO;
import com.spring.community.entity.Post;
import com.spring.community.entity.PostCategory;
import com.spring.community.repository.PostJPARepository;
import com.spring.community.service.PostService;
import com.spring.user.entity.User;
import com.spring.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.parameters.P;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.print.attribute.standard.Media;

import static org.assertj.core.api.Assertions.in;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.ArrayList;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostService postService;

    @BeforeEach
    public void setMockMvc() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }


    @Test
    @Transactional
    public void getPostsByCategoryIdTest() throws Exception {
        int categoryId = 2;
        String url = "/post/list/2";

        ResultActions resultActions = mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isOk()); // HTTP 응답 상태코드가 200 OK 여부를 검증합니다.
    }

    @Test
    @Transactional
    public void getPostByIdTest() throws Exception {
        // given
        String title = "test title1";
        Long postId = 1L;

        String url = "/post/1";

        // when
        ResultActions resultActions = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.postId").value(postId));

    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // create table, drop table 과 같은 DDL 코드는 @Transactional 어노테이션을 무시하고 강제로 커밋하기 때문에
    // 테스트코드 실행 시 롤백이 되지 않아서 테스트코드 실행 불가
//    @Test
//    @Transactional
//    public void createPostTest() throws Exception {
//        // given
//        String url = "/post";
//        Post post = new Post();
//        post.setTitle("Test title");
//        post.setContent("Test content");
//        post.setNickname("Test nickname");
//
////        User user = new User();
//
//        // Create a PostCategory entity and set its properties
//        PostCategory postCategory = new PostCategory();
//        postCategory.setCategoryName("오운완");
//
//        post.setPostCategory(postCategory);
//
//        String requestBody = objectMapper.writeValueAsString(post);
//
//        // when
//        ResultActions resultActions = mockMvc.perform(post(url)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(requestBody));
//
//        // then
//        resultActions.andExpect(status().isOk())
//                .andExpect(content().string("게시글이 저장되었습니다."));
//    }

    @Test
    @Transactional
    public void deletePostByIdTest() throws Exception {
        // given
        Long postId = 1L;
        String url = "/post";

        // when
        mockMvc.perform(delete(url).accept(MediaType.TEXT_PLAIN));

        // then
        assertEquals(12, postJPARepository.findAll().size());
    }

    @Test
    @Transactional
    public void updatePostTest() throws Exception {
        // given
        String updateTitle = "test update title";
        String updateContent = "test update content";
        PostUpdateDTO postUpdateDTO = PostUpdateDTO.builder()
                .title(updateTitle)
                .content(updateContent)
                .build();

        String url = "/post/1";

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

    @Test
    @Transactional
    public void getRecentTop4PostsTest() throws Exception{
        // given
        List<PostTop4DTO> expectedTop4Posts = new ArrayList<>();

        String url = "/post/recent-top4";

        // when
        ResultActions resultActions = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Transactional
    public void getPostsByUserIdTest() throws Exception {
        // given
        Long userId = 1L;
        List<PostListByUserIdDTO> postListByUserIdDTOList = new ArrayList<>();

        String url = "/post/myPage/myCommunity/1";

        // when
        ResultActions resultActions = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(9));

    }
}



