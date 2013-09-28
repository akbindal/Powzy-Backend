package com.powzy.game;

import lombok.Getter;
import lombok.Setter;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Embed;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Load;
import com.powzy.game.param.GameParam;
import com.powzy.game.status.GameStatus;

@Embed
public class Level {
	@Getter @Setter
	@Load Key<GameParam> userGameParam;
	 
	@Getter @Setter
	Double lastProgress;
	
	@Getter @Setter
	@Load Key<GameStatus> userGameStatus;
}
