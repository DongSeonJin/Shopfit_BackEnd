package com.spring.community.repository;

import com.spring.community.DTO.PostSaveDTO;
import com.spring.community.DTO.PostUpdateDTO;
import com.spring.community.entity.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface DynamicPostRepository {


    void createDynamicTable(PostSaveDTO postSaveDTO);
    void insertDynamicTable(PostSaveDTO postSaveDTO);
    void insertPostTable(PostSaveDTO postSaveDTO);
}
