package com.powzy.jsonmodel;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@XmlRootElement
public class UserGameInitiate implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5323377689786902085L;

	/**
	 * won't be part to the 
	 * userGameId
	 */
	
	@Getter @Setter
	Long gameLaunchId;
	
	@Getter @Setter
	Long userGameId;
	
	@Getter @Setter
	Long userId;
}
