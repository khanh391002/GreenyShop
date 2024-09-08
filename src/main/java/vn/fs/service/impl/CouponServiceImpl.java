package vn.fs.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import lombok.RequiredArgsConstructor;
import vn.fs.model.entities.Coupon;
import vn.fs.repository.CouponRepository;
import vn.fs.service.CouponService;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService{

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
	public void add(Coupon coupon, Model model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Coupon> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void getByDiscountMax(Model model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Coupon coupon, Model model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Coupon getById(Long couponId) {
		// TODO Auto-generated method stub
		return null;
	}

}
