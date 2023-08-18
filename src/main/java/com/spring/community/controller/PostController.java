package com.spring.community.controller;

import com.spring.community.DTO.PostSaveDTO;
import com.spring.community.DTO.PostUpdateDTO;
import com.spring.community.entity.Post;
import com.spring.community.service.PostService;
import com.spring.community.service.PostServiceImpl;
import org.apache.coyote.Response;
import org.hibernate.grammars.hql.HqlParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {


    // 각 메서드에 예외처리 추가 예정

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }


    @GetMapping({"/list", "/list/{pageNumber}"})
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        Post post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createPost(@RequestBody PostSaveDTO postSaveDTO) {
        System.out.println(postSaveDTO);
        postService.savePost(postSaveDTO);

        return ResponseEntity.ok("게시글이 저장되었습니다.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePostById(@PathVariable Long id) {
        postService.deletePostById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePost(@PathVariable Long id, @RequestBody PostUpdateDTO postUpdateDTO) {
        postService.update(postUpdateDTO);
        return ResponseEntity.noContent() // 204 No Content -> 리소스 업데이트 시 자주 사용됨.
                .build();

    }




}