package com.powzy.game.status;

import com.googlecode.objectify.annotation.EntitySubclass;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EntitySubclass(index=true)
@NoArgsConstructor
public class GymUserStatus extends UserStatus {
	@Getter @Setter
	Integer totalEarning;
	
	@Getter @Setter
	Integer leftWorkout;
	
	@Getter @Setter
	Integer wager;
}
