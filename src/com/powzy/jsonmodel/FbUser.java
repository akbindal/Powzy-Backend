package com.powzy.jsonmodel;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.annotate.JsonAnySetter;
import com.powzy.entity.Users;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class FbUser implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1120394242511176543L;
	
	@Getter @Setter
	String first_name;
	
	@Getter @Setter
	String last_name;
	
	@Getter @Setter
	String email;
	
	@Getter @Setter
	String id;
	
	public Users getUser() {
		Users user = new Users();
		user.setAuthId(this.id);
		if(this.first_name!=null) 
			user.setFirstName(this.first_name);
		if(this.last_name!=null)
			user.setLastName(this.last_name);
		if(this.email!=null) 
			user.setEmail(this.email);
		return user;
	}
}
