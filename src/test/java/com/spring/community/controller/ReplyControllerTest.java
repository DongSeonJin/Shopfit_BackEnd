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
    public void getAllRepliesByPostIdTest() throws Exception {
        // given
        String url = "/reply/3/all";

        // when
        ResultActions resultActions = mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    @Transactional
    public void getReplyByReplyIdTest() throws Exception {
        // given
        String url = "/reply/94";

        // when
        ResultActions resultActions = mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("test reply"));
    }

    @Test
    @Transactional
    public void createReplyTest() throws Exception{
        // given
        Reply reply = new Reply();
        reply.setContent("새로운 댓글 내용");

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
    @DisplayName("댓글번호 96번 삭제시 글번호 3의 댓글수 2개")
    public void deleteReplyTest() throws Exception {
        // given
        Long replyId = 96L;
        Long postId = 3L;
        String url = "/reply/96";

        // when
        ResultActions resultActions =  mockMvc.perform(delete(url)
                .accept(MediaType.TEXT_PLAIN));

        // then
        resultActions
                .andExpect(status().isOk());

        List<Reply> replyList = replyService.findAllByPostId(postId);
        int replyCount = replyList.size();
        assertEquals(2, replyCount);
    }

    @Test
    @Transactional
    public void updateReplyTest() throws Exception {
        // given
        long replyId = 94L;
        String content = "댓글 수정";

        String url = "/reply/94";

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
