package com.example.api.service;

import com.example.api.repository.CouponCountRepository;
import com.example.api.repository.CouponRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ApplyServiceTest {

    @Autowired
    private ApplyService applyService;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponCountRepository couponCountRepository;

    @Test
    public void 한번만응모() {
        applyService.apply_v1(1L);

        long count = couponRepository.count();

        assertThat(count).isEqualTo(1);
    }


    @Test
    public void 여러명응모_V1() throws InterruptedException {
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            long userId = i;
            executorService.submit(() -> {
                try {
                    applyService.apply_v1(userId);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        long count = couponRepository.count();

        /**
         * Race Condition 때문에 100 제한을 걸어두었지만,초과해서 발행된다.
         * */
        assertThat(count).isNotEqualTo(100);
    }

    @Test
        public void 여러명응모_V2() throws InterruptedException {
            int threadCount = 1000;
            ExecutorService executorService = Executors.newFixedThreadPool(32);
            CountDownLatch latch = new CountDownLatch(threadCount);

            for (int i = 0; i < threadCount; i++) {
                long userId = i;
                executorService.submit(() -> {
                    try {
                        applyService.apply_v2(userId);
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await();

            long count = couponRepository.count();

            /**
             * Race Condition 때문에 100 제한을 걸어두었지만,초과해서 발행된다.
             * */
            assertThat(count).isEqualTo(100);
    }

    @Test
    public void 여러명응모_V3() throws InterruptedException {
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            long userId = i;
            executorService.submit(() -> {
                try {
                    applyService.apply_v3(userId);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Thread.sleep(10000);

        long count = couponRepository.count();

        /**
         * Race Condition 때문에 100 제한을 걸어두었지만,초과해서 발행된다.
         * */
        assertThat(count).isEqualTo(100);
    }

    @Test
    public void 여러명응모_한개의쿠폰만_빌급_V4() throws InterruptedException {
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            long userId = i;
            executorService.submit(() -> {
                try {
                    applyService.apply_v4(1L);  // 1이라는 유저가 1000개의 요청 보내는 케이스
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Thread.sleep(10000);

        long count = couponRepository.count();

        assertThat(count).isEqualTo(1);
    }
}