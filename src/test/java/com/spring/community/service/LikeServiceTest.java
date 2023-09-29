package com.spring.community.service;

import com.spring.community.DTO.LikeRequestDTO;
import com.spring.community.DTO.LikeResponseDTO;
import com.spring.community.controller.LikeController;
import com.spring.community.repository.DynamicLikeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class LikeServiceTest {

    @Autowired
    private LikeService likeService;

    @Autowired
    private DynamicLikeRepository dynamicLikeRepository;


    @Test
    @Transactional
    public void saveLikeTest() throws Exception {
        // given
        Long userId = 1L;
        Long postId = 1L;

        LikeRequestDTO likeRequestDTO = LikeRequestDTO.builder()
                .userId(userId)
                .postId(postId)
                .build();

        // when
        likeService.saveLike(likeRequestDTO);

        // then
        Long likeCount = dynamicLikeRepository.getLikeCount(postId);
        assertEquals(2L, likeCount);

    }

    @Test
    @Transactional
    public void deleteLikeTest() throws Exception {
        // given
        Long userId = 1L;
        Long postId = 1L;

        LikeRequestDTO likeRequestDTO = LikeRequestDTO.builder()
                .userId(userId)
                .postId(postId)
                .build();

        // when
        likeService.deleteLike(likeRequestDTO);

        // then
        Long likeCount = dynamicLikeRepository.getLikeCount(1L);
        assertEquals(0L, likeCount);

        int likeStatus = dynamicLikeRepository.isLiked(likeRequestDTO);
        assertEquals(0, likeStatus);

    }

    @Test
    @Transactional
    public void getLikeCountTest() throws Exception {
        // given
        Long postId = 1L;

        // when
        Long likeCount = likeService.getLikeCount(postId);

        // then
        assertEquals(1, likeCount);
    }

    @Test
    @Transactional
    public void isLikedTest() throws Exception {
        // given
        Long userId = 1L;
        Long postId = 1L;

        LikeRequestDTO likeRequestDTO = LikeRequestDTO.builder()
                .userId(userId)
                .postId(postId)
                .build();

        // when
        int isLikeClicked = likeService.isLiked(likeRequestDTO);

        // then
        assertEquals(1, isLikeClicked);

    }

}