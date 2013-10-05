package com.powzy.entity;

import java.io.Serializable;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import lombok.Getter;
import lombok.Setter;

import com.googlecode.objectify.annotation.Embed;

@Embed
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Address implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 479502406753768464L;
	
	@Getter @Setter
	String streetName;
	
	@Getter @Setter
	String number;
	
	@Getter @Setter
	String city;
	
	@Getter @Setter
	Integer zipcode;
}
