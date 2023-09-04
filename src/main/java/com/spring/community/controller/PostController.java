package com.spring.community.controller;

import com.spring.community.DTO.*;
import com.spring.community.entity.Post;
import com.spring.community.service.LikeService;
import com.spring.community.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {


    // 각 메서드에 예외처리 추가 예정

    private final PostService postService;

    private final LikeService likeService;

    @Autowired
    public PostController(PostService postService, LikeService likeService) {
        this.postService = postService;
        this.likeService = likeService;
    }


    @GetMapping("/list")
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();

        return ResponseEntity.ok(posts);
    }

    @GetMapping({"/list/{categoryId}/{pageNumb}", "/list/{categoryId}"})
    public ResponseEntity<Page<PostListResponseDTO>> getPostsByCategoryId(
                                                    @PathVariable Long categoryId,
                                                    @PathVariable(required = false) Integer pageNumb){
        int currentPageNum = pageNumb != null ? pageNumb : 1;
        Page<PostListResponseDTO> posts = postService.getPostsByCategoryId(categoryId, currentPageNum)
                .map(PostListResponseDTO:: new);

        // 각 게시글의 좋아요 수를 조회하여 DTO에 설정
        posts.getContent().forEach(post -> {
            long likeCount = likeService.getLikeCount(post.getPostId());
            post.setLikeCnt(likeCount);
        });

        return ResponseEntity
                .ok()
                .body(posts);
    }


    @GetMapping("/{postId}")
    public ResponseEntity<IndividualPostResponseDTO> getPostById (@PathVariable Long postId) {
        postService.increaseViewCount(postId); // 조회 할 때마다 조회수 +1
        IndividualPostResponseDTO responseDTO = postService.getPostById(postId);

        //좋아요 갯수 responseDTD에 set
        long likeCount = likeService.getLikeCount(postId);
        responseDTO.setLikeCnt(likeCount);


        return ResponseEntity.ok(responseDTO);
    }


    @PostMapping
    public ResponseEntity<String> createPost(@RequestBody PostCreateRequestDTO postCreateRequestDTO) {
        System.out.println(postCreateRequestDTO);
        postService.savePost(postCreateRequestDTO);
        return ResponseEntity.ok("게시글이 저장되었습니다.");
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String > deletePostById(@PathVariable Long postId) {
        postService.deletePostById(postId);
        return ResponseEntity.ok("게시글이 삭제되었습니다.");
    }

    @RequestMapping(value = "/{postId}", method = {RequestMethod.PUT, RequestMethod.PATCH} )
    public ResponseEntity<String> updatePost(@PathVariable Long postId,
                                             @RequestBody PostUpdateDTO postUpdateDTO) {
        System.out.println("서버 요청 도착");
        try {
            postUpdateDTO.setPostId(postId);
            postService.update(postUpdateDTO);
            System.out.println("게시글 업데이트 성공");
            return ResponseEntity.ok("게시글이 수정되었습니다.");
        } catch (Exception e) {
            System.out.println("게시글 업데이트 실패 : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시글 수정 실패");
        }


    }






    @GetMapping("/recent-top4")
    public List<PostTop4DTO> getRecentTop4Posts() {
        return postService.getRecentTop4Posts();
    }

}