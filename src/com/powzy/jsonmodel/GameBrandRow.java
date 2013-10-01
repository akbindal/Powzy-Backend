package com.powzy.jsonmodel;

import java.io.Serializable;

import javax.ws.rs.Path;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@XmlRootElement
@NoArgsConstructor
public class GameBrandRow implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7381695679138616754L;
	
	@Getter @Setter
	Long gameTypeId;
	
	@Getter @Setter
	String gameTitle;
	
	@Getter @Setter
	Integer totalLevel;
	
	@Getter @Setter
	String shortDesc;
	
	@Getter @Setter
	Double progress;
	
	@Getter @Setter
	String gameUrl;
	
	@Getter @Setter
	Long gameLaunchId;
	
	@Getter @Setter
	Long userGameId;
}
