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

    @Override
    public void update(PostUpdateDTO postUpdateDTO) {
        Post post = postRepository.findById(postUpdateDTO.getPostId())
                .orElseThrow(() -> new PostIdNotFoundException("해당되는 글을 찾을 수 없습니다 : " + postUpdateDTO.getPostId()));

        // entity에 setter를 넣는것은 불변성을 위반하기 때문에 builder로 구현.
        post.builder()
                .title(postUpdateDTO.getTitle())
                .content(postUpdateDTO.getContent())
                .updatedAt(LocalDateTime.now());


        postRepository.save(post);
    }
}
