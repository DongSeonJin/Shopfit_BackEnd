package com.spring.community.repository;

import com.spring.community.DTO.PostSaveDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DynamicPostRepository {


    void createDynamicTable(PostSaveDTO postSaveDTO);
    void insertDynamicTable(PostSaveDTO postSaveDTO);
    void insertPostTable(PostSaveDTO postSaveDTO);
}
