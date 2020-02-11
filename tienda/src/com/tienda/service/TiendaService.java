package com.tienda.service;

import java.util.List;
import java.util.Set;

import com.tienda.bean.Order;
import com.tienda.bean.Product;
import com.tienda.bean.User;
import com.tienda.bean.Wishlist;
import com.tienda.util.DataStore;
import com.tienda.util.OrderNotFoundException;
import com.tienda.util.ProductNotFoundException;
import com.tienda.util.UserNotFoundException;
import com.tienda.util.WishlistNotFoundException;

public class TiendaService {

	public User getUserDetails(String username) throws UserNotFoundException {
		return new DataStore().getUser(username);
	}
	
	public Product getProduct(String code) throws ProductNotFoundException {
		return new DataStore().getProduct(code);
	}
	
	public Order getOrderDetailsForUser(String username, String orderNumber) throws OrderNotFoundException {
		return new DataStore().getOrderForUser(username, orderNumber);
	}
	
	public Set<Order> getAllOrdersForUser(String username) {
		return new DataStore().getAllOrdersForUser(username);
	}
	
	public Wishlist getWishlistForUser(String username) throws WishlistNotFoundException {
		return new DataStore().getWishlistForUser(username);
	}
	
	// modify
	public void insertUser(User user) {
		List<User> users = DataStore.getUsers();
		users.add(user);
		DataStore.setUsers(users);
	}
}
