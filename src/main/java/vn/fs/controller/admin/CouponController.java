package vn.fs.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.fs.model.entities.Coupon;
import vn.fs.service.CouponService;

@Controller
@RequestMapping("/admin/coupon")
public class CouponController {
	@Autowired
	private CouponService couponService;

    @GetMapping()
    public String listCoupon(Model model) {
        model.addAttribute("coupons", couponService.getAll());
        return "redirect:/admin/coupon";
    }

    @PostMapping("/new")
    public String add(@ModelAttribute("coupon") Coupon coupon, Model model) {
        couponService.add(coupon, model);
        return "redirect:/admin/coupon";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("coupon") Coupon coupon, Model model) {
        couponService.update(coupon, model);
        model.addAttribute("coupons", couponService.getAll());
        return "redirect:/admin/coupon";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, Model model) {
        couponService.delete(id);
        return "redirect:/admin/coupon";
    }
}
