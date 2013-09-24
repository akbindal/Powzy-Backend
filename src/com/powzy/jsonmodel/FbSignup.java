package com.powzy.jsonmodel;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@XmlRootElement
public class FbSignup implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5330130653045443706L;
	
	@Getter @Setter
	String oauthToken;
	
	@Getter @Setter
	Long id;
}
