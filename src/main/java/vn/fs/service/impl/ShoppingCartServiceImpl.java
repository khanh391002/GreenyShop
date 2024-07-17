package vn.fs.service.impl;

import java.security.Principal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.util.Strings;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vn.fs.model.dto.CouponDTO;
import vn.fs.model.dto.ProductDTO;
import vn.fs.model.dto.UserDTO;
import vn.fs.model.entities.CartItem;
import vn.fs.model.entities.Product;
import vn.fs.model.entities.User;
import vn.fs.repository.UserRepository;
import vn.fs.service.CouponService;
import vn.fs.service.ProductService;
import vn.fs.service.ShoppingCartService;
import vn.fs.service.UserService;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
	
	@Autowired
	private CouponService couponService;
	
	@Autowired
	HttpSession session;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	private ModelMapper mapper;

	private Map<Long, CartItem> map = new HashMap<Long, CartItem>(); // <Long, CartItem>

	@Override
	public void add(Long productId, CartItem item,Principal principal) {
		UserDTO userDTO = null;
		if (principal != null) {
			User user = userRepository.findByEmail(principal.getName());
			userDTO = mapper.map(user, UserDTO.class);
		}
		if (userDTO != null) {
			ProductDTO productDTO = productService.findById(productId);
			
			CartItem existedItem = map.get(userDTO.getUserId());

			if (!Objects.isNull(existedItem)) {
				existedItem.setQuantity(item.getQuantity() + existedItem.getQuantity());
				existedItem.setTotalPrice(item.getTotalPrice() + existedItem.getUnitPrice() * existedItem.getQuantity());
			} else {
				map.put(userDTO.getUserId(), item);
			}
		}

	}

	@Override
	public void remove(CartItem item) {

		map.remove(item.getId());

	}

	@Override
	public Collection<CartItem> getCartItems() {
		return map.values();
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public double getAmount() {
		return map.values().stream().mapToDouble(item -> item.getQuantity() * item.getUnitPrice()).sum();
	}

	@Override
	public int getCount() {
		if (map.isEmpty()) {
			return 0;
		}

		return map.values().size();
	}

	@Override
	public void remove(Product product) {

	}

	@Override
    @Transactional
    public String updateCart(Long id, int quantity, String coupon, HttpServletRequest request, RedirectAttributes redirectAttributes) {
//        UserDTO currentUser = userService.getCurrentUser();
//        HttpSession session = cartCache.getCache().getIfPresent(currentUser.getId());
//
//        Map<Long, CartItemDTO> map = (Map<Long, CartItemDTO>) session.getAttribute("cart");
		session = request.getSession();
		CartItem existedItem = map.get(id);
//        Integer totalOfCart = (Integer) session.getAttribute("totalOfCart");
//        Double totalPrice = (Double) session.getAttribute("totalPrice");
		Integer totalOfCart = existedItem.getQuantity();
        Double totalPrice = existedItem.getTotalPrice();

        if (Strings.isEmpty(coupon)) {
            if (Objects.nonNull(id)) {
                totalOfCart += quantity;
                totalPrice += quantity * map.get(id).getProduct().getPrice();
//                CartItemDTO dto = map.get(id);
//                dto.setQuantity(dto.getQuantity() + quantity);
                existedItem.setQuantity(existedItem.getQuantity() + quantity);
                existedItem.setTotalPrice(totalPrice);

//                session.setAttribute("cart", map);
                session.setAttribute("totalPriceAfterApplyCoupon", null);
                session.setAttribute("totalPrice", totalPrice);
                session.setAttribute("totalOfCart", totalOfCart);
                session.setAttribute("cartItems", existedItem);
            }
        } else {
            CouponDTO couponDTO = couponService.findCode(coupon, redirectAttributes);
            if (Objects.isNull(couponDTO)) return "redirect:/shoppingCart_checkout";

            Double totalPriceAfterApplyCoupon = totalPrice - (totalPrice * couponDTO.getDiscount() / 100);
            existedItem.setTotalPrice(totalPriceAfterApplyCoupon);
            session.setAttribute("totalPriceAfterApplyCoupon", totalPriceAfterApplyCoupon);
            session.setAttribute("totalPrice", totalPrice);
            session.setAttribute("coupon", couponDTO.getId());
            session.setAttribute("discount", couponDTO.getDiscount());
        }
        return "redirect:/shoppingCart_checkout";
    }
}
