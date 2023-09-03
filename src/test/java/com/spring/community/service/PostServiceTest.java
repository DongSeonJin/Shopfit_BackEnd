package com.spring.community.service;

import com.spring.community.DTO.PostListResponseDTO;
import com.spring.community.DTO.PostCreateRequestDTO;
import com.spring.community.DTO.PostUpdateDTO;
import com.spring.community.entity.Post;
import com.spring.community.exception.PostIdNotFoundException;
import com.spring.community.repository.DynamicPostRepository;
import com.spring.community.repository.PostJPARepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
//@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private PostJPARepository postRepository;
    @Mock
    private DynamicPostRepository dynamicPostRepository;

//    @InjectMocks
//    private PostServiceImpl postService;

    @Autowired
    PostService postService;

//
//    @Autowired
//    private PostJPARepository postRepository;

    @Test
    @Transactional
    public void getAllPostsTest() {
        // when : 전체 데이터 가져오기
        List<Post> postList = postService.getAllPosts();

        // then : 길이 3
        assertEquals(10, postList.size());
    }

    @Test
    @Transactional
    public void getPostsByCategoryId() {
        int id = 2;
        List<PostListResponseDTO> postList = postService.getPostsByCategoryId(id);
        System.out.println(postList);
        assertEquals(5, postList.size());
    }


    @Test
    @Transactional
    public void getPostByIdTest() {
        // given
        long postId  = 2;
        String title = "제목 2";
        String content = "내용 2";

        // when
        Post post = postService.getPostById(postId);

        // then
        assertEquals(postId, post.getPostId());
        assertEquals(title, post.getTitle());
        assertEquals(content, post.getContent());
    }


    @Test
    @Transactional
    public void savePostTest() {
        // given
        String nickname = "testNickname";
        String title = "testTitle";
        String content = "testContent";
        PostCreateRequestDTO postCreateRequestDTO = PostCreateRequestDTO.builder()
                .nickname(nickname)
                .title(title)
                .content(content)
                .build();


        postService.savePost(postCreateRequestDTO);


        assertEquals(5, postService.getAllPosts().size());
        assertEquals(nickname, postService.getAllPosts().get(4).getNickname());
        assertEquals(title, postService.getAllPosts().get(4).getTitle());
        assertEquals(content, postService.getAllPosts().get(4).getContent());
    }



    @Test
    @Transactional
    public void deletePostByIdTest() {
        // given
        long postId = 2;

        // when
        postService.deletePostById(postId);

        // then
        assertEquals(3, postService.getAllPosts().size());
        assertThrows(PostIdNotFoundException.class, () -> postService.getPostById(postId));
    }

    @Test
    @Transactional
    public void updateTest() {
        // given
        Long postId = 20L;
        String title = "수정제목";
        String content = "수정본문";

        PostUpdateDTO postUpdateDTO = PostUpdateDTO.builder()
                .postId(postId)
                .title(title)
                .content(content)
                .build();

        // when
        postService.update(postUpdateDTO);

        // then
        assertEquals(title, postService.getPostById(postUpdateDTO.getPostId()).getTitle());

    }

    @Test
    @Transactional
    public void increaseViewCountTest() {
        // given
        long postId = 105;

        // when
        postService.increaseViewCount(postId);

        // then
        Optional<Post> viewedPost = postRepository.findById(postId);
        assertThat(viewedPost.get().getViewCount()).isEqualTo(1);


    }
}

