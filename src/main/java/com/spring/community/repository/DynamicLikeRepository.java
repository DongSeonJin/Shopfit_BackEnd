package com.spring.community.repository;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DynamicLikeRepository {

    void createDynamicLike(Long userId, Long postId);
}
