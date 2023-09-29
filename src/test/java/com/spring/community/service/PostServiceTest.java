package com.spring.community.service;


import com.spring.community.DTO.*;
import com.spring.community.entity.Post;
import com.spring.community.entity.PostCategory;
import com.spring.community.exception.PostIdNotFoundException;
import com.spring.community.repository.DynamicLikeRepository;
import com.spring.community.repository.PostJPARepository;
import com.spring.community.repository.ReplyRepository;
import com.spring.user.entity.User;
import com.spring.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
//@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @Test
    @Transactional
    public void getAllPostsTest() throws Exception {
        // when
        List<Post> postList = postService.getAllPosts();

        // then
        assertEquals(12, postList.size());
    }

    @Test
    @Transactional
    @DisplayName("1번 글 조회시 제목은 '제목1', 내용은 '내용1'")
    public void getPostByIdTest() throws Exception{
        // given
        long postId = 1L;
        String title = "test title1";
        String content = "test content1";

        // when
        IndividualPostResponseDTO post = postService.getPostById(postId);

        // then
        assertEquals(postId, post.getPostId());
        assertEquals(title, post.getTitle());
        assertEquals(content, post.getContent());
    }

    @Test
    @Transactional
    @DisplayName("카테고리 1번의 1페이지의 글 개수는 20개")
    public void getPostsByCategoryIdTest() {
        // given
        Long categoryId = 1L;
        int pageNumber = 1;

        // when
        Page<Post> posts = postService.getPostsByCategoryId(categoryId, pageNumber);


        // then
        assertEquals(20, posts.getSize());
    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // create table, drop table 과 같은 DDL 코드는 @Transactional 어노테이션을 무시하고 강제로 커밋하기 때문에
    // 테스트코드 실행 시 롤백이 되지 않아서 테스트코드 실행 불가
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//    @Test
//    @Transactional
//    public void createPostTest(){
//        // given
//        String content = "test content";
//        String title = "test title";
//        String nickname = "test nickname";
//
//        Post post = Post.builder()
//                .user(User.builder()
//                        .userId(1L)
//                        .build())
//                .content(content)
//                .title(title)
//                .nickname(nickname)
//                .postCategory(PostCategory.builder()
//                        .categoryId(1)
//                        .categoryName("오운완")
//                        .build())
//                .build();
//        // when
//        postService.createPost(post);
//        postJPARepository.save(post);
//
//        // then
//        assertEquals(16, postService.getAllPosts().size());
//    }

    @Test
    @Transactional
    @DisplayName("1번 글 삭제시, 전체 글 개수 2개")
    public void deletePostByIdTest() {
        // given
        Long postId = 1L;

        // when
        postService.deletePostById(postId);

        // then
        assertEquals(14, postService.getAllPosts().size());
    }

    @Test
    @Transactional
    public void updateTest() {
        // given
        Long postId = 2L;
        String title = "수정 제목";
        String content = "수정 본문";

        PostUpdateDTO postUpdateDTO = PostUpdateDTO.builder()
                .postId(postId)
                .title(title)
                .content(content)
                .build();

        // when
        postService.update(postUpdateDTO);

        // then
        assertEquals(title, postService.getPostById(postUpdateDTO.getPostId()).getTitle());
        assertEquals(content, postService.getPostById(postUpdateDTO.getPostId()).getContent());
    }
    @Test
    @Transactional
    public void increaseViewCountTest() {
        // given
        long postId = 1L;

        // when
        postService.increaseViewCount(postId);

        // then (테스트코드 작성일 기준 1번 글 조회수 262)
        assertEquals(14, postService.getPostById(postId).getViewCount());
    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // getRecentTop4Posts 테스트코드 추가
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    @Test
    @Transactional
    public void findPostsByUserIdTest() {
        // given
        Long userId = 1L;

        // when
        List<PostListByUserIdDTO> result = postService.findPostsByUserId(userId);

        // then (테스트코드 작성일 기준 1번 유저 글 9개)
        assertEquals(9, result.size());
    }

    @Test
    @Transactional
    public void getReplyCountTest() {
        // given
        Long postId = 1L;

        // when
        postService.getReplyCount(postId);

        // then (테스트코드 작성일 기준 3번 글 댓글 2개)
        assertEquals(2, postService.getReplyCount(postId));

    }




}

