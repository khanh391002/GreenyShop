package vn.fs.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

import vn.fs.commom.CommomDataService;
import vn.fs.commom.utils.Utils;
import vn.fs.config.PaypalPaymentIntent;
import vn.fs.config.PaypalPaymentMethod;
import vn.fs.model.entities.CartItem;
import vn.fs.model.entities.Coupon;
import vn.fs.model.entities.Order;
import vn.fs.model.entities.OrderDetail;
import vn.fs.model.entities.Product;
import vn.fs.model.entities.User;
import vn.fs.repository.OrderDetailRepository;
import vn.fs.repository.OrderRepository;
import vn.fs.service.CouponService;
import vn.fs.service.PaypalService;
import vn.fs.service.ShoppingCartService;
import vn.fs.service.UserService;


@Controller
public class CartController extends CommomController {

	@Autowired
	HttpSession session;

	@Autowired
	CommomDataService commomDataService;

	@Autowired
	ShoppingCartService shoppingCartService;

	@Autowired
	private PaypalService paypalService;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderDetailRepository orderDetailRepository;
	
	@Autowired
	CouponService couponService;
	
	@Autowired
	UserService userService;

	public Order orderFinal = new Order();

	public static final String URL_PAYPAL_SUCCESS = "pay/success";
	public static final String URL_PAYPAL_CANCEL = "pay/cancel";
	private Logger log = LoggerFactory.getLogger(getClass());

	@GetMapping(value = "/shoppingCart_checkout")
	public String shoppingCart(Model model) {

		Collection<CartItem> cartItems = shoppingCartService.getCartItems();
		model.addAttribute("cartItems", cartItems);
		model.addAttribute("total", shoppingCartService.getAmount());
		double totalPrice = 0;
		for (CartItem cartItem : cartItems) {
			double price = cartItem.getQuantity() * cartItem.getProduct().getPrice();
			totalPrice += price;
		}

		model.addAttribute("totalPrice", totalPrice);
		model.addAttribute("totalCartItems", shoppingCartService.getCount());

		return "web/shoppingCart_checkout";
	}

	// add cartItem
	@GetMapping(value = "/addToCart")
	public String add(@RequestParam("productId") Long productId, HttpServletRequest request, Model model) {

		Product product = productRepository.findById(productId).orElse(null);

		session = request.getSession();
		Collection<CartItem> cartItems = shoppingCartService.getCartItems();
		if (product != null) {
			CartItem item = new CartItem();
			BeanUtils.copyProperties(product, item);
			item.setQuantity(1);
			item.setProduct(product);
			item.setId(productId);
			item.setUnitPrice(product.getPrice());
			item.setTotalPrice(product.getPrice());
			shoppingCartService.add(item);
		}
		session.setAttribute("cartItems", cartItems);
		model.addAttribute("totalCartItems", shoppingCartService.getCount());
		
		return "redirect:/products";
	}
	
	@GetMapping("/update-cart")
    public String updateToCart(@RequestParam(name = "id") Long id, @RequestParam(name = "quantity") int quantity, HttpServletRequest request, Model model) {
		model.addAttribute("totalCartItems", shoppingCartService.getCount());
        return shoppingCartService.updateCart(id, quantity, request);
    }

	// delete cartItem
	@SuppressWarnings("unlikely-arg-type")
	@GetMapping(value = "/remove/{id}")
	public String remove(@PathVariable("id") Long id, HttpServletRequest request, Model model) {
		Product product = productRepository.findById(id).orElse(null);

		Collection<CartItem> cartItems = shoppingCartService.getCartItems();
		session = request.getSession();
		if (product != null) {
			CartItem item = new CartItem();
			BeanUtils.copyProperties(product, item);
			item.setProduct(product);
			item.setId(id);
			cartItems.remove(session);
			shoppingCartService.remove(item);
		}
		model.addAttribute("totalCartItems", shoppingCartService.getCount());
		return "redirect:/checkout";
	}

	// show check out
	@GetMapping(value = "/checkout")
	public String checkOut(Model model, User user) {

		Order order = new Order();
		model.addAttribute("order", order);

		Collection<CartItem> cartItems = shoppingCartService.getCartItems();
		model.addAttribute("cartItems", cartItems);
		model.addAttribute("total", shoppingCartService.getAmount());
		model.addAttribute("NoOfItems", shoppingCartService.getCount());
		double totalPrice = 0;
		for (CartItem cartItem : cartItems) {
			double price = cartItem.getQuantity() * cartItem.getProduct().getPrice();
			totalPrice += price;
		}

		session.setAttribute("totalPrice", totalPrice);
		session.setAttribute("totalCartItems", shoppingCartService.getCount());
		model.addAttribute("totalPrice", totalPrice);
		model.addAttribute("totalCartItems", shoppingCartService.getCount());
		commomDataService.commonData(model, user);

		return "web/shoppingCart_checkout";
	}

	// submit checkout
	@PostMapping(value = "/checkout")
	@Transactional
	public String checkedOut(Model model, Order order, HttpServletRequest request, User user)
			throws MessagingException {

		session = request.getSession();
		String checkOut = request.getParameter("checkOut");
		Integer totalOfCart = (Integer) session.getAttribute("totalOfCart");
        Double totalPrice = (Double) session.getAttribute("totalPrice");
        String couponCode = (String) session.getAttribute("couponCode");

		Collection<CartItem> cartItems = shoppingCartService.getCartItems();

//		double totalPrice = 0;
//		for (CartItem cartItem : cartItems) {
//			double price = cartItem.getQuantity() * cartItem.getProduct().getPrice();
//			totalPrice += price;
//		}

		BeanUtils.copyProperties(order, orderFinal);
		if (StringUtils.equals(checkOut, "paypal")) {

			String cancelUrl = Utils.getBaseURL(request) + "/" + URL_PAYPAL_CANCEL;
			String successUrl = Utils.getBaseURL(request) + "/" + URL_PAYPAL_SUCCESS;
			try {
				totalPrice = totalPrice / 24;
				Payment payment = paypalService.createPayment(totalPrice, "USD", PaypalPaymentMethod.paypal,
						PaypalPaymentIntent.sale, "payment description", cancelUrl, successUrl);
				for (Links links : payment.getLinks()) {
					if (links.getRel().equals("approval_url")) {
						return "redirect:" + links.getHref();
					}
				}
			} catch (PayPalRESTException e) {
				log.error(e.getMessage());
			}

		}

		Date date = new Date();
		order.setOrderDate(date);
		order.setStatus(0);
		order.getOrderId();

		order.setAmount(totalPrice);
		order.setUser(user);

		orderRepository.save(order);

		List<OrderDetail> orderDetails = new ArrayList<>();
		for (CartItem cartItem : cartItems) {
			OrderDetail orderDetail = new OrderDetail();
			orderDetail.setQuantity(cartItem.getQuantity());
			orderDetail.setOrder(order);
			orderDetail.setProduct(cartItem.getProduct());
			double unitPrice = cartItem.getProduct().getPrice();
			if (!StringUtils.isEmpty(couponCode)) {
				Coupon couponObj = couponService.findCode(couponCode);
				if (!Objects.isNull(couponObj)) {
					unitPrice = unitPrice - (unitPrice * couponObj.getDiscount() / 100);
				}
			}
			orderDetail.setPrice(unitPrice);
			cartItem.setUnitPrice(unitPrice);
			orderDetails.add(orderDetail);
		}
		orderDetailRepository.saveAll(orderDetails);
		// sendMail
		commomDataService.sendSimpleEmail(user.getEmail(), "Greeny-Shop Xác Nhận Đơn hàng",
				"Xác nhận đặt đơn thành công", cartItems, totalPrice, order);

		// lấy ra danh sách admin của hệ thống
		List<User> admins = userService.getAllUserIsAdmin();
		if (!CollectionUtils.isEmpty(admins)) {
			for (User admin : admins) {
				// send mail to admin
				commomDataService.sendEmailToAdmin(admin.getEmail(), "Đơn Hàng Mới Cần Xác Nhận",
						"Đơn Hàng Mới Cần Xác Nhận", user, order, admin);
			}
		}

		shoppingCartService.clear();
		session.removeAttribute("cartItems");
		model.addAttribute("orderId", order.getOrderId());

		return "redirect:/checkout_success";
	}

	// paypal
	@GetMapping(URL_PAYPAL_SUCCESS)
	public String successPay(@RequestParam("" + "" + "") String paymentId, @RequestParam("PayerID") String payerId,
			HttpServletRequest request, User user, Model model) throws MessagingException {
		Collection<CartItem> cartItems = shoppingCartService.getCartItems();
		model.addAttribute("cartItems", cartItems);
		model.addAttribute("total", shoppingCartService.getAmount());

		double totalPrice = 0;
		for (CartItem cartItem : cartItems) {
			double price = cartItem.getQuantity() * cartItem.getProduct().getPrice();
			totalPrice += price;
		}
		model.addAttribute("totalPrice", totalPrice);
		model.addAttribute("totalCartItems", shoppingCartService.getCount());

		try {
			Payment payment = paypalService.executePayment(paymentId, payerId);
			if (payment.getState().equals("approved")) {

				session = request.getSession();
				Date date = new Date();
				orderFinal.setOrderDate(date);
				orderFinal.setStatus(2);
				orderFinal.getOrderId();
				orderFinal.setUser(user);
				orderFinal.setAmount(totalPrice);
				orderRepository.save(orderFinal);

				for (CartItem cartItem : cartItems) {
					OrderDetail orderDetail = new OrderDetail();
					orderDetail.setQuantity(cartItem.getQuantity());
					orderDetail.setOrder(orderFinal);
					orderDetail.setProduct(cartItem.getProduct());
					double unitPrice = cartItem.getProduct().getPrice();
					orderDetail.setPrice(unitPrice);
					orderDetailRepository.save(orderDetail);
				}

				// sendMail
				commomDataService.sendSimpleEmail(user.getEmail(), "Greeny-Shop Xác Nhận Đơn hàng", "aaaa", cartItems,
						totalPrice, orderFinal);

				shoppingCartService.clear();
				session.removeAttribute("cartItems");
				model.addAttribute("orderId", orderFinal.getOrderId());
				orderFinal = new Order();
				return "redirect:/checkout_paypal_success";
			}
		} catch (PayPalRESTException e) {
			log.error(e.getMessage());
		}
		return "redirect:/";
	}

	// done checkout ship cod
	@GetMapping(value = "/checkout_success")
	public String checkoutSuccess(Model model, User user) {
		commomDataService.commonData(model, user);

		return "web/checkout_success";

	}

	// done checkout paypal
	@GetMapping(value = "/checkout_paypal_success")
	public String paypalSuccess(Model model, User user) {
		commomDataService.commonData(model, user);

		return "web/checkout_paypal_success";
	}
	
	@PostMapping("/coupon-cart")
    public String coupon(@RequestParam(name = "coupon") String coupon, Model model, User user, HttpServletRequest request) {
		Order order = new Order();
		model.addAttribute("order", order);
		session = request.getSession();

		Collection<CartItem> cartItems = shoppingCartService.getCartItems();
		model.addAttribute("cartItems", cartItems);
		model.addAttribute("total", shoppingCartService.getAmount());
		model.addAttribute("NoOfItems", shoppingCartService.getCount());
		double totalPrice = 0;
		for (CartItem cartItem : cartItems) {
			double price = cartItem.getQuantity() * cartItem.getProduct().getPrice();
			totalPrice += price;
		}
		if (!StringUtils.isEmpty(coupon)) {
			Coupon couponObj = couponService.findCode(coupon);
			if (!Objects.isNull(couponObj)) {
				totalPrice = totalPrice - (totalPrice * couponObj.getDiscount() / 100);
				session.setAttribute("couponCode", couponObj.getCode());
			}
		}
		model.addAttribute("totalPrice", totalPrice);
		model.addAttribute("totalCartItems", shoppingCartService.getCount());
		session.setAttribute("totalPrice", totalPrice);
		session.setAttribute("totalCartItems", shoppingCartService.getCount());
		commomDataService.commonData(model, user);

		return "web/shoppingCart_checkout";
    }
	
}
