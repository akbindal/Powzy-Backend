package com.powzy.game.param;

import lombok.Getter;
import lombok.Setter;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;


/**
 * default are set by business
 * @author ABindal
 *
 */
@Entity
public class GameParam {
	public Key<GameParam> key(long id) {
		return Key.create(GameParam.class, id);
	}
	
	@Id
	@Getter @Setter
	Long id;
	
	//@Getter @Setter
	//Ref<GameLaunch> associatedGame;
}
