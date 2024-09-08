package vn.fs.service;

import java.util.List;

import org.springframework.ui.Model;

import vn.fs.model.entities.Coupon;

public interface CouponService {
	
    Coupon findCode(String code);

    void add(Coupon coupon, Model model);

    List<Coupon> getAll();

    void getByDiscountMax(Model model);

    void update(Coupon coupon, Model model);

    void delete(Long id);

    Coupon getById(Long couponId);
}
