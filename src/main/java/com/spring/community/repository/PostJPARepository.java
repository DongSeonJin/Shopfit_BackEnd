package com.spring.community.repository;

import com.spring.community.entity.Post;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PostJPARepository extends JpaRepository<Post, Long> {

    List<Post> findByPostCategory_CategoryId(Integer categoryId);

    @Modifying
    @Transactional
    @Query ("UPDATE Post p SET p.viewCount = p.viewCount +1 WHERE p.postId = :postId")
    // 조건에 맞는 postId가 postId 랑 일치할 때 Post 테이블의 조회수 +1.
    void increaseViewCount(@Param("postId") Long postId);

    @Query("SELECT p FROM Post p ORDER BY p.createdAt DESC")
    List<Post> findTop4RecentPosts(Pageable pageable);

    List<Post> findAllByOrderByCreatedAtDesc(PageRequest of); // 정의 추가
}
