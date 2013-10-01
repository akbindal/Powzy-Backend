package com.powzy.jsonmodel;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class LevelStatusResponse implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6322574719319452559L;

	@Getter @Setter
	Integer level;

	@Getter @Setter
	Integer[] committedWorkDays = new Integer[7];
	
	@Getter @Setter
	Integer[] workOutDays = new Integer[7];
	
	@Getter @Setter
	Integer wager;
	
	@Getter @Setter
	Integer winningPrize;
}
