package vn.fs.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.fs.model.entities.CartItem;
import vn.fs.model.entities.Product;
import vn.fs.service.ShoppingCartService;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
	
	private Map<Long, CartItem> map = new HashMap<Long, CartItem>(); // <Long, CartItem>

	@Override
	@Transactional
	public void add(CartItem item) {
		CartItem existedItem = map.get(item.getId());

		if (existedItem != null) {
			existedItem.setQuantity(item.getQuantity() + existedItem.getQuantity());
			existedItem.setTotalPrice(existedItem.getTotalPrice() + item.getUnitPrice() * item.getQuantity());
		} else {
 			map.put(item.getId(), item);
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
	public String updateCart(Long id, int quantity, HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (Objects.nonNull(id)) {
			CartItem item = map.get(id);
			item.setQuantity(item.getQuantity() + quantity);
			item.setTotalPrice(item.getTotalPrice() + item.getUnitPrice() * quantity);
		}
		session.setAttribute("cartItems", map);

		return "redirect:/products";
	}
}
