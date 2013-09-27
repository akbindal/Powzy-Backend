package com.powzy.jsonmodel;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.powzy.entity.BusinessEntity;
import com.powzy.entity.Users;
import com.powzy.game.GameLaunch;
import com.powzy.game.entity.GameType;

import lombok.Getter;
import lombok.Setter;

@XmlRootElement
public class GameLaunchInput implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 200716745027912196L;
	
	@Getter @Setter
	Long id;
	
	@Getter @Setter
	Long ownerId;
	
	@Getter @Setter
	Long gameTypeId;
	
	@Getter @Setter
	String longDesc;
	
	@Getter @Setter
	String shortDesc;
	
	@Getter @Setter
	String rules;
	
	@Getter @Setter
	Long dateCreated;
	
	@Getter @Setter
	String gameLogoUrl;
	
	public static GameLaunch getGameLaunch(GameLaunchInput in) {
		GameLaunch gl = new GameLaunch();
		gl.setDateCreated(in.getDateCreated());
		gl.setShortDesc(in.getShortDesc());
		gl.setLongDesc(in.getLongDesc());
		gl.setRules(in.getRules());
		Ref<BusinessEntity> bussEntity = Ref.create(Key.create(BusinessEntity.class, in.getOwnerId()));
		gl.setOwner(bussEntity);
		Ref<GameType> gameType = Ref.create(Key.create(GameType.class, in.getGameTypeId()));
		gl.setGameType(gameType);
		gl.setGameLogoUrl(in.getGameLogoUrl());
		return gl;
	}
}
