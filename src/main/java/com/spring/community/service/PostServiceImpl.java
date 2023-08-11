package com.spring.community.service;

import com.spring.community.DTO.PostUpdateDTO;
import com.spring.community.entity.Post;
import com.spring.community.exception.PostIdNotFoundException;
import com.spring.community.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService{

    @Autowired
    PostRepository postRepository;


    @Override
    public Post getPostById(Long id) {

        return postRepository.findById(id).get();
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }


    @Override
    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public void deletePostById(Long id) {
        postRepository.deleteById(id);
    }

    @Override // 파라미터에 DTO만 넣어 구현해보려다가, findById메서드의 타입과 맞지않아 id는 다로받음.
    public void update(Long userId, PostUpdateDTO postUpdateDTO) {
        Post post = postRepository.findById(userId)
                .orElseThrow(() -> new PostIdNotFoundException("해당되는 글을 찾을 수 없습니다 : " + userId));

        post.setTitle(postUpdateDTO.getTitle());
        post.setContent(postUpdateDTO.getContent());
        // 필요한 경우 다른 필드들도 업데이트

        postRepository.save(post);
    }
}
