package com.spring.community.service;

import com.spring.community.DTO.PostUpdateDTO;
import com.spring.community.entity.Post;

import java.util.List;

public interface PostService {

    Post getPostById(Long id);
    List<Post> getAllPosts();
    Post savePost(Post post);
    void deletePostById(Long id);

    void update(Long userId, PostUpdateDTO postUpdateDTO);

}
