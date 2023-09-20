package com.spring.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.spring.shopping.entity.Coupon;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity @Getter @Builder @ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class) //CreatedAt, updatedAt 자동으로 현재시간 설정하는 JPA
@Table(name = "users")// mysql에서 USER를 테이블명으로 지정할 수 없으므로 users로 생성

public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", updatable = false)
    private Long userId;

    @Column(name = "email", nullable = false, unique = true)
    @Pattern(regexp = ".+@.+\\..+", message = "잘못된 이메일 형식입니다.")
    private String email;

    @Column(name = "password", nullable = false, unique = true)
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*./]*$", message = "잘못된 비밀번호 형식입니다.")
    @Size(min = 10, message = "비밀번호는 최소 10자 이상이여야 합니다.")
    private String password;

    @Column(name = "nickname")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]*$", message = "잘못된 닉네임 형식입니다.")
    @Size(min = 3, max = 10, message = "닉네임은 최소 3글자, 최대 10글자까지 설정해주세요.")
    private String nickname;

    @Column(name = "point")
    private int point;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "created_at")
    @CreatedDate // 자동으로 생성일자로 설정
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate // 자동으로 업데이트일자로 설정
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @OneToMany(mappedBy = "user")
    private List<Coupon> coupons;

    @Builder
    public User(String email, String password){
        this.email = email;
        this.password = password;
    }

    @Override // 권한 반환
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override // 사용자의 비밀번호 반환
    public String getPassword() {
        return password;
    }

    @Override // 사용자의 id를 반환(unique한 요소만 가능)
    public String getUsername() {
        return email;
    }

    @Override // 계정 만료 여부 반환
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override // 계정 잠금 여부 반환
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override // 패스워드의 만료 여부 반환
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override // 계정 사용 가능 여부 반환
    public boolean isEnabled() {
        return true;
    }

    // 엔터티의 불변성을 지키려려면, @Setter를 사용하지 않는게 좋기때문에 넣은 별도의 업데이트 메서드.
    // builder패턴은 JPA에서 변경을 감지하지 못하기때문에 문제의 여지가 있음.


    // 테스트코드 우회 접근
    public static User createUser() {
        return new User();
    }
}
