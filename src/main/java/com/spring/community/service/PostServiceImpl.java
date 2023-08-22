package com.spring.community.service;

import com.spring.community.DTO.LikeSaveDTO;
import com.spring.community.DTO.PostListResponseDTO;
import com.spring.community.DTO.PostSaveDTO;
import com.spring.community.DTO.PostUpdateDTO;
import com.spring.community.entity.Post;
import com.spring.community.exception.PostIdNotFoundException;
import com.spring.community.repository.DynamicLikeRepository;
import com.spring.community.repository.DynamicPostRepository;
import com.spring.community.repository.PostJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService{


    @Autowired
    PostJPARepository postJPARepository;
    @Autowired
    DynamicPostRepository dynamicPostRepository;

    @Autowired
    DynamicLikeRepository dynamicLikeRepository;

    @Autowired
    public PostServiceImpl(PostJPARepository postRepository){
        this.postJPARepository = postRepository;
    }

    // 생성자 주입으로 변경

    @Override
    public Post getPostById(Long id) {
        return postJPARepository.findById(id)
                .orElseThrow(() -> new PostIdNotFoundException("Post with id " + id + " not found."));
    }

    @Override
    public List<Post> getAllPosts() {

        return postJPARepository.findAll();
    }

    @Override
    public List<PostListResponseDTO> getPostsByCategoryId(Integer categoryId){
        List<PostListResponseDTO> Postlist = postJPARepository.findByPostCategory_CategoryId(categoryId)
                .stream().map(PostListResponseDTO::new).collect(Collectors.toList()); // entity를 DTO로 변환해주는 메서드
        return  Postlist;
    }

    @Override
    public void savePost(PostSaveDTO postSaveDTO) {
        dynamicPostRepository.createDynamicTable(postSaveDTO);// 없는 테이블명(nickname)일 시, 테이블 생성
        dynamicPostRepository.insertDynamicTable(postSaveDTO); // 데이터 삽입
        dynamicPostRepository.insertPostTable(postSaveDTO);// 검색용 통합 테이블에 삽입.
    }

    @Override
    public void deletePostById(Long id) {
        postJPARepository.deleteById(id);
    }

    @Override
    public void update(PostUpdateDTO postUpdateDTO) {
        //Post post = postRepository.findById(postUpdateDTO.getPostId()).get();


        // entity에 setter를 넣는것은 불변성을 위반하기 때문에 builder로 구현.
        Post modifiedPost = Post.builder()
                .postId(postUpdateDTO.getPostId())
                .title(postUpdateDTO.getTitle())
                .content(postUpdateDTO.getContent())
                .updatedAt(LocalDateTime.now())
                .build(); // 추후 DTO에 메서드 추가하고, builder붙여서 DTO로 모두 교체하는 리펙토링 시도.

        postJPARepository.save(modifiedPost); // JPA의 save메서드는 DB에 존재하는 id일경우 update, 없을경우 insert
    }

    @Override
    public void saveLike(LikeSaveDTO likeSaveDTO){
        dynamicLikeRepository.createDynamicLike(likeSaveDTO);
        dynamicLikeRepository.insertDynamicLike(likeSaveDTO);
    }

    // post 조회수 증가
    @Override
    public void increaseViewCount (Long postId) {
        postJPARepository.increaseViewCount(postId);
    }
}
