package com.powzy.entity;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import lombok.Getter;
import lombok.Setter;

import com.googlecode.objectify.annotation.Embed;

@Embed
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class ContactDetail implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5596027672446772057L;
	@Getter @Setter
	List<String> phoneNumbers;
	
	@Getter @Setter
	String email;
	
	@Getter @Setter
	Address address;
}
