package com.powzy.entity;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import lombok.Getter;
import lombok.Setter;

import com.googlecode.objectify.annotation.Embed;

@Embed
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class OpeningTime implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5811341798631274084L;
	final static int MONDAY = 0;
	final static int TUESDAY = 1;
	final static int WEDNESDAY = 2;
	final static int THURSDAY = 3;
	final static int FRIDAY = 4;
	final static int SATURDAY = 5;
	final static int SUNDAY = 6;
	
	@Getter @Setter
	List<TimeBlock> weekDays = new ArrayList<TimeBlock>(7);
	/**
	 * set the monday to friday and saturday and sunday
	 * @param openingTime
	 * @param closingTime
	 * @param lunchStartingTime
	 * @param lunchClosingTime
	 */
	
	
	/*public void setWeekdayTiming(Long openingTime, Long closingTime,
			Long lunchStartingTime, Long lunchClosingTime) {
		//
		for(int day = MONDAY; day<=THURSDAY; day++) {
			this.setDayTime(day, openingTime, 
					closingTime, lunchStartingTime, 
					lunchClosingTime);
		}
		
		for(int day= FRIDAY; day<=SUNDAY; day++) {
			this.setDayTime(day, openingTime, closingTime,
					lunchStartingTime, lunchClosingTime);
		}
	}
	*/
	/*public void setDayTime(int day, Long startingTime, Long closingTime,
			Long lunchStartTime, Long lunchClosingTime) {
		DayTime ot = this.weekDays.get(day);
//		ot.setDayTime(startingTime, closingTime, lunchStartTime, lunchClosingTime);
	}*/
}


@Embed
class TimeBlock implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6798393365761721040L;

	@Getter @Setter
	Long startTime;
	
	@Getter @Setter
	Long endTime;
}
