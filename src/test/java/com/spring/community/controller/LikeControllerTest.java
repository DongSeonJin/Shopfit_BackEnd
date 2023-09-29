package com.spring.community.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.community.DTO.LikeRequestDTO;
import com.spring.community.service.LikeService;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LikeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LikeService likeService;

    @Test
    @Transactional
    public void pushLikeTest() throws Exception {
        // given
        Long userId = 1L;
        Long postId = 1L;

        String url = "/post/like/add";

        LikeRequestDTO likeRequestDTO = LikeRequestDTO.builder()
                .userId(userId)
                .postId(postId)
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(likeRequestDTO)));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string("좋아요 누르기 성공"));

    }

    @Test
    @Transactional
    public void cancelLikeTest() throws Exception {
        // given
        Long userId = 1L;
        Long postId = 1L;

        String url = "/post/like/delete";

        LikeRequestDTO likeRequestDTO = LikeRequestDTO.builder()
                .userId(userId)
                .postId(postId)
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(likeRequestDTO)));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string("좋아요 취소 성공"));

    }

    @Test
    @Transactional
    public void findLikeTest() throws Exception{
        // given
        Long userId = 1L;
        Long postId = 1L;

        String url = "/post/like";

        LikeRequestDTO likeRequestDTO = LikeRequestDTO.builder()
                .userId(userId)
                .postId(postId)
                .build();

        Long expectedLikeCount = 1L;
        int expectedIsLiked = 1;

        // when
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(likeRequestDTO)));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isLiked").value(expectedIsLiked))
                .andExpect(jsonPath("$.likeCnt").value(expectedLikeCount));

    }

}