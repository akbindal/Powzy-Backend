package com.powzy.jsonmodel;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.powzy.entity.UserEmailLookup;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@XmlRootElement
@NoArgsConstructor
public class Signup implements Serializable {
	/**
	 * 
	 */
	
	private static final long serialVersionUID = -2650250769731082456L;
	
	
	@Getter @Setter
	Integer signupType = null;
	
	@Getter @Setter
	Long id = null;
	
	@Getter @Setter
	String authId = null; 
	
	@Getter @Setter
	String authToken = null;
	
	//_powzy signup
	public  Signup(String authId, String authToken) {
		this.authId = authId;
		this.authToken = authToken;
		this.signupType = UserEmailLookup.POWZY_SIGNUP;
	}
	
}
