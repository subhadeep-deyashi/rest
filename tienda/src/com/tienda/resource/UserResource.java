package com.tienda.resource;

import java.util.HashSet;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.tienda.bean.ExceptionMessage;
import com.tienda.bean.Link;
import com.tienda.bean.User;
import com.tienda.service.TiendaService;
import com.tienda.util.UserNotFoundException;

// ROOT-resource class (request will hit this class first)
@Path(value = "/users")
public class UserResource {
	
	@Context		// dependency injection
	private ResourceContext resourceContext;

	@Context		// dependency injection
	private UriInfo uriInfo;		// to dynamically generate URL's
	
	
	// resource method
	@GET
	@Produces(value={MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Path(value="{uname}")		// template parameter
	public Response fetchUserDetails(@PathParam(value = "uname") String username) throws UserNotFoundException {
		User user = new TiendaService().getUserDetails(username);
		
		user.setLinks(new HashSet<Link>());
		
		
		UriBuilder baseBuilder = uriInfo.getBaseUriBuilder();	// generate base URL
		baseBuilder.path(UserResource.class);					// add the user resource to URL
		baseBuilder.path(UserResource.class, "getSubResource").resolveTemplate("uname", username);	// appends the uname part of the URL
		
		
		user.getLinks().add(new Link(UriBuilder.fromPath(baseBuilder.toTemplate())
				.resolveTemplate("sub-resource-name", "orders").build().toString(), 
				String.format("%s,%s", MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML)));
		
		user.getLinks().add(new Link(UriBuilder.fromPath(baseBuilder.toTemplate())
				.resolveTemplate("sub-resource-name", "wishlist").build().toString(), 
				String.format("%s,%s", MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML)));
		
		
		return Response.ok(user).build();
	}
	/*
	 * the above method can also look like this
	 * 
	 * public User fetchUserDetails(@PathParam(value = "uname") String username) throws UserNotFoundException {
		User user = new TiendaService().getUserDetails(username);
		return user;
	}
	 */
	
	
	
	// resource method
	@POST
	@Consumes(value={MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public void createUser(User user) {
		new TiendaService().insertUser(user);
	}
	
	
	// SUB-resource locator
	// for redirecting the request from here to the desired resource class 
	@Path(value="{uname}/{sub-resource-name}")
	public Object getSubResource(@PathParam(value="sub-resource-name")String subResourceName) {
		if ("orders".equals(subResourceName)) {
			return resourceContext.getResource(OrderResource.class);
		} else if ("wishlist".equals(subResourceName)) {
			//return resourceContext.getResource(WishlistResource.class);
		}
		
		throw new WebApplicationException(
				Response.status(Response.Status.BAD_REQUEST).entity(
						new ExceptionMessage("no such resource")).build()
				);
		
	}
}
