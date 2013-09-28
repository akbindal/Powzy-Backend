package com.powzy.game.param;

import java.util.Date;

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
	Date startDate;
	
	@Getter @Setter
	Integer wager;
}
