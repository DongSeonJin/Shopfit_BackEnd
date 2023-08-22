package com.spring.community.repository;

import com.spring.community.DTO.PostListResponseDTO;
import com.spring.community.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PostJPARepository extends JpaRepository<Post, Long> {


    List<Post> findByPostCategory_CategoryId(Integer categoryId);


}
