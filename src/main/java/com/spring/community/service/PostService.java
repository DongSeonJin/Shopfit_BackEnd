package com.spring.community.service;

import com.spring.community.DTO.*;
import com.spring.community.entity.Post;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {

    IndividualPostResponseDTO getPostById(Long id);
    List<Post> getAllPosts();
    Page<Post> getPostsByCategoryId(Long categoryId, int pageNumber);
    void createPost(Post post);
    void deletePostById(Long id);
    void update(PostUpdateDTO postUpdateDTO);
    void increaseViewCount (Long postId);
    List<PostTop4DTO> getRecentTop4Posts();
    List<PostListByUserIdDTO> findPostsByUserId (Long userId);
    long getReplyCount (Long postId);

    Page<Post> searchPostByKeyword(String keyword, int currentPageNum);
}
