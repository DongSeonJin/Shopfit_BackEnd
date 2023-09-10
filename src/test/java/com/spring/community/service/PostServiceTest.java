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
import jakarta.transaction.Transactional;
import net.bytebuddy.matcher.EqualityMatcher;
import org.hibernate.dialect.function.LpadRpadPadEmulation;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.notIn;
import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
//@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private PostJPARepository postJPARepository;
    @Mock
    private DynamicLikeRepository dynamicLikeRepository;
    @Mock
    private ReplyRepository replyRepository;
    @Mock
    private UserRepository userRepository;

//    @Mock
//    User user;

    @Autowired
    PostService postService;

    @Test
    @Transactional
    public void getPostByIdTest() {
        // given
        long postId = 1L;
        String title = "홀쑤기";
        String content = "홀쑤기";

        // when
        IndividualPostResponseDTO post = postService.getPostById(postId);

        // then
        assertEquals(postId, post.getPostId());
        assertEquals(title, post.getTitle());
        assertEquals(content, post.getContent());
    }

    @Test
    @Transactional
    public void getAllPostsTest() {
        // when
        List<Post> postList = postService.getAllPosts();

        // then (테스트코드 작성 기준 전체 글 개수 534 개)
        assertEquals(534, postList.size());
    }

    @Test
    @Transactional
    public void getPostsByCategoryIdTest() {
        // given
        Long categoryId = 1L;
        int pageNumber = 1;

        // when
        Page<Post> posts = postService.getPostsByCategoryId(categoryId, pageNumber);

        // then
        assertEquals(20, posts.getSize());
    }

//    @Test
//    @Transactional
//    public void createPostTest(){
//        // given
//        String content = "test content";
//        String title = "test title";
//        String nickname = "test nickname";
//
//        User user = User.createUser();
//        user.setUserId(1L);
//
//        PostCategory postCategory = new PostCategory();
//        postCategory.setCategoryId(1);
//        postCategory.setCategoryName("오운완");
//
//        Post post = Post.builder()
//                .content(content)
//                .title(title)
//                .nickname(nickname)
//                .postCategory(postCategory)
//                .build();
//
//        // when
//        postService.createPost(post);
//
//        // then
//        assertEquals(534, postService.getAllPosts().size());
//
//
//
//    }


//    }

    @Test
    @Transactional
    public void deletePostByIdTest() {
        // given
        Long postId = 533L;

        // when
        postService.deletePostById(postId);

        // then
        assertEquals(533, postService.getAllPosts().size());
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
        assertEquals(263, postService.getPostById(postId).getViewCount());
    }

    // getRecentTop4Posts 테스트코드 추가


    @Test
    @Transactional
    public void findPostsByUserIdTest() {
        // given
        Long userId = 1L;

        // when
        List<PostListByUserIdDTO> result = postService.findPostsByUserId(userId);

        // then (테스트코드 작성일 기준 1번 유저 글 4개)
        assertEquals(4, result.size());
    }

    @Test
    @Transactional
    public void getReplyCountTest() {
        // given
        Long postId = 3L;

        // when
        postService.getReplyCount(postId);

        // then (테스트코드 작성일 기준 3번 글 댓글 3개)
        assertEquals(3, postService.getReplyCount(postId));

    }




}

