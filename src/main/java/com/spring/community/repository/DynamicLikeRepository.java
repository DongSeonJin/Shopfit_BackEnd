package com.spring.community.repository;

import com.spring.community.DTO.LikeRequestDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DynamicLikeRepository {

    void createDynamicLike(LikeRequestDTO likeRequestDTO);

    void insertDynamicLike(LikeRequestDTO likeRequestDTO);

    void deleteDynamicLike(LikeRequestDTO likeRequestDTO);

    Long getLikeCount(Long postId);

    int isLiked(LikeRequestDTO likeRequestDTO);
}
