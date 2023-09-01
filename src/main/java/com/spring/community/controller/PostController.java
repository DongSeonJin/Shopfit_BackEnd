package com.spring.community.controller;

import com.spring.community.DTO.*;
import com.spring.community.entity.Post;
import com.spring.community.repository.PostJPARepository;
import com.spring.community.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping({"/list/{categoryId}/{pageNumb}", "/list/{categoryId}"})
    public ResponseEntity<Page<PostListResponseDTO>> getPostsByCategoryId(
                                                    @PathVariable Long categoryId,
                                                    @PathVariable(required = false) Integer pageNumb){
        int currentPageNum = pageNumb != null ? pageNumb : 1;
        Page<PostListResponseDTO> posts = postService.getPostsByCategoryId(categoryId, currentPageNum)
                .map(PostListResponseDTO:: new);

        return ResponseEntity
                .ok()
                .body(posts);
    }


    @GetMapping("/{postId}")
    public ResponseEntity<IndividualPostResponseDTO> getPostById (@PathVariable Long postId) {
        postService.increaseViewCount(postId); // 조회 할 때마다 조회수 +1
        IndividualPostResponseDTO responseDTO = postService.getPostById(postId);

        return ResponseEntity.ok(responseDTO);
    }


    @PostMapping
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

    @PostMapping("/like")
    public ResponseEntity<String> pushLike(@RequestBody LikeSaveDTO likeSaveDTO){
        postService.saveLike(likeSaveDTO); //받아야 할 정보 : 글주인 nickname과 postId, 좋아요 누른사람 userId
        return ResponseEntity.ok("좋아요 누르기 성공");
    }

    @GetMapping("/recent-top4")
    public List<PostTop4DTO> getRecentTop4Posts() {
        return postService.getRecentTop4Posts();
    }

}