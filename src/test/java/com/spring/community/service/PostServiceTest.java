package com.spring.community.service;

import com.spring.community.DTO.IndividualPostResponseDTO;
import com.spring.community.DTO.PostListResponseDTO;
import com.spring.community.DTO.PostCreateRequestDTO;
import com.spring.community.DTO.PostUpdateDTO;
import com.spring.community.entity.Post;
import com.spring.community.entity.PostCategory;
import com.spring.community.exception.PostIdNotFoundException;
import com.spring.community.repository.PostJPARepository;
import com.spring.user.entity.User;
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




//    @InjectMocks
//    private PostServiceImpl postService;

    @Autowired
    PostService postService;


    @Autowired
    private PostJPARepository postRepository;

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
        IndividualPostResponseDTO post = postService.getPostById(postId);

        // then
        assertEquals(postId, post.getPostId());
        assertEquals(title, post.getTitle());
        assertEquals(content, post.getContent());
    }


    @Test
    @Transactional
    public void createPostTest(){
        // given
        String content = "test content3";
        String title = "test title3";
        String nickname = "test nickname3";

        PostCategory postCategory = new PostCategory();



        Post post = Post.builder()
                .user(User.builder()
                        .userId(1L)
                        .build())
                .content(content)
                .title(title)
                .nickname(nickname)
                .postCategory(PostCategory.builder().categoryId(1).build())
                .build();
        // when
        postService.createPost(post);
        // then
        assertEquals(536, postService.getAllPosts().size());
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

