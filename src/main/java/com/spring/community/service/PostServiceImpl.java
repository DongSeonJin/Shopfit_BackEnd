package com.spring.community.service;

import com.spring.community.DTO.*;
import com.spring.community.entity.Post;
import com.spring.community.exception.PostIdNotFoundException;
import com.spring.community.repository.DynamicLikeRepository;
import com.spring.community.repository.DynamicPostRepository;
import com.spring.community.repository.PostJPARepository;
import com.spring.shopping.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService{


    @Autowired
    PostJPARepository postJPARepository;
    @Autowired
    DynamicPostRepository dynamicPostRepository;

    @Autowired
    DynamicLikeRepository dynamicLikeRepository;

    final int PAGE_SIZE = 20; // 한 페이지에 몇 개의 게시글을 조회할지

    @Autowired
    public PostServiceImpl(PostJPARepository postRepository){
        this.postJPARepository = postRepository;
    }

    // 생성자 주입으로 변경

    @Override
    public IndividualPostResponseDTO getPostById(Long postId) {
        Post post = postJPARepository.findById(postId)
                .orElseThrow(() -> new PostIdNotFoundException("해당 게시글을 찾을 수 없습니다."));

        return new IndividualPostResponseDTO(post);
    }

    @Override
    public List<Post> getAllPosts() {

        return postJPARepository.findAll();
    }

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

    @Override
    public void savePost(PostSaveDTO postSaveDTO) {
        dynamicPostRepository.createDynamicTable(postSaveDTO);// 없는 테이블명(nickname)일 시, 테이블 생성
        dynamicPostRepository.insertDynamicTable(postSaveDTO); // 데이터 삽입
        dynamicPostRepository.insertPostTable(postSaveDTO);// 검색용 통합 테이블에 삽입.
        dynamicPostRepository.createDynamicLike(postSaveDTO); // 동적 좋아요 테이블 생성
    }

    @Override
    public void deletePostById(Long id) {
        postJPARepository.deleteById(id);
    }

    @Override
    public void update(PostUpdateDTO postUpdateDTO) {

        //        Post post = postJPARepository.findById(postUpdateDTO.getPostId()).get();
//
//
//        // entity에 setter를 넣는것은 불변성을 위반하기 때문에 builder로 구현.
//        Post modifiedPost = Post.builder()
//                .postId(postUpdateDTO.getPostId())
//                .nickname(postUpdateDTO.getNickname())
//                .title(postUpdateDTO.getTitle())
//                .content(postUpdateDTO.getContent())
//                .updatedAt(LocalDateTime.now())
//                .build(); // 추후 DTO에 메서드 추가하고, builder붙여서 DTO로 모두 교체하는 리펙토링 시도.
//
//        postJPARepository.save(modifiedPost); // JPA의 save메서드는 DB에 존재하는 id일경우 update, 없을경우 insert

        // 수정 전 게시글 찾기
        Optional<Post> optionalPost = postJPARepository.findById(postUpdateDTO.getPostId());

        // 해당 postId 게시글 없으면 예외처리
        if (!optionalPost.isPresent()) {
            throw  new PostIdNotFoundException(postUpdateDTO.getPostId() + "번 게시글을 찾을 수 없습니다.");
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

    @Override
    public void saveLike(LikeSaveDTO likeSaveDTO){
        dynamicLikeRepository.createDynamicLike(likeSaveDTO);
        dynamicLikeRepository.insertDynamicLike(likeSaveDTO);
    }

    @Override
    public void increaseViewCount (Long postId) {
        postJPARepository.increaseViewCount(postId);
    }

    @Override
    public List<PostTop4DTO> getRecentTop4Posts() {
        List<PostTop4DTO> top4DTOs = postJPARepository.findAllByOrderByCreatedAtDesc(PageRequest.of(0, 4))
                .stream()
                .map(post -> PostTop4DTO.builder()
                        .title(post.getTitle())
                        .imageUrl(post.getImageUrl1())
                        .postId(post.getPostId())
                        .build())
                .collect(Collectors.toList());

        return top4DTOs;
    }
}
