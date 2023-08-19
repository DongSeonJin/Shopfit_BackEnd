package com.spring.community.repository;

import com.spring.community.DTO.LikeSaveDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DynamicLikeRepository {

    void createDynamicLike(LikeSaveDTO likeSaveDTO);

    void insertDynamicLike(LikeSaveDTO likeSaveDTO);
}
