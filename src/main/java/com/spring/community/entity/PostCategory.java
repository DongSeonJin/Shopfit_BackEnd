package com.spring.community.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Getter @ToString @Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column // OneToMany 붙일 예정, fetch = Lazy를 넣어 조인구문 시에만 호출되어 조회 성능 향상
    private Integer categoryId;

    @Column(nullable = false)
    private String categoryName;

}
