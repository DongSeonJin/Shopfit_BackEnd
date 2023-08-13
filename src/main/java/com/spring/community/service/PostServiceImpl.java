package com.spring.community.service;

import com.spring.community.DTO.PostUpdateDTO;
import com.spring.community.entity.Post;
import com.spring.community.exception.PostIdNotFoundException;
import com.spring.community.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostServiceImpl implements PostService{

    @Autowired
    PostRepository postRepository;

    // 생성자 주입으로 변경

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
        // post 저장 전, 시간정보와 조회수 설정
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        post.setViewCount(0L); // post를 처음 저장할 때 조회수를 Long 타입으로 0으로 초기화
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
        post.setUpdatedAt(LocalDateTime.now()); // 게시글 업데이트 된 시간 업데이트
        // 필요한 경우 다른 필드들도 업데이트

        postRepository.save(post);
    }
}
