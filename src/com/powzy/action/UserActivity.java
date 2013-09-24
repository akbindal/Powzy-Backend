package com.powzy.action;

import static com.powzy.OfyService.ofy;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.powzy.entity.BusinessEntity;
import com.powzy.entity.UserEmailLookup;
import com.powzy.entity.Users;
import com.powzy.jsonmodel.BoolResponse;
import com.powzy.jsonmodel.Signup;
import com.powzy.util.WrongActionException;

@Path("/actions")
public class UserActivity {

	@POST
	@Path("/love/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public BoolResponse postBrandLove(Signup signup, 
			@PathParam("id") Long bussId,
			@QueryParam("status") Boolean lovestatus) {
		
		Users u=null;
		Integer signupType = signup.getSignupType();
		String authId = signup.getAuthId();
		String authToken = signup.getAuthToken();
		if(signupType==UserEmailLookup.FACEBOOK_SIGNUP)
			u = ofy().load().getFBUserbyId(authId);
		else if(signupType == UserEmailLookup.POWZY_SIGNUP)
			u = ofy().load().getPowzyAuthUser(authId, authToken);
		
		
		BusinessEntity brand = ofy().load().entityById(bussId);
		if(brand == null) throw new WrongActionException("brand not found");
		else {
			boolean x = brand.setLover(u.getId(), lovestatus);
			ofy().save().entity(brand).now();
			if (x) return new BoolResponse(true);
			else return new BoolResponse(false);
		}
		
	}
}
