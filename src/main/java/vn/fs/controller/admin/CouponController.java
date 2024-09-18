package vn.fs.controller.admin;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.fs.model.entities.Coupon;
import vn.fs.model.entities.User;
import vn.fs.repository.UserRepository;
import vn.fs.service.CouponService;

@Controller
@RequestMapping("/admin/coupon")
public class CouponController {
	@Autowired
	private CouponService couponService;
	
	@Autowired
	UserRepository userRepository;
	
	@ModelAttribute(value = "user")
	public User user(Model model, Principal principal, User user) {

		if (principal != null) {
			model.addAttribute("user", new User());
			user = userRepository.findByEmail(principal.getName());
			model.addAttribute("user", user);
		}
		return user;
	}

	// show list category - table list
	@ModelAttribute()
	public List<Coupon> showCoupon(Model model) {
		List<Coupon> coupons = couponService.getAll();
		model.addAttribute("coupons", coupons);

		return coupons;
	}

    @GetMapping()
    public String listCoupon(Model model) {
        model.addAttribute("coupons", couponService.getAll());
        return "admin/coupon";
    }

    @GetMapping(value = "/new")
	public String showPopUpAddCoupon(Model model) {
		Coupon coupon = new Coupon();
		model.addAttribute("coupon", coupon);
		return "admin/createCoupon";
	}
    
    @PostMapping("/new")
    public String add(@ModelAttribute("coupon") Coupon coupon, Model model) {
        couponService.add(coupon, model);
        return "admin/coupon";
    }
    
    @GetMapping(value = "/update/{id}")
	public String showEditPage(@PathVariable("id") Long id, ModelMap model) {
		Coupon coupon = couponService.getById(id);
		if (Objects.isNull(coupon)) {
			return "error";
		}
		model.addAttribute("coupon", coupon);
		return "admin/editCoupon";
	}

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Long id, @ModelAttribute("coupon") Coupon coupon, Model model) {
        couponService.update(id, coupon, model);
        model.addAttribute("coupons", couponService.getAll());
        return "admin/coupon";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, Model model) {
        couponService.delete(id);
        return "redirect:/admin/coupon";
    }
}
