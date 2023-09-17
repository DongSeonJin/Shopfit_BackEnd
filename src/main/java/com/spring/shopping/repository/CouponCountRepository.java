package com.spring.shopping.repository;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@AllArgsConstructor
@Repository
public class CouponCountRepository {

    private RedisTemplate<String, String> redisTemplate;

    public Long increment() {
        // Redis 데이터베이스에서 "count"라는 키의 값을 증가시키는 메서드 - 카운터 구현
        Long result = redisTemplate.opsForValue().increment("count", 1); //1을 증가시키도록 수정
//        System.out.println("현재 count 값 :" + result);
        return result;
        // incr count (Redis cli의 명령어와 유사하게 동작)

    }

    public void reset() {
        redisTemplate.opsForValue().set("count", "0"); // couponcount를 0으로 reset하기
    }

}

// 도커에서 레디스 설치하기
// docker pull redis
// 도커에서 "myredis"라는 이름으로 레디스 컨테이너 실행하기
// docker run --name myredis -d -p 6379:6379 redis

// 도커에서 프로세스 종료하기
// docker stop myredis
// 도커에서 서버리스트 조회하기 (종료된 컨테이너 포함)
// docker ps -a
// 도커에서 종료된 프로세스 다시 실행하기
// docker restart myredis

// "count"라는 키의 값 조작을 위해 redis-cli 접속하는 명령어
// docker exec -it myredis redis-cli
// count 값 1 증가 명령어
// incr count
// count 값 초기화 명령어
// set count 0
// count 값 조회 명령어
// get count






