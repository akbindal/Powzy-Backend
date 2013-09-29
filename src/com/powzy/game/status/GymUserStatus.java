package com.powzy.game.status;

import com.googlecode.objectify.annotation.EntitySubclass;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EntitySubclass(index=true)
@NoArgsConstructor
public class GymUserStatus extends UserStatus {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6675729678304910616L;

	@Getter @Setter
	Integer totalEarning;
	
	//@Getter @Setter
//	Integer leftWorkout;
	
//	@Getter @Setter
//	Integer wager;
}
