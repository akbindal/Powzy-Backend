package com.powzy;


import com.googlecode.objectify.ObjectifyFactory;
import com.powzy.entity.*;
import com.powzy.game.GameLaunch;
import com.powzy.game.Level;
import com.powzy.game.UserGame;
import com.powzy.game.entity.GameType;
import com.powzy.game.param.GameParam;
import com.powzy.game.param.GymGameParam;
import com.powzy.game.status.GameStatus;
import com.powzy.game.status.GymGameStatus;
import com.powzy.game.status.GymUserStatus;
import com.powzy.game.status.UserStatus;

public class OfyFactory extends ObjectifyFactory {
	public OfyFactory() {
		long time = System.currentTimeMillis();
		
		this.register(Users.class);
		this.register(BusinessEntity.class);
		this.register(UserEmailLookup.class);
		this.register(Category.class);
		
	//	this.register(ContactDetail.class);
		this.register(BusinessEntityLookup.class);
		//this.register(OpeningTime.class);
		/**Game entities**/
		this.register(GameLaunch.class);
		this.register(GameParam.class);
		this.register(GymGameParam.class);
		this.register(UserGame.class);
		this.register(GameType.class);
		
		this.register(GymUserStatus.class);
		this.register(UserStatus.class);
		
		this.register(GameStatus.class);
		this.register(GymGameStatus.class);
		/**************/
		long millis = System.currentTimeMillis() - time;
		//Log.info("Registration took "+millis+"millis");
	}
	
	@Override
	public Ofy begin() {
		return new Ofy(this);
	}
}
