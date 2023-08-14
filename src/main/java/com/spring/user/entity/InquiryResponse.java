package com.spring.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "inquiry_response")
public class InquiryResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "response_id")
    private Integer responseId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "inquiry_id", referencedColumnName = "inquiry_id")
    private Inquiry inquiry;

    @Column(name = "response_content")
    private String responseContent;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
