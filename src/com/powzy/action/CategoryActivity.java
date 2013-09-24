package com.powzy.action;

import static com.powzy.OfyService.ofy;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.powzy.entity.Category;

@Path("/category")
public class CategoryActivity {
	@POST
	@Path("/put")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Category putCategory(Category category) {
		if(category ==null)
			return null;
		
		ofy().save().entity(category).now();
		return category;
	}
	
	public static Category getCategory(Long id) {
		Category category = ofy().load().type(Category.class).id(id).now();
		return category;
	}
}
