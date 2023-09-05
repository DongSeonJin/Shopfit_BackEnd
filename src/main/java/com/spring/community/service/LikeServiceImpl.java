package com.spring.community.service;

import com.spring.community.DTO.LikeRequestDTO;
import com.spring.community.repository.DynamicLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl implements LikeService{

    @Autowired
    DynamicLikeRepository dynamicLikeRepository;

    @Override
    public void saveLike(LikeRequestDTO likeRequestDTO){ // 좋아요 중복, 없는회원 예외처리 추가예정

        dynamicLikeRepository.createDynamicLike(likeRequestDTO);
        dynamicLikeRepository.insertDynamicLike(likeRequestDTO);
    }

    @Override
    public void deleteLike(LikeRequestDTO likeRequestDTO) {
        dynamicLikeRepository.deleteDynamicLike(likeRequestDTO);
    }

    @Override
    public Long getLikeCount(Long postId) {
        return dynamicLikeRepository.getLikeCount(postId);
    }

    @Override
    public int isLiked(LikeRequestDTO likeRequestDTO) {
        int isLikeCliked = dynamicLikeRepository.isLiked(likeRequestDTO); //좋아요가 눌렸다면 1, 아니면 0 반환.

        return isLikeCliked;
    }

}
