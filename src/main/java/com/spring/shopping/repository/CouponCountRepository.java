package com.spring.shopping.repository;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@AllArgsConstructor
@Repository
public class CouponCountRepository {

    private RedisTemplate<String, String> redisTemplate;

    public Long increment() {
        // Redis 데이터베이스에서 "couponcount"라는 키의 값을 증가시키는 메서드 - 카운터 구현
        return redisTemplate.opsForValue().increment("couponcount"); // incr couponcount (Redis cli의 명령어와 유사하게 동작)
    }

    public void reset() {
        redisTemplate.opsForValue().set("couponcount", "0"); // couponcount를 0으로 reset하기
    }

}
