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
    public ResponseEntity<IndividualPostResponseDTO> getPostById (@PathVariable Long postId) {
        postService.increaseViewCount(postId); // 조회 할 때마다 조회수 +1
        Post post = postService.getPostById(postId);

        IndividualPostResponseDTO responseDTO = new IndividualPostResponseDTO(post);

        return ResponseEntity.ok(responseDTO);
    }


    @PostMapping("/create")
    public ResponseEntity<String> createPost(@RequestBody PostSaveDTO postSaveDTO) {
        System.out.println(postSaveDTO);
        postService.savePost(postSaveDTO);

        return ResponseEntity.ok("게시글이 저장되었습니다.");
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String > deletePostById(@PathVariable Long postId) {
        postService.deletePostById(postId);
        return ResponseEntity.ok("게시글이 삭제되었습니다.");
    }

    @RequestMapping(value = "/{postId}", method = {RequestMethod.PUT, RequestMethod.PATCH} )
    public ResponseEntity<String > updatePost(@PathVariable Long postId, @RequestBody PostUpdateDTO postUpdateDTO) {
        postUpdateDTO.setPostId(postId);
        postService.update(postUpdateDTO);
        return ResponseEntity.ok("게시글이 수정되었습니다.");
    }

    @GetMapping("/like/{postId}")
    public ResponseEntity<String> pushLike(@RequestBody LikeSaveDTO likeSaveDTO){
        postService.saveLike(likeSaveDTO);
        return ResponseEntity.ok("좋아요 누르기 성공");
    }


}