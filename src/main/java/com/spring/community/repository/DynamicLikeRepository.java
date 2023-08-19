package com.spring.community.repository;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DynamicLikeRepository {

    void createDynamicLike(String nickname, Long userId, Long postId);

    void insertDynamicLike(String nickName, Long userId, Long postId);
}
