package vn.fs.service.impl;

import java.time.LocalDate;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vn.fs.model.dto.CouponDTO;
import vn.fs.model.entities.Coupon;
import vn.fs.repository.CouponRepository;
import vn.fs.service.CouponService;

@Service
public class CouponServiceImpl implements CouponService{
	
	@Autowired
	private ModelMapper mapper;

	@Autowired
	private CouponRepository couponRepository;
	
	@Override
	public CouponDTO findCode(String code, RedirectAttributes redirectAttributes) {
		Optional<Coupon> optional = couponRepository.findByCodeAndExpiresFalseAndDeletedFalse(code);
	    if (!optional.isPresent()) {
	        redirectAttributes.addFlashAttribute("err", "The code does not exist");
	        return null;
	    }
	    Coupon coupon = optional.get();
	    if (LocalDate.now().isAfter(coupon.getExpirationDate())) {
	        redirectAttributes.addFlashAttribute("err", "Expired code");
	        return null;
	    }
	    return mapper.map(coupon, CouponDTO.class);
	}

}
