package com.spring.shopping.service;

import com.spring.shopping.repository.CouponCountRepository;
import com.spring.shopping.repository.CouponRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CouponRedisServiceTest {

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponRepository couponRepository;




    @AfterEach
    public void dbreset() {
        couponRepository.deleteAll(); // 모든 로직이 끝나면 디비 초기화
        couponService.reset();
    }




    @Test
    @DisplayName("쿠폰을 하나 발급해서 디비에 하나의 내역이 저장되는지 확인한다.")
    public void 쿠폰발급하나만() {
        couponService.confirm(1L); // 1번 유저에게 쿠폰 발급

        long count = couponRepository.count();

        assertEquals(1, count);
    }

    @Test
    @DisplayName("쿠폰발급은 멀티쓰레드 형식으로 1000개의 요청을 넣고 100개만 발급되고 나머지는 무시되는지 확인")
    public void 쿠폰1000개생성요청100개만만들기() throws InterruptedException{
        int threadCount = 1000; // 100개 요청 동시에 넣기
        ExecutorService executorService = Executors.newFixedThreadPool(32); // 동시 요청을 도와주는 자바 유틸리티
        CountDownLatch countDownLatch = new CountDownLatch(threadCount); // 먼저 끝난 쓰레드가 대기하도록 교통정리

        // 테스트를 위해 테스트DB에 유저 1000명 더미데이터 적재해 두었음
        for(int i = 1; i <= 1000; i++) {
            long userId = i;
            executorService.submit(() -> { // 람다형식으로 각 쓰레드가 실행할 내역 작성
                try {
                    couponService.confirm(userId);
                } finally {
                    countDownLatch.countDown(); // 요청 마친 쓰레드는 대기
                }
            });
        }

        countDownLatch.await(); // 모든 쓰레드가 요청을 마치면 종료

        long count = couponRepository.count();

        assertEquals(100, count);
    }






}
