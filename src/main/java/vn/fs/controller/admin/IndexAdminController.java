package vn.fs.controller.admin;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.fs.model.entities.User;
import vn.fs.repository.CategoryRepository;
import vn.fs.repository.OrderDetailRepository;
import vn.fs.repository.OrderRepository;
import vn.fs.repository.ProductRepository;
import vn.fs.repository.UserRepository;
import vn.fs.service.AdminStatisticService;

@Controller
@RequestMapping("/admin")
public class IndexAdminController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	OrderDetailRepository orderDetailRepository;

	@Autowired
	AdminStatisticService adminStatisticService;

	@ModelAttribute(value = "user")
	public User user(Model model, Principal principal, User user) {

		if (principal != null) {
			model.addAttribute("user", new User());
			user = userRepository.findByEmail(principal.getName());
			model.addAttribute("user", user);
		}

		return user;
	}

	@GetMapping(value = "/home")
	public String index(Model model) {
		model.addAttribute("orderToday", adminStatisticService.countOrderStatisticToday());
		model.addAttribute("totalProduct", productRepository.countAllProductByIsDeletedIsFalse());
		model.addAttribute("totalCustomer", userRepository.countAllUserIsCustomer());
		model.addAttribute("totalCategory", categoryRepository.countAllCategory());
		model.addAttribute("dailySale", orderRepository.getDailySales());
		model.addAttribute("newOrderRatio", orderRepository.getNewOrderRatio());
		model.addAttribute("totalOrder", orderRepository.getTotalOrder());
		List<Long> topProductIds = orderDetailRepository.getTop3BestSellProduct();
		model.addAttribute("top3BestSellProducts", productRepository.findAllByProductIdIn(topProductIds));
		return "admin/index";
	}
}
