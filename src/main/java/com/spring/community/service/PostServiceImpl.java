package com.spring.community.service;

import com.spring.community.DTO.*;
import com.spring.community.entity.Post;
import com.spring.community.exception.PostIdNotFoundException;
import com.spring.community.repository.DynamicLikeRepository;
import com.spring.community.repository.PostJPARepository;
import com.spring.community.repository.ReplyRepository;
import com.spring.exception.CustomException;
import com.spring.exception.ExceptionCode;
import org.openqa.selenium.remote.ErrorCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService{

    private final PostJPARepository postJPARepository;
    private final DynamicLikeRepository dynamicLikeRepository;
    private final ReplyRepository replyRepository;

    final int PAGE_SIZE = 20; // 한 페이지에 몇 개의 게시글을 조회할지

    @Autowired
    public PostServiceImpl(PostJPARepository postRepository,
                           DynamicLikeRepository dynamicLikeRepository, ReplyRepository replyRepository){

        this.dynamicLikeRepository = dynamicLikeRepository;
        this.postJPARepository = postRepository;
        this.replyRepository = replyRepository;
    }

    // get Post By PostId
    @Override
    public IndividualPostResponseDTO getPostById(Long postId) {
        Post post = postJPARepository.findById(postId)
                .orElseThrow(() -> new CustomException(ExceptionCode.POST_NOT_FOUND)); // enum 활용 예시

        return new IndividualPostResponseDTO(post);
    }

    // get All Posts
    @Override
    public List<Post> getAllPosts() {

        return postJPARepository.findAll();
    }

    // get Posts By CategoryId
    @Override
    public Page<Post> getPostsByCategoryId(Long categoryId, int pageNumb){
        Page<Post> postlist = postJPARepository.findByPostCategory_CategoryId(
                categoryId, PageRequest.of(pageNumb - 1, PAGE_SIZE, Sort.Direction.DESC, "postId"));

        if (postlist.getTotalPages() < pageNumb) {  // 만약 페이지 번호가 범위를 벗어나는 경우에는 가장 마지막 페이지로 자동으로 이동
            return postJPARepository.findAll(
                    PageRequest.of(postlist.getTotalPages() - 1, PAGE_SIZE, Sort.Direction.DESC, "postId"));
        } else {
            return postlist;
        }

    }

    // create Post
    @Override
    public void createPost(Post post) {

        Post savedPost = postJPARepository.save(post);
        Long postId = savedPost.getPostId();
        Long userId = savedPost.getUser().getUserId();

        LikeRequestDTO likeSave = LikeRequestDTO.builder()
                .postId(postId)
                .userId(userId)
                .build();

        dynamicLikeRepository.createDynamicLike(likeSave); // 동적 좋아요 테이블 생성
        postJPARepository.save(post); // post 생성

    }

    // delete Post By PostId
    @Override
    public void deletePostById(Long id) {
        if (!postJPARepository.existsById(id)) {
            throw new CustomException(ExceptionCode.POST_NOT_FOUND);
        }
        postJPARepository.deleteById(id);
    }

    // update
    @Override
    public void update(PostUpdateDTO postUpdateDTO) {

        // 수정 전 게시글 찾기
        Optional<Post> optionalPost = postJPARepository.findById(postUpdateDTO.getPostId());

        // 해당 postId 게시글 없으면 예외처리
        if (!optionalPost.isPresent()) {
            throw new CustomException(ExceptionCode.POST_NOT_FOUND);
        }

        // 수정 전 게시글 가져오기
        Post existingPost = optionalPost.get();

        // 게시글 수정
        existingPost.setTitle(postUpdateDTO.getTitle());
        existingPost.setContent(postUpdateDTO.getContent());
        existingPost.setUpdatedAt(LocalDateTime.now());
        existingPost.setImageUrl1(postUpdateDTO.getImageUrl1());
        existingPost.setImageUrl2(postUpdateDTO.getImageUrl2());
        existingPost.setImageUrl3(postUpdateDTO.getImageUrl3());

        // 수정된 게시글 저장
        postJPARepository.save(existingPost);
    }


    // increase ViewCount
    @Override
    public void increaseViewCount (Long postId) {
        postJPARepository.increaseViewCount(postId);
    }

    // get Recent Top4 Posts
    @Override
    public List<PostTop4DTO> getRecentTop4Posts() {
        List<PostTop4DTO> top4DTOs = postJPARepository.findAllByOrderByViewCountDesc(PageRequest.of(0, 4))
                .stream()
                .map(post -> PostTop4DTO.builder()
                        .title(post.getTitle())
                        .imageUrl(post.getImageUrl1())
                        .postId(post.getPostId())
                        .build())
                .collect(Collectors.toList());

        return top4DTOs;
    }

    // find Posts By UserId
    @Override
    public List<PostListByUserIdDTO> findPostsByUserId(Long userId) {
        List<Post> posts = postJPARepository.findByUser_UserId(userId);
        return posts.stream()
                .map(PostListByUserIdDTO::new)
                .collect(Collectors.toList());
    }

    // get Reply Count
    @Override
    public long getReplyCount(Long postId) {
        return replyRepository.countByPost_PostId(postId);
    }


    // 검색 - title, postId 기준 내림차순
    @Override
    public Page<Post> searchPostByKeyword(String keyword, int pageNum) {
        Pageable pageable = PageRequest.of(pageNum - 1, PAGE_SIZE, Sort.Direction.DESC, "postId");
        Page<Post> searchResults = postJPARepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword, pageable);

        if (searchResults.getTotalElements() == 0) {
            return Page.empty(pageable);
        }

        if (searchResults.getTotalPages() < pageNum) {
            pageable = PageRequest.of(searchResults.getTotalPages() - 1, PAGE_SIZE, Sort.Direction.DESC, "postId");
            searchResults = postJPARepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword, pageable);
        }
        return searchResults;
    }
}
