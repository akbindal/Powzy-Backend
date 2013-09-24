package com.powzy;

import com.googlecode.objectify.impl.LoaderImpl;
import com.powzy.entity.BusinessEntity;
import com.powzy.entity.BusinessEntityLookup;
import com.powzy.entity.UserEmailLookup;
import com.powzy.entity.Users;
import com.powzy.util.UnauthorizedException;

import static com.powzy.OfyService.ofy;


public class OfyLoader extends LoaderImpl<OfyLoader> {
	/** */
	public OfyLoader(Ofy base) {
		super(base);
	}
	
	/** add other loader elements here **/
	
	
	/**
	 * Gets the User associated with the email, or null if there is no association.
	 */
	
	public UserEmailLookup getUserLookup(String authId) {
		return ofy().load().
				type(UserEmailLookup.class)
				.id(UserEmailLookup.normalize(authId)).now();
	}
	
	public Users getFBUserbyId(String authId) {
		if(authId==null || authId.trim().length()==0)
			return null;
		UserEmailLookup uEmailLookup = 
				getUserLookup(authId);
		//if(uEmailLookup.get)
		return uEmailLookup.getEntity();
	}
	
	public Users getPowzyAuthUser(String email,
			String password) {
		if(password==null || password.trim().length()==0)
			return null;
		UserEmailLookup userDB = getUserLookup(email);
		if(userDB==null)
			return null;
		String authToken = userDB.getPassword();
		if(password.contains(authToken))
			return userDB.getEntity();
		else
			return null;
	}
		
	public Users userById(Long id) {
		if(id==null) 
			return null;
		Users user = ofy().load()
				.type(Users.class)
				.id(id).now();
		return user;
	}
	
	/******************/
	public BusinessEntity authEntiyByEmail(String email, String password) {
		if(email == null || email.trim().length()==0) 
			return null;
		BusinessEntityLookup lookup = getLookup(email);
	
		if(lookup == null)
			return null;
		else if(lookup.getPassword().contentEquals(password))
			return lookup.getEntity();
		else
			return null;
	}
	
	public BusinessEntity entityByEmail(String email) {
		if(email == null || email.trim().length()==0) 
			return null;
		BusinessEntityLookup lookup = getLookup(email);
		if(lookup == null)
			return null;
		else 
			return lookup.getEntity();
	}
	
	BusinessEntityLookup getLookup(String email) {
		return ofy().load().
				type(BusinessEntityLookup.class).
				id(BusinessEntityLookup.normalize(email)).now();
	}
	
	public BusinessEntity entityById(Long id) {
		if(id==null|| id < 0l) 
			return null;
		
		BusinessEntity result = ofy().load().type(BusinessEntity.class).id(id).now();
		return result;
	}
}
