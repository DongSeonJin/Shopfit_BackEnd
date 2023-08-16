package com.spring.community.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Getter @ToString @Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer categoryId;

    @Column
    private String categoryName;

}
