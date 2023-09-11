package com.spring.community.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.community.DTO.ReplyCreateRequestDTO;
import com.spring.community.entity.Post;
import com.spring.community.entity.Reply;
import com.spring.community.repository.ReplyRepository;
import com.spring.community.service.ReplyService;
import com.spring.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.parameters.P;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class ReplyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReplyService replyService;


    @Test
    @Transactional
    @DisplayName("해당 postId 에 달린 댓글 전부 조회")
    public void getAllRepliesByPostIdTest() throws Exception {
        // given
        String url = "/reply/1/all";

        // when
        ResultActions resultActions = mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @Transactional
    @DisplayName("replyId에 해당하는 댓글 조회")
    public void getReplyByReplyIdTest() throws Exception {
        // given
        String url = "/reply/1";

        // when
        ResultActions resultActions = mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("reply content1"));
    }

    @Test
    @Transactional
    @DisplayName("새로운 댓글 작성")
    public void createReplyTest() throws Exception{
        // given
        Reply reply = new Reply();
        reply.setContent("create reply test");

        String url = "/reply";

        // 데이터 직렬화
        final String requestBody = objectMapper.writeValueAsString(reply);

        // when
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(content().string("댓글 등록 성공"));
    }


    @Test
    @Transactional
    @DisplayName("replyId 에 해당하는 댓글 삭제")
    public void deleteReplyTest() throws Exception {
        // given
        Long replyId = 2L;
        Long postId = 1L;
        String url = "/reply/2";

        // when
        ResultActions resultActions =  mockMvc.perform(delete(url)
                .accept(MediaType.TEXT_PLAIN));

        // then
        resultActions
                .andExpect(status().isOk());

        List<Reply> replyList = replyService.findAllByPostId(postId);
        int replyCount = replyList.size();
        assertEquals(1, replyCount);
    }

    @Test
    @Transactional
    @DisplayName("replyId에 해당하는 댓글 수정")
    public void updateReplyTest() throws Exception {
        // given
        long replyId = 1L;
        String content = "update reply test";

        String url = "/reply/1";

        Reply updatedReply = new Reply();
        updatedReply.setContent(content);

        // 데이터 직렬화
        final String requestBody = objectMapper.writeValueAsString(updatedReply);

        // when
        ResultActions resultActions = mockMvc.perform(patch(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string("댓글이 수정되었습니다."));

        // 댓글 수정 후 조회하여 수정되었는지 확인
        mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(content));



    }
}
