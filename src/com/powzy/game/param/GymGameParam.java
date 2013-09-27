package com.powzy.game.param;

import com.googlecode.objectify.annotation.EntitySubclass;

import lombok.Getter;
import lombok.Setter;

/**
 * To be set by the user
 * @author ABindal
 *
 */

@EntitySubclass(index=true)
public class GymGameParam extends GameParam{

	@Getter @Setter
	Integer[] commitedWorkOutdays = new Integer[7];
	
	@Getter @Setter
	Long startDate;
	
	@Getter @Setter
	Integer wager;
}
