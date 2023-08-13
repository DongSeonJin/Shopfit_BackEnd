package com.spring.news.repository;

import com.spring.news.DTO.NewsViewResponseDTO;
import com.spring.news.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    List<News> findByTitleContaining(String keyword);


}
