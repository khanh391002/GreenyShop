package vn.fs.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vn.fs.model.dto.CouponDTO;

@Service
public interface CouponService {
	CouponDTO findCode(String code, RedirectAttributes redirectAttributes);
}
