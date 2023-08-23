package com.spring.community.controller;

import com.spring.community.DTO.*;
import com.spring.community.entity.Post;
import com.spring.community.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
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


    @GetMapping("/list")
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();

        return ResponseEntity.ok(posts);
    }

    @GetMapping("/list/{categoryId}")
    public ResponseEntity<List<PostListResponseDTO>> getPostsByCategoryId(@PathVariable Integer categoryId){
        List<PostListResponseDTO> posts = postService.getPostsByCategoryId(categoryId);
        return ResponseEntity
                .ok()
                .body(posts);
    }



    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPostById(@PathVariable Long postId) {
        postService.increaseViewCount(postId); // post 조회수 증가
        Post post = postService.getPostById(postId);
        return ResponseEntity.ok(post);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createPost(@RequestBody PostSaveDTO postSaveDTO) {
        System.out.println(postSaveDTO);
        postService.savePost(postSaveDTO);

        return ResponseEntity.ok("게시글이 저장되었습니다.");
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePostById(@PathVariable Long postId) {
        postService.deletePostById(postId);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/{postId}", method = {RequestMethod.PUT, RequestMethod.PATCH} )
    public ResponseEntity<Void> updatePost(@PathVariable Long postId, @RequestBody PostUpdateDTO postUpdateDTO) {
        postUpdateDTO.setPostId(postId);
        postService.update(postUpdateDTO);
        return ResponseEntity.noContent() // 204 No Content -> 리소스 업데이트 시 자주 사용됨.
                .build();

    }

    @GetMapping("/like/{postId}")
    public ResponseEntity<String> pushlike(@RequestBody LikeSaveDTO likeSaveDTO){
        postService.saveLike(likeSaveDTO);
        return ResponseEntity.ok("좋아요 누르기 성공");
    }

    @GetMapping("/recent-top4")
    public List<PostTop4DTO> getRecentTop4Posts() {
        return postService.getRecentTop4Posts();
    }

}