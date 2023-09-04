package com.spring.community.repository;

import com.spring.community.DTO.LikeSaveDTO;
import com.spring.community.DTO.PostSaveDTO;
import com.spring.community.entity.Post;

import com.spring.user.entity.User;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DynamicPostRepository {


    void createDynamicTable(PostSaveDTO postSaveDTO);
    void insertDynamicTable(PostSaveDTO postSaveDTO);
    void insertPostTable(PostSaveDTO postSaveDTO);

    void createDynamicLike(PostSaveDTO postSaveDTO);
}
