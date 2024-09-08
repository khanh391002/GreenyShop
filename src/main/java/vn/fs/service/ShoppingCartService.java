package vn.fs.service;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import vn.fs.model.entities.CartItem;
import vn.fs.model.entities.Product;

@Service
public interface ShoppingCartService {

	int getCount();

	double getAmount();

	void clear();

	Collection<CartItem> getCartItems();

	void remove(CartItem item);

	void add(CartItem item);

	void remove(Product product);
	
	String updateCart(Long id, int quantity, String coupon, HttpServletRequest request);

}
