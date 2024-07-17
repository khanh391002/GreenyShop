package vn.fs.service;

import java.security.Principal;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vn.fs.model.entities.CartItem;
import vn.fs.model.entities.Product;

@Service
public interface ShoppingCartService {

	int getCount();

	double getAmount();

	void clear();

	Collection<CartItem> getCartItems();

	void remove(CartItem item);

	void add(Long productId, CartItem item, Principal principal);

	void remove(Product product);

	String updateCart(Long id, int quantity, String coupon, HttpServletRequest request, RedirectAttributes redirectAttributes);

}
