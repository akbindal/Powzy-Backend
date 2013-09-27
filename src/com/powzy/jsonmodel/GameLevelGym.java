package com.powzy.jsonmodel;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class GameLevelGym {
	@Getter @Setter
	Double lastProgress;
	
	@Getter @Setter
	Long userGameParamId;
	
	@Getter @Setter
	Integer[] commitedDays = new Integer[7];
	
	@Getter @Setter
	Long startDate;
	
	@Getter @Setter
	Integer wager;
}
