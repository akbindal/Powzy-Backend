package com.powzy.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.Parent;
import com.powzy.entity.Users;
import com.powzy.game.status.UserStatus;

@Entity
public class UserGame {
	/*
	 * levels in the game
	 */
	
	@Id
	@Getter @Setter
	Long userGameId;
	
	@Getter @Setter
	@Index Key<Users> user;
	
	@Getter @Setter
	List<Level> level = new ArrayList<Level>();
	
	@Getter @Setter
	@Load Key<UserStatus> userStatus;
	
	
	@Getter @Setter
	@Parent Key<GameLaunch> gameLaunch;
	
//user feed
}
