package com.spring.community.repository;

import com.spring.community.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findAllByPost_PostId(Long postId); // findAllByPostId로 하면 Spring Data JPA 가 연관 관계를 올바르게 해석하지 못한다고 함.

    @Modifying
    @Query("DELETE FROM Reply r WHERE r.post.postId = :postId")
    void deleteByReplyId(@Param("postId") Long postId);

    // 해당 게시글에 달린 댓글 개수 조회
    long countByPost_PostId (Long postId);

}

