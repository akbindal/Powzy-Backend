package com.powzy.jsonmodel;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;


@XmlRootElement
public class GymInfo  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 668282633723631088L;
	@Getter @Setter
	String brandUrl;
	
	@Getter @Setter
	String gameTitle;
	
	@Getter @Setter
	String rules;
	
	@Getter @Setter
	Boolean pactValid;
}

