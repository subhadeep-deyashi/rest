package com.tienda.resource;

import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.tienda.bean.Order;
import com.tienda.service.TiendaService;
import com.tienda.util.OrderNotFoundException;

// SUB-resource class
public class OrderResource {

	@GET
	@Produces(value={MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Path(value="{ord-num}")	
	public Order fetchOrderDetailsForUser(@PathParam(value="uname")String username, 
											@PathParam(value="ord-num")String orderNumber) 
													throws OrderNotFoundException {
		return new TiendaService().getOrderDetailsForUser(username, orderNumber);
	}
	
	@GET
	@Produces(value={MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Set<Order> fetchAllOrdersForUser(@PathParam(value="uname")String username) {
		return new TiendaService().getAllOrdersForUser(username);
	}
}
