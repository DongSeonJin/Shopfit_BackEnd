package com.spring.community.service;

import com.spring.community.DTO.PostSaveDTO;
import com.spring.community.DTO.PostUpdateDTO;
import com.spring.community.entity.Post;
import com.spring.community.exception.PostIdNotFoundException;
import com.spring.community.repository.DynamicPostRepository;
import com.spring.community.repository.PostRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.support.SimpleTriggerContext;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
@SpringBootTest
//@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

//    @Mock
//    private PostRepository postRepository;
//    @Mock
//    private DynamicPostRepository dynamicPostRepository;
//
//    @InjectMocks
//    private PostServiceImpl postService;

    @Autowired
    PostServiceImpl postService;

    @Autowired
    private PostRepository postRepository;

    @Test
    @Transactional
    public void getAllPostsTest() {
        // when : 전체 데이터 가져오기
        List<Post> postList = postService.getAllPosts();

        // then : 길이 3
        assertEquals(3, postList.size());
    }

    @Test
    @Transactional
    public void getPostByIdTest() {
        // given
        long postId  = 2;
        String title = "두 번째 포스트";
        String content = "두 번째 포스트 내용입니다.";

        // when
        Post post = postService.getPostById(postId);

        // then
        assertEquals(postId, post.getPostId());
        assertEquals(title, post.getTitle());
        assertEquals(content, post.getContent());
    }


//    @Test
//    @Transactional
//    public void savePostTest() {
//        // given
//        String nickname = "testNickname";
//        String title = "testTitle";
//        String content = "testContent";
//        PostSaveDTO postSaveDTO = new PostSaveDTO();
//        postSaveDTO.setNickname(nickname);
//        postSaveDTO.setTitle(title);
//        postSaveDTO.setContent(content);
//
//        // when
//        when(postRepository.save(any(Post.class))) // 포스트 저장에 대한 동작 정의
//                .thenReturn(new Post(4L, nickname, title, content));
//
//        doNothing().when(dynamicPostRepository).createDynamicTable(postSaveDTO); // createDynamicTable 메서드의 동작 정의
//
//        postService.savePost(postSaveDTO);
//
//        // then
//        verify(postRepository).save(any(Post.class)); // 포스트 저장 호출 확인
//        verify(dynamicPostRepository).createDynamicTable(postSaveDTO); // createDynamicTable 메서드 호출 확인
//
//        assertEquals(4, postService.getAllPosts().size());
//        assertEquals(nickname, postService.getAllPosts().get(3).getNickname());
//        assertEquals(title, postService.getAllPosts().get(3).getTitle());
//        assertEquals(content, postService.getAllPosts().get(3).getContent());
//    }



    @Test
    @Transactional
    public void deletePostByIdTest() {
        // given
        long postId = 2;

        // when
        postService.deletePostById(postId);

        // then
        assertEquals(2, postService.getAllPosts().size());
        assertThrows(PostIdNotFoundException.class, () -> postService.getPostById(postId));
    }

//    @Test
//    @Transactional
//    public void updateTest() {
//        // given
//        long postId = 3;
//        String title = "수정제목";
//        String content = "수정본문";
//
//        Post post = Post.builder()
//                .postId(postId)
//                .title(title)
//                .content(content)
//                .build();
//
//        // when
//        postService.update(post);
//
//        // then
//        assertEquals(postId, postService.getAllPosts(postId).getPostId());

//    }
}

