package com.powzy.jsonmodel;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@XmlRootElement
public class GymCheckin implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 668282633723631088L;
	@Getter @Setter
	Long currentDate;
	
	@Getter @Setter
	Long userGameId;
}
