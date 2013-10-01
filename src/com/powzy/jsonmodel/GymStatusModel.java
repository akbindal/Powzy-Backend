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
	Integer totalPact;
	
	@Getter @Setter
	Integer totalEarning;
	
	@Getter @Setter
	Integer totalWorkout;
	
	@Getter @Setter
	LevelStatusResponse level;
}
