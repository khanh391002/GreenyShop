package vn.fs.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import vn.fs.commom.CommomDataService;
import vn.fs.commom.Constants;
import vn.fs.model.entities.Order;
import vn.fs.model.entities.OrderDetail;
import vn.fs.model.entities.User;
import vn.fs.model.response.UserResponse;
import vn.fs.repository.OrderDetailRepository;
import vn.fs.repository.OrderRepository;
import vn.fs.repository.UserRepository;
import vn.fs.service.UserService;

@Controller
public class ProfileController extends CommomController {
	
	@Value("${upload.path}")
	private String pathUploadImage;

	@Autowired
	UserRepository userRepository;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderDetailRepository orderDetailRepository;

	@Autowired
	CommomDataService commomDataService;
	
	@Autowired
	UserService userService;

	@GetMapping(value = "/profile")
	public String profile(Model model, Principal principal, User user, Pageable pageable,
			@RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {

		if (principal != null) {

			model.addAttribute("user", new User());
			user = userRepository.findByEmail(principal.getName());
			model.addAttribute("user", user);
		}

		int currentPage = page.orElse(1);
		int pageSize = size.orElse(6);

		Page<Order> orderPage = findPaginated(PageRequest.of(currentPage - 1, pageSize), user);

		int totalPages = orderPage.getTotalPages();
		if (totalPages > 0) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}

		commomDataService.commonData(model, user);
		model.addAttribute("orderByUser", orderPage);

		return "web/profile";
	}

	public Page<Order> findPaginated(Pageable pageable, User user) {

		List<Order> orderPage = orderRepository.findOrderByUserId(user.getUserId());

		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;
		List<Order> list;

		if (orderPage.size() < startItem) {
			list = Collections.emptyList();
		} else {
			int toIndex = Math.min(startItem + pageSize, orderPage.size());
			list = orderPage.subList(startItem, toIndex);
		}

		Page<Order> orderPages = new PageImpl<Order>(list, PageRequest.of(currentPage, pageSize), orderPage.size());

		return orderPages;
	}

	@GetMapping("/order/detail/{order_id}")
	public ModelAndView detail(Model model, Principal principal, User user, @PathVariable("order_id") Long id) {

		if (principal != null) {

			model.addAttribute("user", new User());
			user = userRepository.findByEmail(principal.getName());
			model.addAttribute("user", user);
		}
		Optional<Order> orderOpt = orderRepository.findById(id);
		if (!orderOpt.isPresent()) {
			model.addAttribute("error", "Đơn hàng không tồn tại!");
			return new ModelAndView("web/profile");
		}
		Order order = orderOpt.get();
		String orderStatus = "";
		if (Constants.ORDER_STATUS_PENDING == order.getStatus()) {
			orderStatus = String.format(Constants.STATUS_ORDER_HTML, "#0e76a8", Constants.STATUS_PENDING);
		} else if (Constants.ORDER_STATUS_DELIVERY == order.getStatus()) {
			orderStatus = String.format(Constants.STATUS_ORDER_HTML, "#ffab10", Constants.STATUS_DELIVERY);
		} else if (Constants.ORDER_STATUS_APPROVED == order.getStatus()) {
			orderStatus = String.format(Constants.STATUS_ORDER_HTML, "#119744", Constants.STATUS_APPROVED);
		} else if (Constants.ORDER_STATUS_REJECTED == order.getStatus()) {
			orderStatus = String.format(Constants.STATUS_ORDER_HTML, "#ff3838", Constants.STATUS_REJECTED);
		}
		List<OrderDetail> listO = orderDetailRepository.findByOrderId(id);

//		model.addAttribute("amount", orderRepository.findById(id).get().getAmount());
		model.addAttribute("orderDetail", listO);
		model.addAttribute("orderStatus", orderStatus);
//		model.addAttribute("orderId", id);
		// set active front-end
//		model.addAttribute("menuO", "menu");
		commomDataService.commonData(model, user);

		return new ModelAndView("web/historyOrderDetail");
	}

	@RequestMapping("/order/cancel/{order_id}")
	public ModelAndView cancel(ModelMap model, @PathVariable("order_id") Long id) {
		Optional<Order> o = orderRepository.findById(id);
		if (o.isPresent()) {
			return new ModelAndView("redirect:/profile", model);
		}
		Order oReal = o.get();
		oReal.setStatus((short) 3);
		orderRepository.save(oReal);

		return new ModelAndView("redirect:/profile", model);
	}

	// Xử lý thông tin chỉnh sửa
	@PostMapping("/profile/edit")
	public String updateProfile(@ModelAttribute UserResponse user, Model model,
			@RequestParam("file") MultipartFile file, Principal principal) {
		String avatar = null;
		if (file.getSize() > 0) {
			avatar = file.getOriginalFilename();
			try {
				if (file.getSize() > 0) {
					avatar = file.getOriginalFilename();
				} else {
					avatar = user.getAvatar();
				}
				File convFile = new File(pathUploadImage + "/" + (avatar));
				FileOutputStream fos = new FileOutputStream(convFile);
				fos.write(file.getBytes());
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
				return "error";
			}
		} else {
			avatar = user.getAvatar();
		}
		if (avatar != null) {
			user.setAvatar(avatar);
		}
		userService.updateProfile(principal.getName(), user, model);
//		User user = userService.findByEmail(principal.getName());
//		model.addAttribute("user", user);
		return "redirect:/profile";
	}

}
