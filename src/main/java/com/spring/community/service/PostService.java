package com.spring.community.service;

import com.spring.community.DTO.*;
import com.spring.community.entity.Post;

import java.util.List;

public interface PostService {

    IndividualPostResponseDTO getPostById(Long id);
    List<Post> getAllPosts();
    List<PostListResponseDTO> getPostsByCategoryId(Integer categoryId);
    void savePost(PostSaveDTO postSaveDTO);
    void deletePostById(Long id);
    void update(PostUpdateDTO postUpdateDTO);
    void saveLike(LikeSaveDTO likeSaveDTO);
    void increaseViewCount (Long postId);

}
