package com.spring.community.service;

import com.spring.community.DTO.PostSaveDTO;
import com.spring.community.DTO.PostUpdateDTO;
import com.spring.community.entity.Post;
import com.spring.community.exception.PostIdNotFoundException;
import com.spring.community.repository.DynamicPostRepository;
import com.spring.community.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostServiceImpl implements PostService{


    PostRepository postRepository;
    DynamicPostRepository dynamicPostRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository){
        this.postRepository = postRepository;
    }

    // 생성자 주입으로 변경

    @Override
    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new PostIdNotFoundException("Post with id " + id + " not found."));
    }

    @Override
    public List<Post> getAllPosts() {

        return postRepository.findAll();
    }


    @Override
    public void savePost(PostSaveDTO postSaveDTO) {
        dynamicPostRepository.createDynamicTable(postSaveDTO);// 없는 테이블명(nickname)일 시, 테이블 생성
        dynamicPostRepository.insertDynamicTable(postSaveDTO); // 데이터 삽입
        dynamicPostRepository.insertPostTable(postSaveDTO);// 검색용 통합 테이블에 삽입.
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
                .updatedAt(LocalDateTime.now())
                .build(); // 추후 DTO에 메서드 추가하고, builder붙여서 DTO로 모두 교체하는 리펙토링 시도.


        postRepository.save(post);
    }
}
