package com.spring.community.service;

import com.spring.community.DTO.LikeRequestDTO;
import com.spring.community.repository.DynamicLikeRepository;
import com.spring.exception.CustomException;
import com.spring.exception.ExceptionCode;
import com.spring.user.repository.UserRepository;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl implements LikeService{

    private final DynamicLikeRepository dynamicLikeRepository;
    private final UserRepository userRepository;

    @Autowired
    public LikeServiceImpl (DynamicLikeRepository dynamicLikeRepository, UserRepository userRepository) {
        this.dynamicLikeRepository = dynamicLikeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void saveLike(LikeRequestDTO likeRequestDTO){ // 좋아요 중복 예외처리 추가예정
        if (!userRepository.existsById(likeRequestDTO.getUserId())) {
            throw new CustomException(ExceptionCode.USER_NOT_FOUND);
        }
        if (isLiked(likeRequestDTO) == 1) {
            throw new CustomException(ExceptionCode.ALREADY_LIKED);
        }

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
