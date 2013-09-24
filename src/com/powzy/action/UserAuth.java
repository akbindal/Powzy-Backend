package com.powzy.action;

import static com.powzy.OfyService.ofy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.powzy.entity.BusinessEntity;
import com.powzy.entity.UserEmailLookup;
import com.powzy.entity.Users;
import com.powzy.jsonmodel.FbUser;
import com.powzy.jsonmodel.Signup;
import com.powzy.util.UnauthorizedException;
import com.powzy.util.WrongActionException;

@Path("/users")
public class UserAuth {
	//post
	@POST
	@Path("/signup")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Users signupUser(final Signup newSignup) throws IOException {
		Integer signupType = newSignup.getSignupType();

		String authId = newSignup.getAuthId();
		//check with powzy
		//and third party if user can be created
		
		if(isUserExist(authId)) 
			throw new UnauthorizedException("user exist");
		String authToken = newSignup.getAuthToken();
		Users user;
		switch(signupType) {
			case UserEmailLookup.FACEBOOK_SIGNUP: {
				user = createFBUser(authId, authToken);
				break;
			}
			
			case UserEmailLookup.POWZY_SIGNUP: {
				user = createPowzyUser(authId, authToken);
				break;
			}
			default: throw new UnauthorizedException("Unsupported third party client");				
		}
		user = ofy().load().getUserLookup(authId).getEntity();
		if(user==null) throw new UnauthorizedException("database persistence error");
		return user;
	}
	
	Users createFBUser(String authId, String authToken) {
		//is fb authorized
		Users user = getFBAuthorizedUser(authId, authToken);
		ofy().save().entity(user).now();
		ofy().save().entity(
				new UserEmailLookup(authId, user)
				).now();
		if(user!=null) 
			return user;
		else 
			throw new UnauthorizedException("facebook login error");
	}
	
	boolean isUserExist(String authId) {
		UserEmailLookup userEmail = ofy().load().getUserLookup(authId);
		if(userEmail==null) 
			return false;
		return true;
	}
	
	Users createPowzyUser(String authId, String authToken) {
		Users user = new Users(authId);
		ofy().save().entity(user).now();
		ofy().save().entity(
				new UserEmailLookup(authId, authToken, user)
				).now();
		return user;
	}
	
	Users getFBAuthorizedUser(String authId, String authToken) {
		FbUser fbUser;
		fbUser = getFBUser(authId, authToken);
		try {
			Users user = fbUser.getUser();
			return user;
		} catch (Exception e) {
			throw new UnauthorizedException("facebook response error");
		}
	}
	
	FbUser getFBUser(String authId, String authToken) {
		FbUser fbUser;
		try {
			String graph;
			String g = "https://graph.facebook.com/me?fields="+authId+"&access_token="+authToken;
			URL u = new URL(g);
            URLConnection c = u.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
            String inputLine;
            StringBuffer b = new StringBuffer();
            while ((inputLine = in.readLine()) != null)
                b.append(inputLine + "\n");            
            in.close();
            graph = b.toString();
            if(graph==null || graph.trim().length()==0) throw new Exception();
            fbUser = new ObjectMapper().readValue(graph, FbUser.class);
            String fbId = fbUser.getId();
            if(fbUser==null ||fbId==null || fbId.trim().length()==0)
            	throw new Exception();
     
		} catch (Exception e) {
			throw new UnauthorizedException("facebook connection problem");
		}
		return fbUser;
	}
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON) 
	@Produces(MediaType.APPLICATION_JSON)
	public Users loginUser(final Signup oldSignup) throws UnauthorizedException
	{
		Integer signupType = oldSignup.getSignupType();
		String authId = oldSignup.getAuthId();
		String authToken = oldSignup.getAuthToken();
		if(!isUserExist(authId))
			throw new UnauthorizedException("wrong email or password");
		Users user=null;
		switch(signupType) {
			case UserEmailLookup.FACEBOOK_SIGNUP:
			{
				user = ofy().load().getFBUserbyId(authId);
				
				//facebook authorization
				//TODO:
			}
				
			case UserEmailLookup.POWZY_SIGNUP:
			{
				if(!isUserExist(authId))
				user = ofy().load().getPowzyAuthUser(authId, authToken);
			}
		}
		if(user == null) 
			throw new UnauthorizedException("user not found");
		else 
			return user;
	}
	
	@GET
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	public Users getUserDetail(@QueryParam("id") String stringId) {
		try {
			Long id = Long.valueOf(stringId);
			return ofy().load().userById(id);
		} catch (NumberFormatException e)  {
			return null;
		}
	}
	
	@POST
	@Path("/put")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Users putUserInfo(Users inUser) {
		Users user = ofy().load().userById(inUser.getId());
		user.setAuthId(inUser.getAuthId());
		user.setLastName(inUser.getLastName());
		user.setProfileUr(inUser.getProfileUr());
		user.setFirstName(inUser.getFirstName());
		user.setFriends(inUser.getFriends());
		ofy().save().entity(user).now();
		return user;
	}
	
	@GET
	@Path("/getfriend/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Users> getFriendList(@PathParam("id") Long id) {
		List<Users> friends = new ArrayList<Users>();
		Users user = ofy().load().userById(id);
		for(Long friendId:user.getFriends()){
			friends.add(ofy().load().userById(friendId));
		}
		return friends;
	}
}
