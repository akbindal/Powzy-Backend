package com.powzy.game.status;


import com.googlecode.objectify.annotation.EntitySubclass;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EntitySubclass(index=true)
@NoArgsConstructor
public class GymGameStatus extends GameStatus {
	public GymGameStatus(boolean isCompleted) {
		super(isCompleted);
		this.earning = 0;
		// TODO Auto-generated constructor stub
	}

	@Getter @Setter
	Integer[] workOutDays = new Integer[7];
	
	@Getter @Setter
	Integer earning;
}
