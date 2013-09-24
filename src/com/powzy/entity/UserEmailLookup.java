package com.powzy.entity;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;

@Entity
@NoArgsConstructor
@XmlRootElement
public class UserEmailLookup{
	public final static int FACEBOOK_SIGNUP = 0;
	public final static int GOOGLE_SIGNUP = 1;
	public final static int POWZY_SIGNUP = 2;
	
	
	
	/** Use this method to normalize email addresses for lookup */
	public static String normalize(String email) {
		return email.toLowerCase();
	}
	
	@Id
	@Getter @Setter
	String authId; // it could be normal email or
	//fb user id
	//Google Id
	
	@Getter @Setter
	Integer signupType;
	
	/**
	 * needed for powzy
	 */
	@Getter @Setter
	String password;
	
	@Getter @Setter
	Long created;
	
	@Index
	@Load
	Ref<Users> entity;
	
	/**
	 * powzy creation 
	 * */
	public UserEmailLookup(String email, String password,
			Users user) {
		this.setCreated(new Date().getTime());
		this.authId = normalize(email);
		this.entity = Ref.create(user);
		this.password = password;
		this.signupType = UserEmailLookup.POWZY_SIGNUP;
	}
	
	/***
	 * fb user creation
	 * @param authId
	 * @param user
	 */
	public UserEmailLookup(String authId, Users user) {
		this.authId = authId;
		this.entity = Ref.create(user);
		this.password = "";
		this.signupType = UserEmailLookup.FACEBOOK_SIGNUP;
		this.setCreated(new Date().getTime());
	}
	
	public Users getEntity() {
		return entity.get();
	}
}
