package vn.fs.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import lombok.RequiredArgsConstructor;
import vn.fs.model.entities.Coupon;
import vn.fs.repository.CouponRepository;
import vn.fs.service.CouponService;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

	@Autowired
	private CouponRepository couponRepository;

	@Override
	public Coupon findCode(String code) {
		Optional<Coupon> optional = couponRepository.getByCodeAndExpiresFalseAndDeletedFalse(code);
		if (!optional.isPresent()) {
			return null;
		}
		Coupon coupon = optional.get();
		if (LocalDate.now().isAfter(coupon.getExpirationDate())) {
			return null;
		}
		return coupon;
	}

	@Override
	@Transactional
	public void add(Coupon coupon, Model model) {
		Optional<Coupon> optional = couponRepository.findByCodeAndDeletedFalse(coupon.getCode());
		if (optional.isPresent()) {
			model.addAttribute("error", "Code is already exist");
			return;
		}
		couponRepository.save(coupon);
	}

	@Override
	@Transactional
	public List<Coupon> getAll() {
		List<Coupon> coupons = couponRepository.findAllByDeletedFalse();
		for (Coupon coupon : coupons) {
			if (coupon.getExpirationDate().isBefore(LocalDate.now())) {
				coupon.setExpires(true);
			}
		}
		couponRepository.saveAll(coupons);
		return coupons;
	}

	@Override
	public void getByDiscountMax(Model model) {
		// TODO Auto-generated method stub

	}

	@Override
	@Transactional
	public void update(Long id, Coupon coupon, Model model) {
		Optional<Coupon> optional = couponRepository.findByCodeAndDeletedFalse(coupon.getCode());
		if (optional.isPresent()) {
			model.addAttribute("error", "Code is already exist");
			return;
		}
		Coupon getById = couponRepository.getById(id);
		getById.update(coupon).setExpires(getById.getExpirationDate().isBefore(LocalDate.now()));
		couponRepository.save(getById);
	}

	@Override
	public void delete(Long id) {
		Optional<Coupon> byId = couponRepository.findByIdAndDeletedFalse(id);
		byId.get().setDeleted(true);
		couponRepository.save(byId.get());

	}

	@Override
	public Coupon getById(Long couponId) {
		Optional<Coupon> couponOpt = couponRepository.findById(couponId);
		if (!couponOpt.isPresent()) {
			return null;
		}
		return couponOpt.get();
	}

}
