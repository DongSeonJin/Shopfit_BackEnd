package com.spring.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity @Getter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "inquiry_response")
public class InquiryResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "response_id")
    private Integer responseId;

    @ManyToOne
    @JoinColumn(name = "inquiry_id")
    @OnDelete(action = OnDeleteAction.CASCADE) // on delete cascade를 ddl에 직접 입력해주는 방식. 문의가삭제되면 따라서 삭제.
    private Inquiry inquiry;

    @Column(name = "response_content")
    private String responseContent;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
