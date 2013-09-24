package com.powzy.entity;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
@NoArgsConstructor
@XmlRootElement
public class Users{
	/** */
	public static Key<Users> key(long id) {
		return Key.create(Users.class, id);
	}
	
	public static Users getNullUser() {
		Users user = new Users();
		user.setId(-1l);
		user.setAuthId(null);
		user.setFirstName("");
		user.setLastName("");
		user.setEmail("");
		return user;
	}
	/**
	 * Synthetic id
	 */
	@Id
	@Getter @Setter
	Long id;
	
	@Getter @Setter
	String firstName;
	
	@Getter @Setter
	String lastName;
	
	@Getter @Setter
	String authId; //it could be email or the fb user id
	
	@Getter @Setter
	String email;
	
	@Getter @Setter
	String profileUr;
	
	@Getter @Setter
	@Index List<Long> friends = new ArrayList<Long>();
	
	/**
	 * powzy signupUser
	 */
	public Users(String email) {
		this.authId = UserEmailLookup.normalize(authId);
		this.email = UserEmailLookup.normalize(authId);
	}
	
	public Users(String authId, String email) {
		this.authId = UserEmailLookup.normalize(authId);
		this.email = UserEmailLookup.normalize(email);
	}
	
	
}
