package com.example.api.service;

import com.example.api.domain.Coupon;
import com.example.api.producer.CouponCreateProducer;
import com.example.api.repository.AppliedUserRepository;
import com.example.api.repository.CouponCountRepository;
import com.example.api.repository.CouponRepository;
import org.springframework.stereotype.Service;

@Service
public class ApplyService {
    private final CouponRepository couponRepository;

    private final CouponCountRepository couponCountRepository;

    private final CouponCreateProducer couponeCreateProducer;

    private final AppliedUserRepository appliedUserRepository;

    public ApplyService(CouponRepository couponRepository, CouponCountRepository couponCountRepository, CouponCreateProducer couponeCreateProducer, AppliedUserRepository appliedUserRepository) {
        this.couponRepository = couponRepository;
        this.couponCountRepository = couponCountRepository;
        this.couponeCreateProducer = couponeCreateProducer;
        this.appliedUserRepository = appliedUserRepository;
    }

    public void apply_v1(Long userId){
        long count = couponRepository.count();

        if(count > 100){
            return;
        }

        couponRepository.save(new Coupon(userId));
    }

    public void apply_v2(Long userId){
        long count = couponCountRepository.increament();

        if(count > 100){
            return;
        }

        couponRepository.save(new Coupon(userId));
    }

    public void apply_v3(Long userId){
        long count = couponCountRepository.increament();

        if(count > 100){
            return;
        }

        couponeCreateProducer.create(userId);
    }

    public void apply_v4(Long userId){
        Long apply = appliedUserRepository.add(userId);

        if(apply != 1){
            return ;
        }

        long count = couponCountRepository.increament();

        if(count > 100){
            return;
        }

        couponeCreateProducer.create(userId);
    }
}
