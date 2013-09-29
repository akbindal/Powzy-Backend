package com.powzy.jsonmodel;

import java.io.Serializable;


import lombok.Getter;
import lombok.Setter;

public class GymStatusModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6479765449076760116L;

	
	@Getter @Setter
	Integer lastPacts;
	
	@Getter @Setter
	Integer totalEarning;
	
	@Getter @Setter
	LevelStatusResponse level;
}
