package com.spring.community.service;

import com.spring.community.DTO.PostUpdateDTO;
import com.spring.community.entity.Post;
import com.spring.community.exception.PostIdNotFoundException;
import com.spring.community.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Post savePost(Post post) { // 아래 주석 전부 JPA로 교체
        // post 저장 전, 시간정보와 조회수 설정
//        post.setCreatedAt(LocalDateTime.now());
//        post.setUpdatedAt(LocalDateTime.now());
//        post.setViewCount(0L); // post를 처음 저장할 때 조회수를 Long 타입으로 0으로 초기화

        return postRepository.save(post);
    }

    @Override
    public void deletePostById(Long id) {
        postRepository.deleteById(id);
    }

    @Override // 파라미터에 DTO만 넣어 구현해보려다가, findById메서드의 타입과 맞지않아 id는 다로받음.
    public void update(PostUpdateDTO postUpdateDTO) {
        Post post = postRepository.findById(postUpdateDTO.getPostId())
                .orElseThrow(() -> new PostIdNotFoundException("해당되는 글을 찾을 수 없습니다 : " + postUpdateDTO.getPostId()));

        post.update(postUpdateDTO); // post 엔터티에 있는 update메서드
                                    // 업데이트 정보 받아온 후 post 에 할당

        postRepository.save(post);
    }
}
