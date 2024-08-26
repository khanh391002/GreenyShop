package vn.fs.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.fs.model.entities.CartItem;
import vn.fs.service.ShoppingCartService;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
public class RestController {
	
	@Autowired
	ShoppingCartService shoppingCartService;
	
	@GetMapping(value = "/carts")
	public Collection<CartItem> getCarts() {
		return shoppingCartService.getCartItems();
	}
	
	@GetMapping(value = "/total-cart-items")
	public Integer getTotalCartItems() {
		return shoppingCartService.getCount();
	}
}
