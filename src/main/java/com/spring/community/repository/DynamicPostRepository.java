package com.spring.community.repository;

import com.spring.community.DTO.PostCreateRequestDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DynamicPostRepository {


    void createDynamicTable(PostCreateRequestDTO postCreateRequestDTO);
    void insertDynamicTable(PostCreateRequestDTO postCreateRequestDTO);
    void insertPostTable(PostCreateRequestDTO postCreateRequestDTO);

    void createDynamicLike(PostCreateRequestDTO postCreateRequestDTO);


}
