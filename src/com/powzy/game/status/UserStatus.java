package com.powzy.game.status;


import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@XmlRootElement
public class UserStatus implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5195730236473719685L;

	@Id
	Long userGameStatusId;
	
	@Getter @Setter
	Double totalProgress;
	
	@Getter @Setter
	Integer lastLevel;
}
