package com.powzy.jsonmodel;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import com.powzy.game.param.GameParam;
import com.powzy.game.param.GymGameParam;

import lombok.Getter;
import lombok.Setter;

@XmlRootElement
public class PowzyGameInitiate extends UserGameInitiate{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5982935419900433794L;

	//Level
	@Getter @Setter
	Integer[] commitedWorkOutdays = new Integer[7];
	
	@Getter @Setter
	Long startDate;
	
	@Getter @Setter
	Integer wager;
	
	public static GymGameParam convertToParam(PowzyGameInitiate pgi) {
		GymGameParam gp = new GymGameParam();
		gp.setStartDate(new Date(pgi.getStartDate()));
		gp.setWager(pgi.getWager());
		gp.setCommitedWorkOutdays(pgi.getCommitedWorkOutdays());
		return gp;		
	}
	
}
