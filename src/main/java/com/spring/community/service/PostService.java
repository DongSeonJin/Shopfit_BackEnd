package com.spring.community.service;

import com.spring.community.DTO.PostSaveDTO;
import com.spring.community.DTO.PostUpdateDTO;
import com.spring.community.entity.Post;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;
import java.util.List;

public interface PostService {

    Post getPostById(Long id);
    List<Post> getAllPosts();
    void savePost(PostSaveDTO postSaveDTO);
    void deletePostById(Long id);
    void update(PostUpdateDTO postUpdateDTO);

    void saveLike(String nickname, Long userId, Long postId);

}
